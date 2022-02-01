def remote = [:]
remote.name = 'sago_web_server'
remote.host = '101.101.161.9'
remote.port = 8080
remote.allowAnyHosts = true

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

        withCredentials([usernamePassword(credentialsId: 'ssh_key', passwordVariable: 'password', usernameVariable: 'userName')]) {
            remote.user = userName
            remote.password = password

            stage("Deploy") {
                steps{
                    script {
                        sshCommand remote: remote, command: 'cd /sago_docker_container'
                        sshCommand remote: remote, command: 'docker rmi luok377/sago'
                        sshCommand remote: remote, command: 'docker pull luok377/sago'
                    }
                }
            }
        }
    }
}