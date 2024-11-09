pipeline {
  agent any
  stages {
    stage('Hello') {
      steps {
        githubNotify context: 'Build', status: 'PENDING', description: 'Build started'
        echo 'Hello from JenkinsFile'
        sh 'mvn --vdsfj'
        sh 'gradle --version'
      }

      post {
        success {
          githubNotify context: 'Build', status: 'SUCCESS', description: 'Build succeeded'
        }
        failure {
          githubNotify context: 'Build', status: 'FAILURE', description: 'Build failed'
        }
      }
    }
  }
}