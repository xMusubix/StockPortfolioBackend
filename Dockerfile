FROM maven:3.9.6-amazoncorretto-21-debian-bookworm AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:21-jdk-slim
COPY --from=build /home/app/target/stockportfolio-1.0-SNAPSHOT.jar /usr/local/lib/stockportfolio.jar

RUN groupadd -r stockportfoliouser && useradd -r -g stockportfoliouser stockportfoliouser
RUN chown -R stockportfoliouser:stockportfoliouser /usr
USER stockportfoliouser
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/stockportfolio.jar"]