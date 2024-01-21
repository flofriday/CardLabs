# RabbitMQ

## Installation

1. Ensure [Docker](https://www.docker.com/) is installed
2. Pull the latest docker image using `docker pull bitnami/rabbitmq:latest`

## Run

1. Start the docker container `docker run -it --rm --name rabbitmq -p 5672:5672 bitnami/rabbitmq:latest`

# Bot

## IDs

A bot with a negative id is a test bot (maintained by the CardLabs team). A user bot must have a positive id.

## Bot states

A brief explanation of how bot states work.

A bot default state is the state into which a bot is set after a match has been completed. Valid values are READY or
QUEUED.

When a bot is created, then the bot receives the state CREATED.
A new bot version can only be released when the bot state is CREATED or READY.
