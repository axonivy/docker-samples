pipeline {
  agent {
      label 'docker-compose'
  }
  
  // This would be nicer solution, but I can't access the containers 
  // started with docker-compose within the build container (network)  
  //agent {
  //  docker {
  //    image 'tmaier/docker-compose'
  //   args '-u 0:0 -v /var/run/docker.sock:/var/run/docker.sock'
  //  }
  //}

  options {
      disableConcurrentBuilds()
  }

  triggers {
    cron '@midnight'
  }

  stages {
    stage('test') {
      steps {
        // start containers
        script {            
            sh 'docker-compose -f ivy/docker-compose.yml up -d'            
        }
        
        // waiting still ivy is online
        timeout(2) {
            waitUntil {
            script {
                def r = sh script: 'wget -q http://localhost:8080/ivy -O /dev/null', returnStatus: true
                return (r == 0);
            }
          }
        }

        // make my assertion
        script {
            def responseHtml = sh (script: "wget -qO- http://localhost:8080/ivy/info/index.jsp", returnStdout: true)
            if (!responseHtml.contains('Demo Mode'))
            {
                currentBuild.result = 'UNSTABLE'
                sh script: "echo 'ivy is not available in demo mode' >> warn.log"
            }
        }

        archiveArtifacts 'warn.log'       
      }
    }    
  }
  post {
    // cleanup containers
    always {
      sh 'docker-compose -f ivy/docker-compose.yml down'
    }
  }
}
