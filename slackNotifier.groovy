#!/usr/bin/env groovy

def call(String buildResult, String chan, String cred) {

  String jobName = "${env.JOB_NAME}".replace("%2F", "/")
  String buildNumber = "${env.BUILD_NUMBER}"
  String authorName = "${env.AUTHOR_NAME}"
  String buildURL = "${env.BUILD_URL}"
  String branchName = "${env.BRANCH_NAME}"

  String env = envName("${branchName}")

  if ( buildResult == "SUCCESS" ) {
    slackSend channel: "${chan}", tokenCredentialId: "${cred}", color: "good", message: "*${buildResult}*\n Job: *${jobName}* build no.${buildNumber} \n Environment: `${env}`\n by ${authorName}\n More info at: ${buildURL}"
  }
  else if( buildResult == "FAILURE" ) {
    slackSend channel: "${chan}", tokenCredentialId: "${cred}", color: "danger", message: "*${buildResult}:*\n Job *${jobName}* build no.${buildNumber} \n Environment: `${env}`\n by ${authorName}\n More info at: ${buildURL}"
  }
  else if( buildResult == "UNSTABLE" ) {
    slackSend channel: "${chan}", tokenCredentialId: "${cred}", color: "warning", message: "*${buildResult}:*\n Job *${jobName}* build no.${buildNumber} \n Environment: `${env}`\n by ${authorName}\n More info at: ${buildURL}"
  }
  else {
    slackSend channel: "${chan}", tokenCredentialId: "${cred}", color: "danger", message: "*${buildResult}:*\nJob *${jobName}* build no.${buildNumber}\n Result was unclear! \n Environment: `${env}`\n by ${authorName}\n More info at: ${buildURL}"
  }
}