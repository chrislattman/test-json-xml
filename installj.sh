#!/bin/bash
curl -LO https://repo.maven.apache.org/maven2/org/json/json/20240303/json-20240303.jar
curl -LO https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-core/2.17.0/jackson-core-2.17.0.jar
curl -LO https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.17.0/jackson-annotations-2.17.0.jar
curl -LO https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.17.0/jackson-databind-2.17.0.jar
mkdir lib
mv ./*.jar lib
