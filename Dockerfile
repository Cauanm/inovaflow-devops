# ==========================================
# ETAPA 1 - Build da aplicação
# ==========================================
FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copia primeiro o pom.xml para aproveitar cache de dependências
COPY pom.xml .

# Baixa as dependências do Maven
RUN mvn dependency:go-offline -B

# Copia o código-fonte
COPY src ./src

# Compila e gera o JAR
# Os testes já são executados separadamente pelo GitHub Actions
RUN mvn clean package -DskipTests -B


# ==========================================
# ETAPA 2 - Execução da aplicação
# ==========================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Cria usuário sem privilégios de root
RUN addgroup -S inovaflow && adduser -S inovaflow -G inovaflow

# Copia somente o JAR da etapa de build
COPY --from=build /app/target/inovaflow-backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

USER inovaflow

ENTRYPOINT ["java", "-jar", "app.jar"]