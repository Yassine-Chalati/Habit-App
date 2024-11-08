pipeline {
  agent any
  stages {
    stage('Hello') {
      steps {
        echo 'Hello from JenkinsFile'
        sh '''mvn --version
gradle --version
npm --version'''
      }
    }

  }
}