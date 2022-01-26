pipeline {
    agent any

    stages {
        stage('Build & Test') {
            steps {
                echo 'Building the project with ${env.BUILD_NUMBER}'
                sh './gradlew clean build'
                archiveArtifacts 'build/libs/*.jar'
            }
        }

        stage('Docker Build & Update hub repository') {
            steps {
                script {
                    sh 'docker build -t luok377/sago .'
                    sh 'cat ~/docker_token.txt | docker login --username luok377 --password-stdin'
                    sh 'docker push luok377/sago'
                    sh 'docker rmi luok377/sago'
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    sh 'ssh -p 8080 root@101.101.161.9'
                    sh 'cd /sago_docker_container'
                    sh 'docker pull luok377/sago'
                }
            }
        }
    }
}