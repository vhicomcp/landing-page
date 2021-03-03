#!/usr/bin/env groovy

pipeline {
    
    agent any
        
    stages {
        stage('Build Docker Image') {
            when {
                branch 'master'
            }
            steps {
                sh 'docker build -t kovhico/landingpage:$BUILD_NUMBER .'
            }
        }
        stage('Push Image to Docker hub') {
            when {
                branch 'master'
            }
            steps {
                // withCredentials([string(credentialsId: 'docker_pwd', variable: 'dockerHubPwd')]) {
                //     sh "docker login -u mcpidinfra -p ${dockerHubPwd}"
                sh 'docker push kovhico/landingpage:$BUILD_NUMBER'
            }
        }
        stage('Deploy to Server') {
            agent { node { label 'worker' } }
            when {
                branch 'master'
            }
            steps {
                checkout scm
                sh """
                sed -i 's/latest/$BUILD_NUMBER/g' docker-compose.yml
                """
            }      
        }
        stage('Remove docker image last build') {
            when {
                branch 'master'
            }
            steps {
                sh 'docker rmi kovhico/landingpage:$BUILD_NUMBER'
            }      
        }
        stage('Git') {
            steps {
                step([$class: 'WsCleanup'])
                checkout scm
            }
        }
        // stage ('Slack notif') {
        //     steps {
        //         slackSend baseUrl: 'https://sre-3ln5441.slack.com/',
        //         channel: '#jenkins', 
        //         color: 'good', 
        //         failOnError: true, 
        //         message: 'Build Started: ${env.JOB_NAME} ${env.BRANCH_NAME} ${env.BUILD_NUMBER}', 
        //         teamDomain: 'sre-3ln5441', 
        //         tokenCredentialId: 'slack-arvy'
        //     }
        // }
    }      
    post {
      always {
        echo 'Cleaning up workspace'
        deleteDir()
        script {
          if (env.BRANCH_NAME == 'master') {
            def channel="jenkins"
            def cred="slack-arvy"
            slackNotifier(currentBuild.currentResult,channel,cred)
          } else {
            echo "No BRANCH specified!"
          }
        }
      }
    }
}