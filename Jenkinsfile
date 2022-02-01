pipeline {
    agent any

    environment {
        app = ''
    }

    stages {
        stage('Build & Test') {
            steps {
                sh './gradlew clean build'
                archiveArtifacts 'build/libs/*.jar'
            }
        }

        stage('Docker build image') {
            steps {
                script {
                    app = docker.build('luok377/sago', '.')
                }
            }
        }

        stage('Docker push image') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-account') {
                       app.push('latest')
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    sh 'ssh -t -t -p 8080 root@101.101.161.9'
                    sh 'cd /sago_docker_container'
                    sh 'docker rmi luok377/sago'
                    sh 'docker pull luok377/sago'
                }
            }
        }
    }
}