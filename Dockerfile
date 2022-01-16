FROM openjdk:11
LABEL description="Online Auction Platform, Sago"
EXPOSE 8080
COPY /sago/target/sago-0.0.1-SNAPSHOT.jar /opt/sago-image.jar
WORKDIR /opt
ENTRYPOINT [ "java", "-jar", "sago-image.jar" ]