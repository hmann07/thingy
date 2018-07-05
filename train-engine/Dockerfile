FROM openjdk:8

WORKDIR /thingy

COPY ./ /thingy/

RUN ls -lh 

RUN unzip ./target/universal/akka-quickstart-scala-1.0.zip

RUN ls -lh

EXPOSE 9000

ENV NAME MONGODBIP

CMD ["bash", "./akka-quickstart-scala-1.0/bin/akka-quickstart-scala"]