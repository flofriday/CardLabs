# CardScheme

Write the best card playing bots.

## Run with docker compose

```bash
docker compose up --build
```

The frontend will start on localhost:3000.

to start rabbitmq

```
docker run -d -p 5672:5672 --name rabbitMQ -e RABBITMQ_DEFAULT_USER=guest -e RABBITMQ_DEFAULT_PASS=guest rabbitmq:3.12-alpine
```

## Continuous deployment

http://23ws-ase-pr-inso-04.apps.student.inso-w.at

Note that you need to be connected to the TU VPN for it to work.
