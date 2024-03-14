FROM maven:3.9.6-amazoncorretto-17-debian-bookworm AS build

COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y wget
RUN apt-get install -y zip \
    unzip

RUN wget http://dl.google.com/linux/chrome/deb/pool/main/g/google-chrome-stable/google-chrome-stable_121.0.6167.184-1_amd64.deb -O /chrome.deb
RUN apt-get install -y ./chrome.deb
RUN rm /chrome.deb

RUN wget https://storage.googleapis.com/chrome-for-testing-public/121.0.6167.184/linux64/chromedriver-linux64.zip -O /tmp/chromedriver.zip \
    && unzip /tmp/chromedriver.zip -d /usr/local/bin/ \
    && rm /tmp/chromedriver.zip
RUN chmod +x /usr/local/bin/chromedriver-linux64/chromedriver

COPY --from=build /home/app/target/StockPortfolio-1.0-SNAPSHOT.jar /usr/local/lib/stockportfolio.jar

RUN groupadd -r stockportfoliouser && useradd -r -g stockportfoliouser stockportfoliouser
RUN chown -R stockportfoliouser:stockportfoliouser /usr
USER stockportfoliouser
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/stockportfolio.jar"]