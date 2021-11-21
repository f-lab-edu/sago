pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building the project with ${env.BUILD_NUMBER}'
                sh './gradlew clean build'
            }
        }
        stage('Test') {
            steps {
                echo 'Checking all test scripts'
                sh './gradlew clean test'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}