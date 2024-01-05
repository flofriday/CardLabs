# RabbitMQ

## Installation

1. Ensure [Docker](https://www.docker.com/) is installed
2. Pull the latest docker image using `docker pull bitnami/rabbitmq:latest`
3. Start the docker container `docker run --name rabbitmq bitnami/rabbitmq:latest`

## Run
1. Run the docker container `docker start rabbitmq`

# Bot

## Bot states

A brief explanation of how bot states work.

A bot default state is the state into which a bot is set after a match has been completed. Valid values are READY or
QUEUED.

When a bot is created, then the bot receives the state CREATED.
A new bot version can only be released when the bot state is CREATED or READY.
