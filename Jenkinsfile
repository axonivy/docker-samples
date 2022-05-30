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
    stage('pull-image') {
      steps {
        script {
          pullEngineImage()
        }
      }
    }
    
    stage('test-compose') {
      steps {
        script {          
          def compose = load 'compose.groovy'
          examples().each { entry ->
            def example = entry.key
            def assertion = entry.value
            compose.newCompose(example).runTest(assertion);
          }          
        }
      }
    }

    stage('test-kubernetes') {
      steps {
        script {          
          def kubernetes = load 'kubernetes.groovy'
          examples().each { entry ->
            def example = entry.key
            def assertion = entry.value
            kubernetes.newKubernetes(example).runTest(assertion)
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
    'ivy': { log -> assertIvyIsRunningInDemoMode() },
    'ivy-systemdb-postgres': { log -> assertIvyIsNotRunningInDemoMode() },
    'ivy-systemdb-mysql': { log -> assertIvyIsNotRunningInDemoMode() },
    'ivy-systemdb-mariadb': { log -> assertIvyIsNotRunningInDemoMode() },
    'ivy-systemdb-mssql': { log -> assertIvyIsNotRunningInDemoMode() },
    'ivy-sso-saml': { log -> assertSSO() },
    'ivy-sso-openid-connect': { log -> assertSSO() },
    'ivy-deploy-app': { log -> assertAppIsDeployed("myApp") },
    'ivy-branding': { log -> assertBranding() },
    'ivy-elasticsearch': { log -> assertElasticsearch() },  
    'ivy-elasticsearch-cluster': { log -> assertElasticsearchCluster() },
    'ivy-environment-variables': { log -> assertIvyIsNotRunningInDemoMode() },
    'ivy-logging': { log -> assertIvyConsoleLog(log, "Loaded configurations of '/usr/lib/axonivy-engine/configuration") },
    'ivy-reverse-proxy-nginx': { log -> assertReverseProxy() },
    'ivy-reverse-proxy-apache': { log -> assertReverseProxy() },
    'ivy-openldap': { log -> assertOpenLdap() },
    'ivy-patching': { log -> assertPatching(log) },
    'ivy-secrets': { log -> assertIvyIsNotRunningInDemoMode() },
    'ivy-valve': { log -> assertValve() },
    'ivy-custom-errorpage': { log -> assertCustomErrorPage() },
    'ivy-scaling-haproxy': { log -> assertIvyIsNotRunningInDemoModeOnPort(80) },
    'ivy-scaling-nginx': { log -> assertIvyIsNotRunningInDemoModeOnPort(80) }
  ]
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
  def response = sh (script: "wget -qO- http://localhost:$port/info/index.jsp", returnStdout: true)
  return response.contains('Demo Mode')
}

def isIvyRunningInMaintenanceMode() {
  isIvyRunningInMaintenanceModeOnPort(8080)
}

def isIvyRunningInMaintenanceModeOnPort(port) {
  def response = sh (script: "wget -qO- http://localhost:$port/info/index.jsp", returnStdout: true)
  return response.contains('Maintenance Mode')
}

def assertOpenLdap() {
  waitUntilAppIsReady('ldap')
  // using basic auth mechanism to login (process servlet has basic auth filter)
  // even if no login is required for process start, the request will fail, if authentication is wrong
  def response = sh (script: "curl 'http://localhost:8080/ldap/pro/quick-start-tutorial/148655DDB7BB6588/start.ivp' --user rwei:rwei -L -i -b cookie.txt -s -o /dev/null -D/dev/stdout", returnStdout: true)
  if (response.contains("401")) { // 
    throw new Exception("could not login to app ldap as rwei/rwei");
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

def assertIvyConsoleLog(log, message) {
  if (!log.contains(message)) {
    throw new Exception("console log of ivy does not contain $message");
  }
}

def assertPatching(log) {
  assertIvyConsoleLog(log, "Install patches for classes: ch.ivyteam.ivy.service.internal.ServiceManager")
  assertIvyConsoleLog(log, "starting patched service manager")
}

def assertValve() {
  def log =  sh (script: "docker exec ivy-valve_ivy_1 cat logs/ivy.log", returnStdout: true)
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

def assertElasticsearch() {
  // 1. Deploy Test Project
  sh "docker cp test.iar ivy-elasticsearch_ivy_1:/usr/lib/axonivy-engine/deploy/test.zip"  
  sleep(5) // wait until is deployed

  // 2. Execute Process which create business data
  sleep(10) // FIXME: sometimes es cluster is still not available. we have to fix that in the engine startup, wait on elasticsearch.
  sh "curl 'http://localhost:8080/test/pro/test/1665799EBA281E4C/start.ivp'"

  // 3. Query Elastic Search
  checkBusinessDataIndex(9200); 
}

def assertElasticsearchCluster() {
  // 1. Deploy Test Project
  sh "docker cp test.iar ivy-elasticsearch-cluster_ivy_1:/usr/lib/axonivy-engine/deploy/test.zip"  
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
      def elasticSearchIndex = "ivy.businessdata-default-test.testbusinessdata";  
      echo "elastic search response: $response"
      return response.contains(elasticSearchIndex);
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
