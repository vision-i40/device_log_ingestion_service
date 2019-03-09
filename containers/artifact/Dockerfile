FROM openjdk:8-jre

COPY device_log_ingestion_service-1.0 /device_log_ingestion_service-1.0

EXPOSE 9000

CMD /device_log_ingestion_service-1.0/bin/device_log_ingestion_service -Dhttp.port=9000 -Dconfig.file=/device_log_ingestion_service-1.0/conf/application-prod.conf