#!/bin/bash
docker start pg17.5
docker ps
mvn spring-boot:run -Dspring-boot.run.profiles=prod
