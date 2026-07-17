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

    }

}