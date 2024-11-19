pipeline {
  agent any
  stages {
    stage('Build BackEnd') {
      parallel {
        stage('Backend Validate Phase') {
          steps {
            sh '''cd BackEnd/common
mvn install -Dmaven.java.home=/opt/java/jdk-21.0.4'''
            sh '''cd BackEnd
mvn clean -Dmaven.java.home=/opt/java/jdk-21.0.4'''
            echo 'Validate Phase'
            sh '''cd BackEnd
mvn validate -Dmaven.java.home=/opt/java/jdk-21.0.4'''
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

    stage('Backend Compile Phase') {
      parallel {
        stage('Backend Compile Phase') {
          steps {
            echo 'Compile Phase'
            sh 'cd BackEnd'
            sh 'mvn test-compile'
          }
        }

        stage('FrontEnd Compile Phase') {
          steps {
            echo 'Hello'
          }
        }

        stage('Mobile Compile Phase') {
          steps {
            sh 'Hello'
          }
        }

      }
    }

    stage('Backend Unit Test Phase') {
      steps {
        echo 'Unit Test Phase'
        sh 'cd BackEnd'
        sh 'mvn surefire:test'
      }
    }

    stage('Backend Integration Test Phase') {
      steps {
        echo 'Integration Test Phase'
        sh 'cd BackEnd'
        sh '''mvn failsafe:integration-test failsafe:verify
'''
      }
    }

    stage('Backend Package Phase') {
      steps {
        echo 'mvn package'
        sh 'cd BackEnd'
        sh 'mvn package'
      }
    }

    stage('Backend Quality Code Test Phase') {
      steps {
        echo 'Quality Code Test Phase'
        sh 'cd BackEnd'
        sh 'mvn validate sonar:sonar -e -Dsonar.projectKey=Habit-App  -Dsonar.projectName=\'Habit-App\'  -Dsonar.host.url=http://77.37.86.136:9000 -Dsonar.token=sqp_4df33d6a801906f9ffe3336d3dfa2cea823fcf0c'
      }
    }

    stage('Backend Deploy Phase') {
      steps {
        echo 'Deploy Phase'
        sh 'cd BackEnd'
        sh 'docker compose up'
      }
    }

  }
}