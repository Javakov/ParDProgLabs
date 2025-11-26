FROM gradle:8.14-jdk21 AS builder
WORKDIR /app

COPY --chown=gradle:gradle . .
RUN gradle installDist --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /app/build/install/ParDProgLabs .

ENTRYPOINT ["./bin/ParDProgLabs"]

