FROM openjdk:11
LABEL description="Online Auction Platform, Sago"
EXPOSE 8080
COPY ./build/libs/sago-0.0.1-SNAPSHOT.jar /opt/sago-in-image.jar
WORKDIR /opt
ENTRYPOINT [ "java", "-jar", "sago-in-image.jar" ]