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

            'ivy-environment-variables': { assertIvyIsNotRunningInDemoMode() },
            'ivy-logging': { assertIvyConsoleLog("ivy-logging", "Loaded configurations of 'file:/opt/ivy/configuration/ivy.yaml[prefixed: ivy.]'") },
            'ivy-openldap': { assertLogin("ldap", "rwei", "rwei") },
            'ivy-secrets': { assertIvyIsNotRunningInDemoMode() },
          ]

          // 'ivy-elasticsearch': { assert() },
          // 'ivy-visualvm': { assertJmxConnection() },

          examples.each { entry ->
            def example = entry.key;
            def assertion = entry.value;

            echo "START TESTING $example"
            try {
              dockerComposeUp(example)
              waitUntilIvyIsRunning()
              assertion.call()
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
    writeWarnLog('ivy is not running in demo mode')
  }
}

def assertIvyIsNotRunningInDemoMode() {
  def responseHtml = sh (script: "wget -qO- http://localhost:8080/ivy/info/index.jsp", returnStdout: true)
  if (responseHtml.contains('Demo Mode')) {      
    writeWarnLog('ivy is running in demo mode')
  }
}

def assertAppIsDeployed(applicationName) {
  def responseHtml = sh (script: "wget -qO- http://localhost:8080/ivy/wf/$applicationName/applicationHome", returnStdout: true)
  if (!responseHtml.contains("This is the home of the application $applicationName")) {
    writeWarnLog("application $applicationName is not deployed")
  }
}

def assertIvyConsoleLog(folder, message) {
  def log = sh (script: "docker-compose -f $folder/docker-compose.yml logs", returnStdout: true)
  if (!log.contains(message)) {
    writeWarnLog("console log of ivy does not contain $message")
  }
}

def assertLogin(application, user, password) {
  def htmlResponse = sh (script: "curl 'http://localhost:8080/ivy/wf/$application/login.jsp' --data 'username=$user&password=$password' -L -i -b cookie.txt", returnStdout: true)
  if (!htmlResponse.contains("Logged in as $user")) {
    writeWarnLog("could not login to application $application as $user")
  }
}

// def assertJmxConnection() {
//   wget 'http://crawler.archive.org/cmdline-jmxclient/cmdline-jmxclient-0.10.3.jar'
//   def mbean = "'ivy Engine:type=Application,name=System'";
//   def response = sh (script: "java -jar ./cmdline-jmxclient-0.10.3.jar admin:admin localhost:9003 $mbean", returnStdout: true)
//   if (!response.contains("system: True if the current application is the system  application, otherwise false (type=java.lang.Boolean)")) {
//     writeWarnLog("could not read $mbean via jmx")
//   }
// }

def writeWarnLog(message) {
  currentBuild.result = 'UNSTABLE'
  sh "echo $message >> warn.log"
}
