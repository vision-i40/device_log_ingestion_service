pipeline {
  agent {
    docker {
      image 'gcr.io/rich-atom-211704/scala-sbt:scala-2.12-2'
    }

  }
  stages {
    stage('') {
      steps {
        sh 'sbt unit'
      }
    }
  }
  environment {
    CONTAINER_NAME = 'io_log_ingestion_manager_10_$BUILD_NUMBER'
    CONTAINER_TAG = 'io_log_ingestion_manager-1.0:$BUILD_NUMBER'
  }
}