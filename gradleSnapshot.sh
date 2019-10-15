#!/usr/bin/env bash

gradle_release(){

    git config --global user.email "ubuntu@box176.localdomain"
    git config --global user.name "Ubuntu"

    sh gradlew artifactoryPublish
}

gradle_release