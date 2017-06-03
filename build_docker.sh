#!/bin/sh

sbt assembly
docker-compose build
