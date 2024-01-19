FROM gradle:8.5-jdk21

WORKDIR /app

COPY settings.gradle build.gradle ./
RUN gradle dependencies
COPY . .
RUN gradle build --console=plain

CMD ["gradle", "bootRun", "--args='--spring.profiles.active=local'"]