FROM gradle:8.5-jdk21

WORKDIR /app

ENV GRADLE_OPTS="-Xmx1000m"

COPY settings.gradle build.gradle ./
RUN gradle dependencies
COPY . .
RUN gradle build --no-daemon --console=plain

CMD ["gradle", "bootRun", "--args='--spring.profiles.active=local'"]