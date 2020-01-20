

#Assumptions
- Assumption is made that new data appears in a new file in given directory and will not be appended to the files already existing in the directory.
- It was not defined if Java Streams are to be used or Kafka streams. Both have been used.
- unit tests / mocks have not been created.

#Requirements
- java 8+
- maven
- kafka 
- git

#The Solution
The solution consists of 4 services:
1. Monitor - is responsible for checking the given directory every x seconds (x is configured in properties). If new files appear then it's content is read.
Receiving timestamp is generated. Kafka message with the trade object is sent (separate message for every trade).
2. StreamTransformer - small microservice using Kafka Streams. It reads incoming trade on input topic. It computes the amount field. It sends the trades to the output topic.
3. ProducerA , ProducerB - two microservices almost sharing codebase. Main differences in configuration. They read trades processed by StreamTransformer from topic every x seconds (300 for A and 60 for B).
They create json files with the data in output directory given in configuration. They belong to separate consumer group in order to have both of them process all messages.
ProducerA and ProducerB have been created as 2 services having in mind that they might be developed to serve different purposes (different transformations etc).
At the current requirement level it could be in fact 1 microservice invoked with 2 instances with different config each.

The task to get the messages in batches every x seconds is not recommended for Kafka which is live streaming platform rather than batch processing.  
 

#How to run the solution
1. Download the services:
- git clone https://github.com/barteklab/monitor.git
- git clone https://github.com/barteklab/StreamTransformer
- git clone https://github.com/barteklab/ProducerA
- git clone https://github.com/barteklab/ProducerB

2.  Set the properties in src/main/resources/*.properties files
3.  Create jar's for every service: mvn package
4. Run kafka zookeeper/broker
5.  Run the microservices (changing the JARNAME for the name of file generated in /target): 
-  java -cp JARNAME-with-dependencies.jar trades.ProducerA
-  java -cp JARNAME-with-dependencies.jar trades.ProducerB
- java -cp JARNAME-with-dependencies.jar stream.StreamTransformer
- java -cp JARNAME-with-dependencies.jar trades.Monitor