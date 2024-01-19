FROM node:21-slim

ARG MANAGMENT_HOST
ENV MANAGMENT_HOST $MANAGMENT_HOST

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build
CMD [ "npm", "start" ]

#CMD [ "npm", "run", "dev"]