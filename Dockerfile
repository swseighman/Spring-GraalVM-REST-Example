FROM container-registry.oracle.com/java/openjdk:17-oraclelinux8
# FROM container-registry.oracle.com/os/oraclelinux:8-slim

EXPOSE 8080

# For Maven build
COPY maven/target/rest-service-demo-0.0.1-SNAPSHOT.jar app.jar
CMD ["java","-jar","app.jar"]

# For Native Image build
# COPY maven/target/rest-service-demo app
# RUN ["chmod", "755","/app"]
# ENTRYPOINT ["/app"]