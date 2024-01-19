FROM eclipse-temurin:17.0.8.1_1-jdk-jammy
VOLUME /tmp

# Copie o código-fonte para o contêiner
COPY . .

# Conceda permissões de execução ao Maven Wrapper
RUN chmod +x mvnw

# Execute o Maven Wrapper para construir o projeto
RUN ./mvnw clean install -DskipTests

# Defina o comando de entrada (entrypoint) para executar o JAR
ENTRYPOINT ["java", "-jar", "target/desafio-backend-crdc-0.0.1-SNAPSHOT.jar"]
