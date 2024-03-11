FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar invoicemanager.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "invoicemanager.jar"]

