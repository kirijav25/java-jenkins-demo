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
