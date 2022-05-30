class Compose {
  
  private pipeline;
  private example
  
  def Compose(pipeline, example) {
    this.pipeline = pipeline;
    this.example = example
  }
  
  def runTest(def assertion) {
    pipeline.echo "==========================================================="
    pipeline.echo "START TESTING COMPOSE EXAMPLE $example"
    try {
      dockerComposeUp()
      waitUntilIvyIsRunning()
      def consoleLog = getIvyConsoleLog()
      assertion.call(consoleLog)
      assertNoErrorOrWarnInIvyLog(consoleLog)
    } catch (ex) {
      pipeline.echo ex.getMessage()
      pipeline.currentBuild.result = 'UNSTABLE'

      def log = "warn-compose-${example}.log"
      pipeline.sh "echo SAMPLE ${example} FAILED >> ${log}"              
      pipeline.sh "echo =========================================================== >> ${log}"
          
      pipeline.sh "echo \"Error Message: ${ex.getMessage()}\" >> ${log}"   
      pipeline.sh "echo =========================================================== >> ${log}"
          
      pipeline.sh "echo DOCKER-COMPOSE BUILD LOG: >> ${log}"  
      pipeline.sh "cat docker-compose-build.log >> ${log}"
      pipeline.sh "echo =========================================================== >> ${log}"

      pipeline.sh "echo DOCKER-COMPOSE UP LOG: >> ${log}"
      pipeline.sh "docker-compose -f ${example}/docker-compose.yml logs >> ${log}"
      throw ex
    } finally {
      pipeline.sh 'rm docker-compose-build.log'
      pipeline.echo getIvyConsoleLog()
      dockerComposeDown()
      pipeline.echo "==========================================================="
    }
  }

  def dockerComposeUp() {
    pipeline.sh "docker-compose -f $example/docker-compose.yml build >> docker-compose-build.log"
    pipeline.sh "docker-compose -f $example/docker-compose.yml up -d"
  }

  def dockerComposeDown() {
    pipeline.sh "docker-compose -f $example/docker-compose.yml down"
  }

  def waitUntilIvyIsRunning() {
    pipeline.timeout(2) {
      pipeline.waitUntil {
        def exitCode = pipeline.sh script: "docker-compose -f $example/docker-compose.yml exec -T ivy wget -t 1 -q http://localhost:8080/ -O /dev/null", returnStatus: true
        return exitCode == 0;
      }
    }
  }
  
  def getIvyConsoleLog() {
    return pipeline.sh (script: "docker-compose -f $example/docker-compose.yml logs ivy", returnStdout: true)
  }
  
  def assertNoErrorOrWarnInIvyLog(log) {
    if (log.contains("WARN") || log.contains("ERROR")) {
      throw new Exception("console log of ivy contains WARN/ERROR messages");
    }
  }
}

def Compose newCompose(example) {
  return new Compose(this, example)
}

return this