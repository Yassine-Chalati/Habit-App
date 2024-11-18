pipeline {
  agent any
  stages {
    stage('Build BackEnd') {
      parallel {
        stage('Build BackEnd') {
          steps {
            sh 'cd BackEnd/common'
            sh '''mvn install
cd ..
cd ..'''
            sh 'cd BackEnd'
            sh 'mvn clean'
            echo 'Validate Phase'
            sh 'mvn validate'
            echo 'Compile Phase'
            sh 'mvn test-compile'
            echo 'Unit Test Phase'
            sh 'mvn surefire:test'
            echo 'Integration Test Phase'
            sh '''mvn failsafe:integration-test failsafe:verify
'''
            echo 'Package Phase'
            sh 'mvn package'
            echo 'Quality Code Test Phase'
            sh 'mvn validate sonar:sonar -e -Dsonar.projectKey=Habit-App  -Dsonar.projectName=\'Habit-App\'  -Dsonar.host.url=http://77.37.86.136:9000 -Dsonar.token=sqp_4df33d6a801906f9ffe3336d3dfa2cea823fcf0c'
            echo 'Deploy Phase'
          }
        }

        stage('Build FrontEnd') {
          steps {
            echo 'Hello'
          }
        }

        stage('Build Mobile') {
          steps {
            echo 'Hello'
          }
        }

      }
    }

  }
}