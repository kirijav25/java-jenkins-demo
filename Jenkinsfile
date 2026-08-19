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
                sh 'mvn clean package -DossIndexAnalyzerEnabled=false'
            }
        }
        stage('Trivy Scan') {
    steps {
        sh '''
            export TMPDIR=/var/tmp/trivy
            trivy image java-jenkins-demo
        '''
    }
}

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
      stage('Dependency Check') {
    steps {
        sh '''
            dependency-check.sh \
              --project "java-jenkins-demo" \
              --scan . \
              --data /var/lib/dependency-check \
              --format HTML \
              --out dependency-check-report \
              --disableOssIndex \
              --failOnCVSS 7
        '''
    }
}
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh '''
                    mvn clean verify \
                    org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
                    -DossIndexAnalyzerEnabled=false \
                    -Dsonar.projectKey=java-jenkins-demo \
                    -Dsonar.projectName=java-jenkins-demo
                    '''
                }
            }
        }
stage('OWASP Security Scan') {
    steps {
        sh '''
            mvn org.owasp:dependency-check-maven:12.1.0:check \
            -DossIndexAnalyzerEnabled=false \
            -DossindexAnalyzerEnabled=false
        '''
    }
}
        stage('Docker Build') {
            steps {
                sh 'docker build -t java-jenkins-demo .'
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-credentials',
                    usernameVariable: 'DOCKER_USERNAME',
                    passwordVariable: 'DOCKER_PASSWORD'
                )]) {
                    sh '''
                        echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
                        docker tag java-jenkins-demo:latest $DOCKER_USERNAME/java-jenkins-demo:latest
                        docker push $DOCKER_USERNAME/java-jenkins-demo:latest
                        docker logout
                    '''
                }
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
