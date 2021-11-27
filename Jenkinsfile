pipeline {
    agent any

    stages {
        stage('Build & Test') {
            steps {
                echo 'Building the project with ${env.BUILD_NUMBER}'
                sh './gradlew clean build'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}