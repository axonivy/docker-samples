pipeline {
  agent {
    label 'docker-compose'
  }

  options {
    disableConcurrentBuilds()
  }

  triggers {
    cron '@hourly'
  }

  stages {
    stage('pull-engine') {
      steps {
        pullEngineImage()             
      }
    }

    stage('test') {
      steps {
        script {
          def examples = [
            'ivy': { assertIvyIsRunningInDemoMode() },
            'ivy-systemdb-postgres': { assertIvyIsNotRunningInDemoMode() },
            'ivy-systemdb-mysql': { assertIvyIsNotRunningInDemoMode() },
            'ivy-systemdb-mariadb': { assertIvyIsNotRunningInDemoMode() },
            'ivy-systemdb-mssql': { assertIvyIsNotRunningInDemoMode() },
            'ivy-deploy-app': { assertAppIsDeployed("app") },
            'ivy-elasticsearch': { assertElasticsearch() },  
            'ivy-elasticsearch-cluster': { assertElasticsearchCluster() },
            'ivy-environment-variables': { assertIvyIsNotRunningInDemoMode() },
            'ivy-logging': { assertIvyConsoleLog("ivy-logging", "Loaded configurations of '/etc/axonivy-engine-7x/ivy.yaml'") },
            'ivy-reverse-proxy-nginx': { assertFrontendServerNginx() },
            'ivy-reverse-proxy-apache': { assertFrontendServerApache() },
            'ivy-openldap': { assertLogin("ldap", "rwei", "rwei") },
            'ivy-patching': { assertPatching() },
            'ivy-secrets': { assertIvyIsNotRunningInDemoMode() },
          ]

          examples.each { entry ->
            def example = entry.key;
            def assertion = entry.value;

            echo "==========================================================="
            echo "START TESTING EXAMPLE $example"
            try {
              dockerComposeUp(example)
              waitUntilIvyIsRunning()
              assertion.call()
              assertNoErrorOrWarnInIvyLog(example)
            } catch (ex) {
              currentBuild.result = 'UNSTABLE'
              echo ex.getMessage()
              sh "echo \"${ex.getMessage()}\" >> warn.log"
              sh 'cat docker-compose-up.log >> warn.log'
              writeDockerLog(example);
            } finally {
              sh 'rm docker-compose-up.log'
              echo getIvyConsoleLog(example)
              dockerComposeDown(example)
              echo "==========================================================="
            }
          }
        }
      }
    }

    stage('archive') {
      steps {        
        archiveArtifacts allowEmptyArchive: true, artifacts: 'warn.log'
      }
    }
  }
}


def pullEngineImage() {
  sh 'docker pull axonivy/axonivy-engine:dev'
}

def dockerComposeUp(example) {
  sh "docker-compose -f $example/docker-compose.yml up -d > docker-compose-up.log"
}

def dockerComposeDown(example) {
  sh "docker-compose -f $example/docker-compose.yml down"
}

def writeDockerLog(example) {
  sh "docker-compose -f $example/docker-compose.yml logs ivy >> warn.log"
}

def waitUntilIvyIsRunning() {
  timeout(2) {
    waitUntil {
      def r = sh script: 'wget -q http://localhost:8080/ivy -O /dev/null', returnStatus: true
      return (r == 0);
    }
  }
}

def assertIvyIsRunningInDemoMode() {
  if (!isIvyRunningInDemoMode()) {
    throw new Exception("ivy is not running in demo mode")
  }
}

def assertIvyIsNotRunningInDemoMode() {
  if (isIvyRunningInDemoMode()) {      
    throw new Exception("ivy is running in demo mode")
  }
}

def isIvyRunningInDemoMode() {
  def response = sh (script: "wget -qO- http://localhost:8080/ivy/info/index.jsp", returnStdout: true)
  return response.contains('Demo Mode')
}

def assertLogin(appName, user, password) {
  def response = sh (script: "curl 'http://localhost:8080/ivy/wf/$appName/login.jsp' --data 'username=$user&password=$password' -L -i -b cookie.txt", returnStdout: true)
  if (!response.contains("Logged in as $user")) {
    throw new Exception("could not login to app $appName as $user");
  }
}

def assertAppIsDeployed(appName) {
  def response = sh (script: "wget -qO- http://localhost:8080/ivy/wf/$appName/applicationHome", returnStdout: true)
  if (!response.contains("This is the home of the application $appName")) {
    throw new Exception("app $appName is not deployed");
  }
}

def assertIvyConsoleLog(example, message) {
  def log = getIvyConsoleLog(example)
  if (!log.contains(message)) {
    throw new Exception("console log of ivy does not contain $message");
  }
}

def assertNoErrorOrWarnInIvyLog(example) {
  def log = getIvyConsoleLog(example)
  if (log.contains("WARN") || log.contains("ERROR")) {
    throw new Exception("console log of ivy contains WARN/ERROR messages");
  }
}

def getIvyConsoleLog(example) {
  return sh (script: "docker-compose -f $example/docker-compose.yml logs ivy", returnStdout: true)
}

def assertPatching() {
  def sample = "ivy-patching"
  assertIvyConsoleLog(sample, "Install patches for classes: ch.ivyteam.ivy.globalvars.GlobalVariableManager")
  assertIvyConsoleLog(sample, "This Global Variable has been patched for Demo Purpose")
}

def assertFrontendServerNginx() {
  def response = sh (script: "wget -qO- http://localhost/", returnStdout: true)
  if (!response.contains('Welcome')) {
    throw new Exception("frontend server does not redirect to portal login page");
  }
}

def assertFrontendServerApache() {
  def response = sh (script: "wget -qO- http://localhost/ivy", returnStdout: true)
  if (!response.contains('Demo')) {
    throw new Exception("frontend server does not route to ivy");
  }
}

def assertElasticsearch() {
  // 1. Deploy Test Project
  sh "docker cp test.iar ivy-elasticsearch_ivy_1:/usr/lib/axonivy-engine/deploy/test.zip"  
  sleep(5) // wait until is deployed

  // 2. Execute Process which create business data
  sh "curl 'http://localhost:8080/ivy/pro/test/test/1665799EBA281E4C/start.ivp'"

  // 3. Query Elastic Search
  checkBusinessDataIndex(9200); 
}

def assertElasticsearchCluster() {
  // 1. Deploy Test Project
  sh "docker cp test.iar ivy-elasticsearch-cluster_ivy_1:/usr/lib/axonivy-engine/deploy/test.zip"  
  sleep(5) // wait until is deployed

  // 2. Execute Process which create business data
  sh "curl 'http://localhost:8080/ivy/pro/test/test/1665799EBA281E4C/start.ivp'"

  // 3. All Nodes are available
  checkElasticsearchHealth(9201);
  checkElasticsearchHealth(9202);
  checkElasticsearchHealth(9203);  

  // 3. Query Elastic Search
  checkBusinessDataIndex(9201);
  checkBusinessDataIndex(9202);
  checkBusinessDataIndex(9203);  
}

def checkElasticsearchHealth(port) {
  def response = sh (script: "curl http://localhost:$port/_cat/health --user elastic:changeme", returnStdout: true)
  if (!response.contains("green")) {
    throw new Exception("elasticsearch node health is not green");
  }
}

def checkBusinessDataIndex(port) {
  def response = sh (script: "curl http://localhost:$port/_cat/indices --user elastic:changeme", returnStdout: true)
  def elasticSearchIndex = "ivy.businessdata-test.testbusinessdata";  
  echo "elastic search response: $response"
  if (!response.contains(elasticSearchIndex)) {
    throw new Exception("could not find elastic search index $elasticSearchIndex in response $response");
  }
}
