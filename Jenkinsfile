pipeline {
    agent any

    stages {
        stage('Build & Test') {
            steps {
                sh './gradlew clean build'
                archiveArtifacts 'build/libs/*.jar'
            }
        }

        stage("Docker build image") {
            app = docker.build("luok377/sago")
        }

        stage('Docker push image') {
            steps {
                docker.withRegistry('https://registry.hub.docker.com', 'docker-account') {
                    app.push("${env.BUILD_NUMBER}")
                    app.push("latest")
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