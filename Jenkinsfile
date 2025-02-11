pipeline {
  agent any
  stages {
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
mvn validate sonar:sonar -e  -Dsonar.projectKey=HabitApp  -Dsonar.projectName=\'Habit-App\'  -Dsonar.host.url=http://77.37.86.136:9000  -Dsonar.token=sqp_24d7a14a618d9abc4d04b3fa5e1e7cb4812fa7d8'''
      }
    }

    stage('Deploy Phase') {
      when {
        branch 'main'
      }
      steps {
        echo 'Deploy Phase'
        sh '''cd BackEnd

docker stop grafana || true
docker rm grafana || true
docker rmi grafana/grafana || true

docker stop prometheus || true
docker rm prometheus || true
docker rmi prom/prometheus || true

docker stop progress-service || true
docker rm progress-service || true
docker rmi progress-service || true

docker stop habit-service || true
docker rm habit-service || true
docker rmi habit-service || true

docker stop profile-service || true
docker rm profile-service || true
docker rmi profile-service || true

docker stop authentication-service || true
docker rm authentication-service || true
docker rmi authentication-service || true

docker stop emailing-service || true
docker rm emailing-service || true
docker rmi emailing-service || true

docker stop gateway-service || true
docker rm gateway-service || true
docker rmi gateway-service || true

docker stop config-service || true
docker rm config-service || true
docker rmi config-service || true

docker stop mysql || true
docker rm mysql || true
docker rmi mysql:8.0 || true

docker stop registry-service || true
docker rm registry-service || true
docker rmi registry-service || true

docker compose up -d'''
        echo 'deplot equation diffirential frontEnd'
        sh '''cd FrontEnd
cd equation_diffirential

docker stop frontend-equation-diffirential || true
docker rm frontend-equation-diffirential || true
docker rmi frontend-equation-diffirential || true

docker build -t frontend-equation-diffirential .

docker run -d -p 4200:4200 --name frontend-equation-diffirential frontend-equation-diffirential'''
        echo 'deploy model AI'
        sh '''cd AiModel
cd SolveEquationDifferential

docker stop ai-model-equation-diffirential || true
docker rm ai-model-equation-diffirential || true
docker rmi ai-model-equation-diffirential || true

docker build -t ai-model-equation-diffirential .

docker run -d -p 5000:5000 --name ai-model-equation-diffirential ai-model-equation-diffirential'''
        sh '''cd FrontEnd
cd habit_app

docker stop frontend-habit-app || true
docker rm frontend-habit-app || true
docker rmi frontend-habit-app || true

docker build -t frontend-habit-app .

docker run -d -p 4201:4201 --name frontend-habit-app frontend-habit-app'''
      }
    }

  }
}