FROM azul/zulu-openjdk:22-latest
EXPOSE 8080
ADD /target/cashtrack-0.0.1-SNAPSHOT.jar cashtrack.jar
ENTRYPOINT ["java", "-jar", "cashtrack.jar"]
