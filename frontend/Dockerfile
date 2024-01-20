FROM node:21-slim

#ARG MANAGEMENT_HOST
#ENV MANAGEMENT_HOST $MANAGEMENT_HOST

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build
CMD [ "npm", "start" ]

#CMD [ "npm", "run", "dev"]