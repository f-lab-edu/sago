pipeline {
    agent any

    stages {
        stage('Build & Test') {
            steps {
                sh './gradlew clean build'
                archiveArtifacts 'build/libs/*.jar'
            }
        }

        stage('Docker build & push image') {
            steps {
                script {
                    sh 'docker build -t luok377/sago .'
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-account') {
                       sh "docker push luok377/sago"
                    }
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