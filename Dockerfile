# ---- Build stage ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copia so o pom.xml primeiro para aproveitar o cache de dependencias do
# Docker: elas só sao baixadas de novo se o pom.xml mudar, nao a cada
# alteracao de codigo-fonte.
COPY pom.xml .
RUN mvn -q dependency:go-offline

COPY src ./src
RUN mvn -q clean package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/target/inclusao-digital-*.jar app.jar

EXPOSE 8080

# -XX:MaxRAMPercentage evita que a JVM reserve quase toda a RAM do
# container para o heap, sem deixar margem para metaspace/threads/
# buffers diretos. -XX:+UseSerialGC troca o coletor padrão (G1, que usa
# múltiplas threads) por um single-thread, mais adequado a ambientes com
# CPU bem limitada (ex.: plano free do Render, 0.1 vCPU) do que a
# concorrência do G1 competindo pela pouca CPU disponível.
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70.0", "-XX:+UseSerialGC", "-jar", "app.jar"]
