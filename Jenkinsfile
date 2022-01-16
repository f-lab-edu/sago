pipeline {
    agent any

    stages {
        stage('Build & Test') {
            steps {
                echo 'Building the project with ${env.BUILD_NUMBER}'
                sh './gradlew clean build'
                archiveArtifacts 'sago/target/*.jar'
            }
        }

        stage('Docker Build & Update hub repository') {
            steps {
                script {
                    sh 'docker build -t luok377/sago sago/.'
                    sh 'docker push luok377/sago'
                    sh 'docker rmi luok377/sago'
                }
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}