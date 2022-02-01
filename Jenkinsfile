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
                    def remote = [:]
                    remote.name = 'sago_webserver1'
                    remote.host = '101.101.161.9'
                    remote.user = '${SSH_SERVER_USERNAME}'
                    remote.port = 8080
                    remote.password = '${SSH_SERVER_PASSWORD}'
                    remote.allowAnyHosts = true
                    sshCommand remote: remote, command: 'sudo cd /sago_docker_container'
                    sshCommand remote: remote, command: 'sudo docker rmi luok377/sago'
                    sshCommand remote: remote, command: 'sudo docker pull luok377/sago'
                }
            }
        }
    }
}