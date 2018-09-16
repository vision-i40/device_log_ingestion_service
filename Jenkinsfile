pipeline {
  agent {
    docker {
      image 'gcr.io/rich-atom-211704/scala-sbt:scala-2.12-3'
    }
  }

  environment {
    PROJECT_ID = 'rich-atom-211704'
    GCLOUD_CREDENTIALS = credentials('c4acba287c49f871fc714f1952baa9a17f2c2658')
    DEFAULT_REGION = "us-central1-a"
    K8S_CLUSTER_NAME = 'microservices-default'
    K8S_POD_NAME = 'iologingestionmanager'
    CONTAINER_NAME = "io_log_ingestion_manager_10_$BUILD_NUMBER"
    CONTAINER_TAG = "io_log_ingestion_manager-1.0:$BUILD_NUMBER"
    CONTAINER_NETWORK = 'io_log_ingestion_manager_network'
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
      }
      post {
        success {
          archiveArtifacts 'target/scala-2.11/scoverage-report/**/*'
        }
      }
    }
    stage('Build Docker Container') {
      steps {
        sh 'sbt dist'
        sh '''
            unzip target/universal/io_log_ingestion_manager-1.0*.zip -d containers/artifact/
            cd containers/artifact/
            mv io_log_ingestion_manager-1.0* io_log_ingestion_manager-1.0
            docker build -t gcr.io/$PROJECT_ID/$CONTAINER_TAG .
        '''
      }
    }
    stage('Functional Tests') {
      steps {
        sh '''
            export RABBITMQ_IP=$(docker inspect $RABBITMQ_CONTAINER_NAME -f "{{ .NetworkSettings.Networks.$CONTAINER_NETWORK.IPAddress }}")
            export MONGODB_IP=$(docker inspect $MONGODB_CONTAINER_NAME -f "{{ .NetworkSettings.Networks.$CONTAINER_NETWORK.IPAddress }}")

            ./scripts/waitForConnection.sh $MONGODB_IP $MONGODB_CONTAINER_PORT
            ./scripts/waitForConnection.sh $RABBITMQ_IP $RABBITMQ_CONTAINER_PORT

            export IO_LOG_INGESTION_MANAGER_PORT=9000
            export MONGODB_DB="io_logs_functional"
            export MONGODB_URI="mongodb://${MONGODB_IP}:${MONGODB_CONTAINER_PORT}/${MONGODB_DB}"
            export RABBITMQ_PORT=5672
            export RABBITMQ_VIRTUAL_HOST="/"
            export RABBITMQ_USER="rabbitmq"
            export RABBITMQ_PASSWORD="rabbitmq"
            export RABBITMQ_QUEUE_NAME="ingestion_queue_functional"
            export RABBITMQ_ROUTING_KEY="io_log.ingestion"

            docker run -d --rm -p9000:9000 --network=$CONTAINER_NETWORK --name=$CONTAINER_NAME \
                   --env MONGODB_DB=$MONGODB_DB \
                   --env MONGODB_URI=$MONGODB_URI \
                   --env RABBITMQ_IP=$RABBITMQ_IP \
                   --env RABBITMQ_PORT=$RABBITMQ_PORT \
                   --env RABBITMQ_VIRTUAL_HOST=$RABBITMQ_VIRTUAL_HOST \
                   --env RABBITMQ_USER=$RABBITMQ_USER \
                   --env RABBITMQ_PASSWORD=$RABBITMQ_PASSWORD \
                   --env RABBITMQ_QUEUE_NAME=$RABBITMQ_QUEUE_NAME \
                   --env RABBITMQ_ROUTING_KEY=$RABBITMQ_ROUTING_KEY \
                   gcr.io/$PROJECT_ID/$CONTAINER_TAG

            export IO_LOG_INGESTION_MANAGER_HOST=$(docker inspect $CONTAINER_NAME -f "{{ .NetworkSettings.Networks.$CONTAINER_NETWORK.IPAddress }}")

            ./scripts/waitForConnection.sh $IO_LOG_INGESTION_MANAGER_HOST $IO_LOG_INGESTION_MANAGER_PORT

            env MONGODB_URI=$MONGODB_URI JAVA_OPTS="-Dconfig.resource=application-functional.conf" sbt cucumber
          '''
        sh ''
      }
      post {
        success {
          archiveArtifacts 'target/test-reports/**/*'
        }
      }
    }
    stage('Register Container Build') {
      steps {
        sh 'gcloud auth activate-service-account --key-file $GCLOUD_CREDENTIALS'
        sh 'gcloud auth configure-docker --quiet'
        sh 'docker push gcr.io/$PROJECT_ID/$CONTAINER_TAG'
      }
    }
    stage('Deploy to Production') {
      steps {
        sh 'gcloud auth activate-service-account --key-file $GCLOUD_CREDENTIALS'
        sh 'gcloud container clusters get-credentials $K8S_CLUSTER_NAME --region=$DEFAULT_REGION --project=$PROJECT_ID'
        sh 'kubectl set image deployments/$K8S_POD_NAME $K8S_POD_NAME=gcr.io/$PROJECT_ID/$CONTAINER_TAG'
      }
    }
  }

  post {
    always {
      sh 'docker network disconnect $CONTAINER_NETWORK $(head -1 /proc/self/cgroup|cut -d/ -f3) || true'
      sh 'docker stop $CONTAINER_NAME || true && docker rm $CONTAINER_NAME  || true'
      sh 'docker-compose down --rmi all'
      sh 'docker rmi $(docker images --filter="reference=$CONTAINER_TAG" --format "{{.ID}}" -q) || true'
    }
  }
}