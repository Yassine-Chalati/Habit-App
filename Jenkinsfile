pipeline {
  agent any
  stages {
    stage('Build BackEnd') {
      parallel {
        stage('Backend Validate Phase') {
          steps {
            sh '''cd BackEnd/common
export JAVA_HOME=/opt/java/jdk-21.0.5
mvn -v
mvn install'''
            sh '''cd BackEnd
export JAVA_HOME=/opt/java/jdk-21.0.5
mvn clean'''
            echo 'Validate Phase'
            sh '''cd BackEnd
export JAVA_HOME=/opt/java/jdk-21.0.5
mvn validate'''
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
      steps {
        echo 'Compile Phase'
        sh '''export JAVA_HOME=/opt/java/jdk-21.0.5
cd BackEnd
mvn test-compile'''
      }
    }

    stage('Backend Unit Test Phase') {
      steps {
        echo 'Unit Test Phase'
        sh '''export JAVA_HOME=/opt/java/jdk-21.0.5

cd BackEnd/scripts
chmod +x environment-variable.sh
./environment-variable.sh

export PORT_EUREKA="7002"
export PROFILES="dev"
export HOSTNAME_EUREKA="localhost"
export USERNAME_EUREKA="aPsDMJnpQD9dTdEx3qfgoPy9YcJfz8iridqunderTSRLXYSbKasTe8xPLrdpTkAdY7BdpedzRYTunder_id-habit-app-service-eureka"
export PASSWORD_EUREKA="LCwJSPdLDjYZPI57E5UPDBBdPsPKCaWUVz8fMYQYY7ZDCUhgnYHyMEtuI4bbCkSGvwjpjLph4KcBtNBXKBn8YX6udkZrQZ2FKShBDCC6ynyj463sXS9V4KS4HpC3yBDDX9SNXYbGPLJyznSP52zV8YD7xDySjCcHXa354MnPKd3Gs3AGBB6PQvEtUDSm4JdS"
export GIT_BRANCH="main"
export DNS="${HOSTNAME_EUREKA}:${PORT_EUREKA}"
export HTTP="http"
export BASIC_AUTH_EUREKA="${USERNAME_EUREKA}:${PASSWORD_EUREKA}@"

echo $PROFILES

cd ..
mvn surefire:test'''
      }
    }

    stage('Backend Integration Test Phase') {
      steps {
        echo 'Integration Test Phase'
        sh '''export JAVA_HOME=/opt/java/jdk-21.0.5

cd BackEnd/scripts
chmod +x environment-variable.sh
./environment-variable.sh

echo $PROFILES

cd ..
mvn failsafe:integration-test failsafe:verify
'''
      }
    }

    stage('Backend Package Phase') {
      steps {
        echo 'mvn package'
        sh '''export JAVA_HOME=/opt/java/jdk-21.0.5
cd BackEnd
mvn package'''
      }
    }

    stage('Backend Quality Code Test Phase') {
      steps {
        echo 'Quality Code Test Phase'
        sh '''export JAVA_HOME=/opt/java/jdk-21.0.5

cd BackEnd/scripts
chmod +x environment-variable.sh
./environment-variable.sh

cd ..
mvn validate sonar:sonar -e -Dsonar.projectKey=Habit-App  -Dsonar.projectName=\'Habit-App\'  -Dsonar.host.url=http://77.37.86.136:9000 -Dsonar.token=sqp_4df33d6a801906f9ffe3336d3dfa2cea823fcf0c'''
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