pipeline {
  agent any
  stages {
    stage('Hello') {
      post {
        always {
          githubChecksSend(checkName: 'Build', detailsURL: "${env.BUILD_URL}", status: 'completed', conclusion: 'success')
        }

      }
      steps {
        githubNotify(context: 'Build', status: 'PENDING', description: 'Build started')
        echo 'Hello from JenkinsFile'
        sh 'mvn --version'
        sh 'gradle --version'
      }
    }

  }
}