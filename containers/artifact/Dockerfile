FROM openjdk:8-jre

COPY io_log_ingestion_manager-1.0 /io_log_ingestion_manager-1.0

EXPOSE 9000

CMD /io_log_ingestion_manager-1.0/bin/io_log_ingestion_manager -Dhttp.port=9000 -Dconfig.file=/io_log_ingestion_manager-1.0/conf/application-prod.conf