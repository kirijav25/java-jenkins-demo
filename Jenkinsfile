pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        node {
  stage('SCM') {
    checkout scm
  }
  stage('SonarQube Analysis') {
    def mvn = tool 'Default Maven';
    withSonarQubeEnv() {
      sh "${mvn}/bin/mvn clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=java-jenkins-demo -Dsonar.projectName='java-jenkins-demo'"
    }
  }
}

        stage('Docker Build') {
            steps {
                sh 'docker build -t java-jenkins-demo .'
            }
        }
        stage('Deploy') {
    steps {
        sh '''
        docker rm -f java-app || true
        docker run -d --name java-app -p 8080:8080 java-jenkins-demo
        '''
    }
}
    }
}
