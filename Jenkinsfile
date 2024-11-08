pipeline {
  agent any
  stages {
    stage('Hello') {
      steps {
        echo 'Hello from JenkinsFile'
        sh 'mvn --vdsfj'
        sh 'gradle --version'
      }
    }

  }
}