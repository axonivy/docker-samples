class Kubernetes {
  
  private pipeline;
  private example
  
  def Kubernetes(pipeline, example) {
    this.pipeline = pipeline;
    this.example = example
  }
  
  def runTest(def assertion) {
    try {
      if (! hasKubernetes()) {
	    return
	  }
      pipeline.echo "==========================================================="
      pipeline.echo "START TESTING KUBERNETES EXAMPLE $example"
//	    kustomize()
//      kubernetesApply()
//      waitUntilIvyIsRunning()
//      def consoleLog = getIvyConsoleLog()
//      assertion.call(consoleLog)
//      assertNoErrorOrWarnInIvyLog(consoleLog)
    } catch (ex) {
      pipeline.echo ex.getMessage()
      pipeline.currentBuild.result = 'UNSTABLE'

      def log = "warn-kubernetes-${example}.log"
      pipeline.sh "echo SAMPLE ${example} FAILED >> ${log}"              
      pipeline.sh "echo =========================================================== >> ${log}"
          
      pipeline.sh "echo \"Error Message: ${ex.getMessage()}\" >> ${log}"   
      pipeline.sh "echo =========================================================== >> ${log}"
          
//      pipeline.sh "echo KUBERNETES IVY-ENGINE POD LOG: >> ${log}"
//      pipeline.sh "kubectl logs -l app=ivy-engine --tail=-1 >> ${log}"
      throw ex
    } finally {
//      pipeline.echo getIvyConsoleLog()
//      kubernetesDelete()
      pipeline.echo "==========================================================="
    }
  }
  
  def hasKubernetes() {
    def exitCode = pipeline.sh script: "test -f $example/kubernetes/kustomization.yaml", returnStatus: true
    return exitCode == 0;
  }

  def kustomize() {
    pipeline.sh "kubectl kustomize --load-restrictor=\"LoadRestrictionsNone\" $example > $example/kubernetes/kubernetes.yaml"
  }

  def kubernetesApply() {
    pipeline.sh "kubectl apply -f $example/kubernetes/kubernetes.yaml"
  }

  def kubernetesDelete() {
    pipeline.sh "kubectl delete -f $example/kubernetes/kubernetes.yaml"
  }

  def waitUntilIvyIsRunning() {
    pipeline.timeout(2) {
      pipeline.waitUntil {
        def exitCode = pipeline.sh script: "kubectl exec deployment/ivy-engine -- wget -t 1 -q http://localhost:8080/ -O /dev/null", returnStatus: true
        return exitCode == 0;
      }
    }
  }
  
  def getIvyConsoleLog() {
    return pipeline.sh (script: "kubectl logs -l app=ivy-engine --tail=-1", returnStdout: true)
  }
  
  def assertNoErrorOrWarnInIvyLog(log) {
    if (log.contains("WARN") || log.contains("ERROR")) {
      throw new Exception("console log of ivy contains WARN/ERROR messages");
    }
  }
}

def Kubernetes newKubernetes(example) {
  return new Kubernetes(this, example)
}

return this