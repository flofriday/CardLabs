# CardScheme

Write the best card playing bots.

## Run with docker compose

```bash
docker compose up --build
```

The frontend will start on localhost:3000.

## Run locally 

If you want to run the services locally you will need a local rabbitmq instance.
The easiest way to get one up running is with:

```bash
docker run -it --rm --hostname my-rabbit --name some-rabbit -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

Next you can start all the servers, each in their own terminal session:
```bash
# Start the management
cd management
gradle bootRun --args='--spring.profiles.active=local'

# Start the gameserver
cd gameserver
./mvnw compile exec:java

# Start the frontend
cd frontend
npm run dev
```

The frontend will start on localhost:3000.

## Continuous deployment

The application is also deployed on the kubernetes cluster provided by the 
lecture at:

http://23ws-ase-pr-inso-04.apps.student.inso-w.at

Note that you need to be connected to the TU VPN for it to work.
