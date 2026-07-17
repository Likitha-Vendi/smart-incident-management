pipeline {

    agent any

    stages {

        stage('Checkout Code') {
            steps {
                echo 'Checking out source code from GitHub'
            }
        }

        stage('Build Backend') {
            steps {
                dir('backend') {
                    bat 'mvn clean install -DskipTests'
                }
            }
        }

        stage('Run Tests') {
            steps {
                dir('backend') {
                    bat 'mvn test'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                dir('backend') {
                    withSonarQubeEnv('SonarQube') {
                        bat 'mvn sonar:sonar'
                    }
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    bat 'npm install'
                    bat 'npm run build'
                }
            }
        }

        stage('Docker Build Backend') {
            steps {
                bat 'docker build -t vlikitha/incident-management-backend:latest backend'
            }
        }

        stage('Docker Build Frontend') {
            steps {
                bat 'docker build -t vlikitha/smart-incident-frontend:latest frontend'
            }
        }

        stage('Docker Login') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-credentials',
                    usernameVariable: 'DOCKER_USERNAME',
                    passwordVariable: 'DOCKER_PASSWORD'
                )]) {
                    bat 'docker login -u %DOCKER_USERNAME% -p %DOCKER_PASSWORD%'
                }
            }
        }

        stage('Docker Push') {
            steps {
                bat 'docker push vlikitha/incident-management-backend:latest'
                bat 'docker push vlikitha/smart-incident-frontend:latest'
            }
        }


        stage('Kubernetes Deploy') {
            steps {

                echo 'Deploying application to Kubernetes'

                bat 'kubectl apply -f kubernetes/backend-deployment.yaml'
                bat 'kubectl apply -f kubernetes/frontend-deployment.yaml'

                bat 'kubectl rollout status deployment/backend -n servicepulse'
                bat 'kubectl rollout status deployment/frontend -n servicepulse'

            }
        }


        stage('Kubernetes Verification') {
            steps {

                echo 'Checking Kubernetes pods'

                bat 'kubectl get pods -n servicepulse'

            }
        }

    }

}