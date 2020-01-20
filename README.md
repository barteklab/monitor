

#Assumptions
- Assumption is made that new data appears in a new file in given directory and will not be appended to the files already existing in the directory.

#Requirements
- java 8+
- maven
- kafka 
- git

#The Solution
The solution consists of 4 services

#How to run the solution
1. Download the services:
- git clone https://github.com/barteklab/monitor.git
- git clone https://github.com/barteklab/StreamTransformer
- git clone https://github.com/barteklab/ProducerA
- git clone https://github.com/barteklab/ProducerB

2.  Set the properties in src/main/resources/*.properties files
3.  Create jar's for every service: mvn package
4.  Run the microservices (changing the JARNAME for the name of file generated in /target): 
-  java cp JARNAME.jar trades.ProducerA
-  java cp JARNAME.jar trades.ProducerB
- java cp JARNAME.jar stream.StreamTransformer
- java cp JARNAME.jar trades.Monitor