#!/bin/bash

# make docker volume
# docker volume create --name maven-repo

# gradle run parameter
docker run \
        --rm \
        -v maven-repo:/root/.m2 \
        -v "$PWD":/home/gradle/project \
        -w /home/gradle/project \
        gradle:jdk11-alpine \
        gradle $@  # all parameter
