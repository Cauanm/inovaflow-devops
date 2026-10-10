# ==========================================
# ETAPA 1 - Build da aplicação
# ==========================================
FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copia primeiro o pom para aproveitar o cache das dependências
COPY pom.xml .

RUN mvn dependency:go-offline -B

# Copia o código-fonte
COPY src ./src

# Compila, executa os testes e gera o JAR
RUN mvn clean package -B


# ==========================================
# ETAPA 2 - Execução da aplicação
# ==========================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Cria usuário sem privilégios de root
RUN addgroup -S inovaflow && adduser -S inovaflow -G inovaflow

# Copia somente o JAR gerado na etapa de build
COPY --from=build /app/target/inovaflow-backend-0.0.1-SNAPSHOT.jar app.jar

# Porta padrão do Spring Boot
EXPOSE 8080

# Executa como usuário não-root
USER inovaflow

ENTRYPOINT ["java", "-jar", "app.jar"]