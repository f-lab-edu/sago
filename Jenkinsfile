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
        stage('Build') {
            steps {
                sh './gradlew clean build -x test'
                archiveArtifacts 'build/libs/*.jar'
            }
        }

        stage('Unit Test') {
            steps {
                sh './gradlew test'
            }
        }

        stage('Integration Test') {
            steps {
                sh './gradlew integrationTest'
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

        stage("Deploy") {
            steps{
                script {
                    withCredentials([usernamePassword(credentialsId: 'ssh_key', passwordVariable: 'password', usernameVariable: 'userName')]) {
                        remote.user = userName
                        remote.password = password

                        sshCommand remote: remote, command: 'cd /sago_docker_container'
                        sshCommand remote: remote, command: 'docker pull luok377/sago'
                    }
                }
            }
        }
    }
}