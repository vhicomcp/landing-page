pipeline {
    
    agent any
        
    stages {
        stage('Build Docker Image') {
            when {
                branch 'master'
            }
            steps {
                sh 'docker build -t mcpidinfra/vhico:$BUILD_NUMBER .'
            }
        }
        stage('Sonarqube Analysis') {
            when {
                branch 'master'
            }
            steps {
                script {
                    scannerHome = tool 'sonarqube'
                }
                withSonarQubeEnv('sonarqube') {
                    sh "${scannerHome}/bin/sonar-scanner"
                }
            }
        }
        stage('Push Image to Docker hub') {
            when {
                branch 'master'
            }
            steps {
                withCredentials([string(credentialsId: 'docker_pwd', variable: 'dockerHubPwd')]) {
                    sh "docker login -u mcpidinfra -p ${dockerHubPwd}"
                }
                sh 'docker push mcpidinfra/vhico:$BUILD_NUMBER'
            }
        }
        stage('Deploy to Server PHP-02') {
            agent { node { label 'PHP-02' } }
            when {
                branch 'master'
            }
            steps {
                withCredentials([string(credentialsId: 'docker_pwd', variable: 'dockerHubPwd')]) {
                    sh "docker login -u mcpidinfra -p ${dockerHubPwd}"
                }
                checkout scm
                sh """
                sed -i 's/latest/$BUILD_NUMBER/g' docker-compose.yml
                docker-compose --project-name=blue up -d
                """
            }      
        }
        stage('Remove docker image last build Dev') {
            when {
                branch 'master'
            }
            steps {
                sh 'docker rmi mcpidinfra/vhico:$BUILD_NUMBER'
            }      
        }
        stage('Git') {
            steps {
                step([$class: 'WsCleanup'])
                checkout scm
            }
        }      
    }
    post { 
        success {  
             mail bcc: 'vhico@mcpayment', body: "<b>SUCCES BUILD</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> URL de build: ${env.BUILD_URL}", charset: 'UTF-8', from: 'ops.tech@mcpayment.co.id', cc: 'hendro.triyatmoko@mcpayment.co.id', mimeType: 'text/html', replyTo: '', subject: "SUCCESS CI: Project name -> ${env.JOB_NAME}", to: "vhico.putra@mcpayment.co.id";
            //  mail bcc: 'vhico.putra@mcpayment', body: "<b>SUCCES BUILD</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> URL de build: ${env.BUILD_URL}", charset: 'UTF-8', from: 'ops.tech@mcpayment.co.id', cc: 'hendro.triyatmoko@mcpayment.co.id', mimeType: 'text/html', replyTo: '', subject: "ERROR CI: Project name -> ${env.JOB_NAME}", to: "hendro.triyatmoko@mcpayment.co.id";  
        }
        failure {  
             mail bcc: 'vhico.putra@mcpayment', body: "<b>ERROR BUILD</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> URL de build: ${env.BUILD_URL}", charset: 'UTF-8', from: 'ops.tech@mcpayment.co.id', cc: 'hendro.triyatmoko@mcpayment.co.id', mimeType: 'text/html', replyTo: '', subject: "ERROR CI: Project name -> ${env.JOB_NAME}", to: "vhico.putra@mcpayment.co.id";
            //  mail bcc: 'vhico.putra@mcpayment', body: "<b>ERROR BUILD</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> URL de build: ${env.BUILD_URL}", charset: 'UTF-8', from: 'ops.tech@mcpayment.co.id', cc: 'hendro.triyatmoko@mcpayment.co.id', mimeType: 'text/html', replyTo: '', subject: "ERROR CI: Project name -> ${env.JOB_NAME}", to: "hendro.triyatmoko@mcpayment.co.id";  
        }    
    }  
}
