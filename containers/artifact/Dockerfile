FROM openjdk:8-jre

COPY io_log_ingestion_manager-1.0 /io_log_ingestion_manager-1.0

EXPOSE 80 443

CMD /io_log_ingestion_manager-1.0/bin/io_log_ingestion_manager -Dhttps.port=443 -Dhttp.port=80 -Dconfig.file=/io_log_ingestion_manager-1.0/conf/application-prod.conf