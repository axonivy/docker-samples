pipeline {
  agent {
    label 'docker-compose'
  }

  options {
    disableConcurrentBuilds()
  }

  triggers {
    cron '@midnight'
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
            'ivy-db-postgres': { assertIvyIsNotRunningInDemoMode() },
            'ivy-db-mysql': { assertIvyIsNotRunningInDemoMode() },
            'ivy-db-mariadb': { assertIvyIsNotRunningInDemoMode() },
            'ivy-db-mssql': { assertIvyIsNotRunningInDemoMode() },
            'ivy-deploy-app': { assertAppIsDeployed("app") },
            'ivy-elasticsearch': { assertBusinessData() },  
            'ivy-environment-variables': { assertIvyIsNotRunningInDemoMode() },
            'ivy-logging': { assertIvyConsoleLog("ivy-logging", "Loaded configurations of '/opt/ivy/configuration/ivy.yaml'") },
            'ivy-openldap': { assertLogin("ldap", "rwei", "rwei") },
            'ivy-patching': { assertPatching() },
            'ivy-secrets': { assertIvyIsNotRunningInDemoMode() },
            //'ivy-visualvm': { assertJmxConnection() },
          ]

          examples.each { entry ->
            def example = entry.key;
            def assertion = entry.value;

            echo "START TESTING $example"
            try {
              dockerComposeUp(example)
              waitUntilIvyIsRunning()
              assertion.call()
              assertNoErrorOrWarnInIvyLog(example)
            } finally {
              dockerComposeDown(example)
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

def dockerComposeUp(folder) {
  sh "docker-compose -f $folder/docker-compose.yml up -d"
}

def dockerComposeDown(folder) {
  sh "docker-compose -f $folder/docker-compose.yml down"
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
  def responseHtml = sh (script: "wget -qO- http://localhost:8080/ivy/info/index.jsp", returnStdout: true)
  if (!responseHtml.contains('Demo Mode')) {      
    writeWarnLog("ivy is not running in demo mode. responseHtml: $responseHtml")
  }
}

def assertIvyIsNotRunningInDemoMode() {
  def responseHtml = sh (script: "wget -qO- http://localhost:8080/ivy/info/index.jsp", returnStdout: true)
  if (responseHtml.contains('Demo Mode')) {      
    writeWarnLog("ivy is running in demo mode. responseHtml: $responseHtml")
  }
}

def assertAppIsDeployed(applicationName) {
  def responseHtml = sh (script: "wget -qO- http://localhost:8080/ivy/wf/$applicationName/applicationHome", returnStdout: true)
  if (!responseHtml.contains("This is the home of the application $applicationName")) {
    writeWarnLog("application $applicationName is not deployed. responseHtml: $responseHtml")
  }
}

def assertIvyConsoleLog(folder, message) {
  def log = sh (script: "docker-compose -f $folder/docker-compose.yml logs ivy", returnStdout: true)
  if (!log.contains(message)) {
    writeWarnLog("console log of ivy does not contain $message.")
    writeDockerLog(folder)
  }
}

def assertNoErrorOrWarnInIvyLog(folder) {
  def log = sh (script: "docker-compose -f $folder/docker-compose.yml logs ivy", returnStdout: true)
  if (log.contains("WARN")) {
    writeWarnLog("console log of ivy contains a warn")
    writeDockerLog(folder)
  }
  if (log.contains("ERROR")) {
    writeWarnLog("console log of ivy contains an error")
    writeDockerLog(folder)
  }
}

def assertLogin(application, user, password) {
  def htmlResponse = sh (script: "curl 'http://localhost:8080/ivy/wf/$application/login.jsp' --data 'username=$user&password=$password' -L -i -b cookie.txt", returnStdout: true)
  if (!htmlResponse.contains("Logged in as $user")) {
    writeWarnLog("could not login to application $application as $user. htmlResponse: $htmlResponse")
  }
}

def assertPatching() {
  def sample = "ivy-patching"
  assertIvyConsoleLog(sample, "Install patches for classes: ch.ivyteam.ivy.globalvars.GlobalVariableManager")
  assertIvyConsoleLog(sample, "This Global Variable has been patched for Demo Purpose")
}

def assertBusinessData() {
  // 1. Deploy Test Project
  sh "docker cp test.iar ivy-elasticsearch_ivy_1:/opt/ivy/deploy/test.zip"  
  sleep(5) // wait until is deployed

  // 2. Execute Process which create business data
  sh "curl 'http://localhost:8080/ivy/pro/test/test/1665799EBA281E4C/start.ivp'"

  // 3. Query Elastic Search
  def response = sh (script: "curl http://localhost:9200/_cat/indices --user elastic:changeme", returnStdout: true)
  def elasticSearchIndex = "ivy.businessdata-test.testbusinessdata";  
  echo "elastic search response: $response"
  if (!response.contains(elasticSearchIndex)) {
    writeWarnLog("could not find elastic search index $elasticSearchIndex in response $response")
  }
}

def writeWarnLog(message) {
  currentBuild.result = 'UNSTABLE'
  sh "echo \"$message\" >> warn.log"
}

def writeDockerLog(folder) {
  sh "docker-compose -f $folder/docker-compose.yml logs ivy >> warn.log"
}

def assertJmxConnection() {
  sh 'wget https://github.com/weissreto/jmx-cli/releases/download/v0.1.0/jmx-cli-0.1.0-linux.zip'
  sh "docker cp jmx-cli-0.1.0-linux.zip ivy-visualvm_ivy_1:/var/jmx-cli.zip"
  sh "docker exec ivy-visualvm_ivy_1 unzip /var/jmx-cli.zip"
  def stdout = sh (script: "docker exec -t ivy-visualvm_ivy_1 /opt/ivy/jmx-cli-0.1.0/bin/jmcli list beans", returnStdout: true)
  if (!stdout.contains("ivy Engine:type=Service,name=Page Engine System/AdminUI"))
  {
    writeWarnLog("stdout does not contain a mbean for admin")
  }
}