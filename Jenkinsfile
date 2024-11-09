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
         always {
            githubChecksSend checkName: 'Build', detailsURL: "${env.BUILD_URL}", status: 'completed', conclusion: 'success'
        }
      }
    }
  }
}
