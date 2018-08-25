pipeline {
  agent {
    docker {
      image 'gcr.io/rich-atom-211704/scala-sbt:scala-2.12-2'
    }
  }

  environment {
    CONTAINER_NAME = 'io_log_ingestion_manager_10_$BUILD_NUMBER'
    CONTAINER_TAG = 'io_log_ingestion_manager-1.0:$BUILD_NUMBER'
    CONTAINER_NETWORK = 'io_log_ingestion_manager'
    RABBITMQ_CONTAINER_NAME = 'io_log_ingestion_rabbitmq'
    RABBITMQ_CONTAINER_PORT = '5672'
    MONGODB_CONTAINER_NAME = 'io_log_ingestion_mongo'
    MONGODB_CONTAINER_PORT = '27017'
  }

  stages {
    stage('Unit tests') {
      steps {
        sh 'sbt unit'
      }
    }
    stage('Run Dependencies') {
      steps {
        sh 'docker-compose up -d'
        sh 'docker network connect $CONTAINER_NETWORK  $(head -1 /proc/self/cgroup|cut -d/ -f3)'
        sh '''
            export RABBITMQ_IP=$(docker inspect $RABBITMQ_CONTAINER_NAME -f "{{ .NetworkSettings.Networks.$CONTAINER_NETWORK.IPAddress }}")
            ./scripts/waitForConnection.sh $RABBITMQ_IP $RABBITMQ_CONTAINER_PORT
        '''
        sh '''
            export MONGODB_IP=$(docker inspect $MONGODB_CONTAINER_NAME -f "{{ .NetworkSettings.Networks.$CONTAINER_NETWORK.IPAddress }}")
            ./scripts/waitForConnection.sh $MONGODB_IP $MONGODB_CONTAINER_PORT
        '''
      }
    }
    stage('Integration and Coverage Tests') {
      steps {
        sh '''
            export RABBITMQ_IP=$(docker inspect $RABBITMQ_CONTAINER_NAME -f "{{ .NetworkSettings.Networks.$CONTAINER_NETWORK.IPAddress }}")
            export MONGODB_IP=$(docker inspect $MONGODB_CONTAINER_NAME -f "{{ .NetworkSettings.Networks.$CONTAINER_NETWORK.IPAddress }}")
            sbt coverage unit integration
          '''
        sh 'sbt coverageReport'
        post {
          success {
            archiveArtifacts 'target/scala-2.11/scoverage-report/**/*'
          }
        }
      }
    }
  }

  post {
      always {
        sh 'docker network disconnect $CONTAINER_NETWORK $(head -1 /proc/self/cgroup|cut -d/ -f3) || true'
        sh 'docker-compose down --rmi all'
      }
    }
}