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
source environment-variable.sh

cd ..

cd config-service/scripts
chmod +x environment-variable.sh
source environment-variable.sh

cd ..
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
source environment-variable.sh

cd ..

cd config-service/scripts
chmod +x environment-variable.sh
source environment-variable.sh

cd ..
cd ..

mvn failsafe:integration-test failsafe:verify
'''
      }
    }

    stage('Backend Package Phase') {
      steps {
        echo 'mvn package'
        sh '''export JAVA_HOME=/opt/java/jdk-21.0.5

cd BackEnd/scripts
chmod +x environment-variable.sh
source environment-variable.sh

cd ..

cd config-service/scripts
chmod +x environment-variable.sh
source environment-variable.sh

cd ..

cd ..

mvn package'''
      }
    }

    stage('Backend Quality Code Test Phase') {
      steps {
        echo 'Quality Code Test Phase'
        sh '''export JAVA_HOME=/opt/java/jdk-21.0.5

cd BackEnd/scripts
chmod +x environment-variable.sh
source environment-variable.sh

cd ..

cd config-service/scripts
chmod +x environment-variable.sh
source environment-variable.sh

cd ..

cd ..
mvn validate sonar:sonar -Dsonar.projectKey=HabitApp  -Dsonar.projectName=\'Habit-App\'  -Dsonar.host.url=http://77.37.86.136:9000  -Dsonar.token=sqp_6b67b57bf6488b0719cea05713d7b3cea72e8be5 -Dsonar.qualitygate.wait=true'''
      }
    }

    stage('Backend Deploy Phase') {
      when {
        branch 'main'
      }
      steps {
        echo 'Deploy Phase'
        sh '''cd BackEnd
docker compose up -d'''
      }
    }

  }
}