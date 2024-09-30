pipeline {
  agent {
    label 'docker-compose'
  }

  options {
    disableConcurrentBuilds()
    buildDiscarder(logRotator(numToKeepStr: '60', artifactNumToKeepStr: '30'))
  }

  triggers {
    cron 'H H/4 * * *'
  }

  stages {
    stage('test') {
      steps {
        script {          
            pullEngineImage()
            examples().each { entry ->
              def example = entry.key
              def assertion = entry.value
              runTest(example, assertion)
            }          
        }
      }
    }

    stage('archive') {
      steps {        
        archiveArtifacts allowEmptyArchive: true, artifacts: 'warn*.log'
      }
    }
  }
}

def pullEngineImage() {
  sh 'docker pull axonivy/axonivy-engine:dev'
}

def examples() {
  return [
    'ivy': { assertIvyIsRunningInDemoMode() },
    'ivy-systemdb-postgres': { assertIvyIsNotRunningInDemoMode() },
    'ivy-systemdb-mysql': { assertIvyIsNotRunningInDemoMode() },
    'ivy-systemdb-mariadb': { assertIvyIsNotRunningInDemoMode() },
    'ivy-systemdb-mssql': { assertIvyIsNotRunningInDemoMode() },
    'ivy-sso-saml': { assertSSO() },
    'ivy-sso-openid-connect': { assertSSO() },
    'ivy-deploy-app': { assertAppIsDeployed("myApp") },
    'ivy-branding': { assertBranding() },
    'ivy-opensearch': { assertOpenSearch() },      
    'ivy-environment-variables': { assertIvyIsNotRunningInDemoMode() },
    'ivy-logging': { assertIvyConsoleLog("ivy-logging", "Loaded configurations of '/ivy/configuration") },
    'ivy-reverse-proxy-nginx': { assertReverseProxy() },
    'ivy-reverse-proxy-apache': { assertReverseProxy() },
    'ivy-openldap': { assertOpenLdap() },
    'ivy-patching': { assertPatching() },
    'ivy-secrets': { assertIvyIsNotRunningInDemoMode() },
    'ivy-valve': { assertValve() },
    'ivy-custom-errorpage': { assertCustomErrorPage() },
    
    // disabled because highly flaky
    //'ivy-scaling-nginx': { assertIvyIsNotRunningInDemoModeOnPort(80) }

    //disabled because it needs to much power for our build-nodes
    //'ivy-elasticsearch-cluster': { assertElasticsearchCluster() },
    
    // only works with docker-compose v1
    //'ivy-scaling-haproxy': { assertIvyIsNotRunningInDemoModeOnPort(80) },
  ]
}

def runTest(def example, def assertion) {
  echo "==========================================================="
  echo "START TESTING EXAMPLE $example"
  try {
    dockerComposeDown(example) // remove previous created containers
    dockerComposeUp(example)
    echo "Wait until ivy is running"
    waitUntilIvyIsRunning(example)
    echo "Test sample"
    assertion.call()
    if (!example.equals('ivy-scaling-nginx')) {
      assertNoErrorOrWarnInIvyLog(example)
    }
  } catch (ex) {
    currentBuild.result = 'UNSTABLE'
    echo ex.getMessage()
    def log = "warn-${example}.log"
    sh "echo SAMPLE ${example} FAILED >> ${log}"              
    sh "echo =========================================================== >> ${log}"
                
    sh "echo \"Error Message: ${ex.getMessage()}\" >> ${log}"   
    sh "echo =========================================================== >> ${log}"
                
    sh "echo DOCKER-COMPOSE BUILD LOG: >> ${log}"  
    sh "cat docker-compose-build.log >> ${log}"
    sh "echo =========================================================== >> ${log}"

    sh "echo DOCKER-COMPOSE UP LOG: >> ${log}"
    sh "docker compose -f ${example}/compose.yaml logs >> ${log}"
  } finally {
    sh 'rm docker-compose-build.log'
    echo getIvyConsoleLog(example)
    dockerComposeDown(example)
    echo "==========================================================="
  }
}

def dockerComposeUp(example) {
  sh "docker compose -f $example/compose.yaml build >> docker-compose-build.log"
  sh "docker compose -f $example/compose.yaml up -d"
}

def dockerComposeDown(example) {
  sh "docker compose -f $example/compose.yaml down"
}

def waitUntilIvyIsRunning(def example) {
  timeout(3) {
    waitUntil {      
      def response = sh (script: "docker compose -f $example/compose.yaml logs ivy", returnStdout: true)
      return response.contains('Axon Ivy Engine is running and ready to serve');
    }
  }
}

def assertIvyIsRunningInDemoMode() {
  if (!isIvyRunningInDemoMode()) {
    throw new Exception("ivy is not running in demo mode")
  }
  if (isIvyRunningInMaintenanceMode()) {
    throw new Exception("ivy is running in maintenance mode");
  }
}

def assertIvyIsNotRunningInDemoMode() {
  assertIvyIsNotRunningInDemoModeOnPort(8080)  
}

def assertIvyIsNotRunningInDemoModeOnPort(port) {
  if (isIvyRunningInDemoModeOnPort(port)) {      
    throw new Exception("ivy is running in demo mode")
  }
  if (isIvyRunningInMaintenanceModeOnPort(port)) {
    throw new Exception("ivy is running in maintenance mode");
  }
}

def assertSSO() {
  sleep 120
  def response = sh (script: 'curl -k -L https://localhost', returnStdout: true)
  if (!response.contains('Sign in to ivy-demo')) {
    throw new Exception("not redirected to keycloak login page " + response)    
  }
}

def isIvyRunningInDemoMode() {
  isIvyRunningInDemoModeOnPort(8080)
}

def isIvyRunningInDemoModeOnPort(port) {
  sh "wget -O- http://localhost:$port"
  def response = sh (script: "wget -qO- http://localhost:$port", returnStdout: true)
  return response.contains('demoMode')
}

def isIvyRunningInMaintenanceMode() {
  isIvyRunningInMaintenanceModeOnPort(8080)
}

def isIvyRunningInMaintenanceModeOnPort(port) {
  def response = sh (script: "wget -qO- http://localhost:$port", returnStdout: true)
  return response.contains('Maintenance Mode')
}

def assertOpenLdap() {
  waitUntilAppIsReady('ldap')
  // using basic auth mechanism to login (process servlet has basic auth filter)
  // even if no login is required for process start, the request will fail, if authentication is wrong
  timeout(2) {
    waitUntil {
      def response = sh (script: "curl 'http://localhost:8080/ldap/pro/quick-start-tutorial/148655DDB7BB6588/start.ivp' --user rwei:rwei -L -i -b cookie.txt -s -o /dev/null -D/dev/stdout", returnStdout: true)
      return !response.contains("401");
    }
  }
}

def assertAppIsDeployed(appName) {
  waitUntilAppIsReady(appName)
  def response = sh (script: "wget -qO- http://localhost:8080/$appName/", returnStdout: true)
  if (response.contains("404")) {
    throw new Exception("app $appName is not deployed");
  }
}

def assertBranding() {
  waitUntilAppIsReady('demo-portal')
  def customCss = sh (script: "wget -qO- http://localhost:8080/demo-portal/faces/javax.faces.resource/custom.css?ln=xpertivy-branding", returnStdout: true)
  if (!customCss.contains("--sidebar-bg-color: #e20512;")) {
    throw new Exception("branding not active, default custom.css is served");
  }
}

def waitUntilAppIsReady(def appName) {
  // apps will be deployed async while starting engines
  // we could make a http request to the new app, but if its not exiting this will create ERROR messages in logs
  sleep 10
}

def assertIvyConsoleLog(example, message) {
  def log = getIvyConsoleLog(example)
  if (!log.contains(message)) {
    throw new Exception("console log of ivy does not contain $message");
  }
}

def assertNoErrorOrWarnInIvyLog(example) {
  def log = getIvyConsoleLog(example)

  // remove false-positive warnings for mariadb
  log = log.replace("WARN] (main) Error: 1049-42000:", "")
  log = log.replace("WARN] (System Database Creator Thread) Error: 1049-42000:", "")

  if (log.contains("WARN") || log.contains("ERROR")) {
    throw new Exception("console log of ivy contains WARN/ERROR messages");
  }
}

def getIvyConsoleLog(example) {
  return sh (script: "docker compose -f $example/compose.yaml logs ivy", returnStdout: true)
}

def assertPatching() {
  def sample = "ivy-patching"
  assertIvyConsoleLog(sample, "Install patches for classes:")
  assertIvyConsoleLog(sample, "patches/service-manager-patch.jar ->")
  assertIvyConsoleLog(sample, "ch.ivyteam.ivy.service.internal.ServiceManager")
  assertIvyConsoleLog(sample, "starting patched service manager")
}

def assertValve() {
  sh (script: "wget -qO- http://localhost:8080", returnStdout: true)
  def log =  sh (script: "docker exec ivy-valve cat logs/ivy.log", returnStdout: true)
  if (!log.contains("Header -->")) {
    throw new Exception("ivy.log of ivy does not contains Header -->");
  }  
}

def assertReverseProxy() {
  def response = sh (script: "wget --no-check-certificate -qO- https://localhost/", returnStdout: true)
  if (!followDefaultPageRedirect("https://localhost", response).contains('Welcome')) {
    throw new Exception("reverse proxy server does not redirect to portal login page");
  }
}

def assertOpenSearch() {
  // 1. Deploy Test Project
  sh "docker cp test.iar ivy-opensearch:/ivy/deploy/test.zip"  
  sleep(5) // wait until is deployed

  // 2. Execute Process which create business data
  sleep(10) // FIXME: sometimes es cluster is still not available. we have to fix that in the engine startup, wait on OpenSearch.
  sh "curl 'http://localhost:8080/test/pro/test/1665799EBA281E4C/start.ivp'"

  // 3. Query Elastic Search
  checkBusinessDataIndex(9200); 
}

def assertElasticsearchCluster() {
  // 1. Deploy Test Project
  sh "docker cp test.iar ivy-elasticsearch-cluster:/ivy/deploy/test.zip"  
  sleep(5) // wait until is deployed

  // 2. Execute Process which create business data
  sh "curl 'http://localhost:8080/test/pro/test/1665799EBA281E4C/start.ivp'"

  // 3. Query Elastic Search
  checkBusinessDataIndex(9201);
  checkBusinessDataIndex(9202);
  checkBusinessDataIndex(9203);
}

def checkBusinessDataIndex(port) {
  timeout(2) {
    waitUntil {
      def url = "http://localhost:$port/_cat/indices"
      def response = sh (script: "curl $url", returnStdout: true)
      def index = "ivy-default-businessdata-test.testbusinessdata";  
      echo "osearch engine response: $response"
      return response.contains(index);
    }
  }
}

def assertCustomErrorPage() {
  def response = sh (script: "curl http://localhost:8080/faces/notfound.xhtml", returnStdout: true)
  if (!response.contains('Please contact the system administrator')) {
    throw new Exception("could not load custom error page");
  }
}

def followDefaultPageRedirect(base, redirectPage) {
  def url = redirectPage.split('<meta http-equiv=\"refresh\" content=\"0; URL=')[1]
  url = base + url.split('\" />')[0]
  return sh (script: "wget --no-check-certificate -qO- $url", returnStdout: true)
}
