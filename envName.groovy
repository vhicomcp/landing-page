#!/usr/bin/env groovy

def call(String branchName) {
  String env
  if (branchName == 'master'){
    env='ODIN'
    return env
  } else {
    return 0
  }
}