# InovaFlow Backend

Backend desenvolvido em Java Spring Boot para o projeto **InovaFlow**, uma plataforma de inovação corporativa voltada para gestão de ideias, estratégias, projetos, indicadores e integração com Inteligência Artificial.

O projeto foi evoluído com práticas de DevOps, incluindo containerização com Docker, orquestração com Docker Compose, pipeline CI/CD com GitHub Actions e deploy automatizado nos ambientes de staging e produção.

---

## Tecnologias utilizadas

### Backend

- Java 21
- Spring Boot
- Spring Security
- JWT
- Spring Data MongoDB
- Bean Validation
- Maven
- Jackson 3
- Gemini API

### Banco de dados

- MongoDB
- MongoDB Atlas
- MongoDB Compass

### DevOps

- Git
- GitHub
- GitHub Actions
- Docker
- Docker Compose
- Render

---

# Funcionalidades

## Autenticação

O sistema possui autenticação baseada em JWT.

Funcionalidades:

- Cadastro de usuários
- Login com geração de token JWT
- Criptografia de senha com BCrypt
- Controle de acesso por perfil

Perfis disponíveis:

- `OPERADOR`
- `GESTOR`
- `LIDERANCA`

---

## Estratégias

As estratégias são utilizadas para orientar as iniciativas cadastradas na plataforma.

Funcionalidades:

- Cadastro de estratégias
- Consulta
- Atualização
- Exclusão
- Definição de categoria
- Campanha
- Status

A gestão das estratégias é realizada pela liderança.

---

## Ideias

Os operadores podem cadastrar ideias de inovação vinculadas às estratégias da organização.

Funcionalidades:

- Cadastro de ideias
- Consulta das próprias ideias
- Edição
- Exclusão
- Aprovação
- Reprovação
- Priorização
- Vínculo com estratégias

Os gestores são responsáveis pela análise e priorização das ideias recebidas.

---

## Projetos

As ideias aprovadas podem ser utilizadas como base para novos projetos.

Funcionalidades:

- Cadastro de projetos
- Consulta
- Atualização
- Exclusão
- Controle de investimento
- Retorno financeiro
- Lucro
- ROI
- Produtividade
- Prazo
- Etapa
- Status
- Vínculo com estratégias e ideias

O ROI é calculado utilizando:

```text
ROI = ((Retorno - Investimento) / Investimento) * 100
```

---

## Dashboard

A liderança pode consultar indicadores consolidados dos projetos cadastrados na plataforma.

Entre os indicadores estão:

- Total de projetos
- Investimento total
- Retorno financeiro total
- Lucro total
- ROI médio
- Produtividade média
- Quantidade de projetos por status

O sistema também permite consultar indicadores relacionados a uma estratégia específica.

---

## Inteligência Artificial

O sistema possui integração com a **Gemini API**.

A Inteligência Artificial é utilizada para:

- Assistente de inovação
- Sugestões relacionadas a projetos
- Apoio na análise de ideias
- Pontuação automática de ideias
- Prioridade sugerida
- Justificativa da análise

As análises realizadas também podem ser armazenadas no MongoDB.

---

# Tratamento de erros

A aplicação possui tratamento global de exceções utilizando `@RestControllerAdvice`.

Os erros retornados pela API seguem um formato padronizado.

Exemplo:

```json
{
  "timestamp": "2026-10-12T15:30:00",
  "status": 404,
  "erro": "Not Found",
  "mensagem": "Ideia não encontrada.",
  "path": "/ideias/123",
  "campos": null
}
```

Principais códigos HTTP tratados:

| Código | Situação |
|---|---|
| 400 | Dados inválidos ou regra de negócio |
| 401 | Credenciais inválidas ou usuário não autenticado |
| 403 | Usuário sem permissão |
| 404 | Recurso não encontrado |
| 409 | Conflito de dados |
| 502 | Falha na integração com serviço externo |
| 503 | Banco de dados indisponível |
| 500 | Erro interno |

Também são utilizadas validações nos DTOs para evitar o envio de dados inválidos.

---

# Como executar localmente com Docker

## Pré-requisitos

É necessário ter instalado:

- Git
- Docker Desktop
- Docker Compose

Clone o repositório:

```bash
git clone https://github.com/Cauanm/inovaflow-devops.git
```

Entre na pasta do projeto:

```bash
cd inovaflow-devops
```

Crie um arquivo `.env` baseado no `.env.example`.

Exemplo:

```env
SERVER_PORT=8080

MONGO_ROOT_USERNAME=inovaflow_admin
MONGO_ROOT_PASSWORD=sua_senha

GEMINI_API_KEY=sua_chave_gemini
GEMINI_MODEL=gemini-3.6-flash
```

> O arquivo `.env` contém informações sensíveis e não deve ser enviado ao GitHub.

---

## Executando a aplicação

Para construir e iniciar os containers:

```bash
docker compose up --build -d
```

Para verificar os containers:

```bash
docker compose ps
```

Para visualizar os logs do backend:

```bash
docker compose logs backend
```

Para encerrar os containers:

```bash
docker compose down
```

A aplicação ficará disponível em:

```text
http://localhost:8080
```

Health check:

```text
http://localhost:8080/health
```

---

# Containerização

O projeto utiliza Docker para containerizar a aplicação.

O `Dockerfile` utiliza uma estratégia de **multi-stage build**.

Na primeira etapa:

- Maven realiza o build
- O código-fonte é compilado
- O arquivo `.jar` é gerado

Na segunda etapa:

- É utilizado apenas o Java Runtime Environment
- O arquivo `.jar` é copiado para a imagem final
- A aplicação é executada utilizando um usuário sem privilégios de root

---

# Docker Compose

O arquivo `docker-compose.yml` realiza a orquestração do ambiente local.

São utilizados dois serviços:

```text
inovaflow-backend
inovaflow-mongodb
```

O Docker Compose também configura:

- Rede Docker
- Volume persistente
- Variáveis de ambiente
- Health check do MongoDB
- Dependência entre backend e banco de dados

Rede utilizada:

```text
inovaflow-network
```

Volume utilizado pelo MongoDB:

```text
mongodb_data
```

---

# Health Check

O backend possui o endpoint:

```http
GET /health
```

Esse endpoint verifica:

- Status da aplicação
- Conexão com o MongoDB
- Banco de dados utilizado

Exemplo de resposta local:

```json
{
  "application": "InovaFlow Backend",
  "status": "UP",
  "mongodb": "UP",
  "database": "inovaflow_db"
}
```

---

# Pipeline CI/CD

Foi implementado um pipeline utilizando **GitHub Actions**.

Arquivo:

```text
.github/workflows/ci-cd.yml
```

O pipeline é executado automaticamente quando ocorre um `push` na branch `main`.

Também possui suporte a:

```text
pull_request
workflow_dispatch
```

Fluxo principal:

```text
Build e Testes
      ↓
Docker Build
      ↓
Deploy Staging
      ↓
Validação Staging
      ↓
Deploy Production
      ↓
Validação Production
```

---

## Build e testes

O pipeline configura o Java 21 e realiza a compilação da aplicação.

```bash
./mvnw clean compile
```

Depois são executados os testes automatizados:

```bash
./mvnw test
```

Caso o build ou os testes falhem, as etapas seguintes não são executadas.

---

## Docker Build

Após a conclusão dos testes, o pipeline valida o Docker Compose:

```bash
docker compose config
```

Em seguida é criada a imagem Docker da aplicação.

---

## Deploy Staging

O deploy de staging é realizado automaticamente no **Render**.

Tecnologias utilizadas:

- Render
- MongoDB Atlas

Banco utilizado:

```text
inovaflow_staging
```

Após o deploy, o GitHub Actions verifica automaticamente o endpoint `/health`.

O pipeline somente continua caso o ambiente de staging esteja funcionando corretamente.

---

## Deploy Production

O ambiente de produção também utiliza:

- Render
- MongoDB Atlas

Banco utilizado:

```text
inovaflow_production
```

O deploy de produção somente é executado depois que o ambiente de staging é validado com sucesso.

Após o deploy, o pipeline também verifica o endpoint `/health` do ambiente de produção.

Dessa forma, uma falha no staging impede que uma nova versão seja implantada em produção.

---

# Variáveis de ambiente

O projeto utiliza variáveis de ambiente para evitar o armazenamento de informações sensíveis diretamente no código-fonte.

Principais variáveis:

```text
MONGODB_URI
GEMINI_API_KEY
GEMINI_MODEL
SERVER_PORT
PORT
```

Configuração utilizada no `application.properties`:

```properties
spring.application.name=inovaflow-backend

server.port=${PORT:${SERVER_PORT:8080}}

spring.mongodb.uri=${MONGODB_URI:mongodb://localhost:27017/inovaflow_db}

gemini.api.key=${GEMINI_API_KEY:}
gemini.model=${GEMINI_MODEL:gemini-3.6-flash}
```

---

# Segurança

Entre as práticas utilizadas estão:

- Autenticação JWT
- Criptografia de senhas com BCrypt
- Controle de acesso baseado em perfil
- Aplicação stateless
- Secrets fora do código-fonte
- Arquivo `.env` ignorado pelo Git
- Variáveis sensíveis configuradas no Render
- Deploy Hooks armazenados como GitHub Secrets
- Container executado sem privilégios de root
- Tratamento global de exceções
- Validação de DTOs

---

# Evidências

As evidências de execução estão presentes na documentação técnica entregue junto ao projeto.

Entre elas:

- Build da imagem Docker
- Containers em execução
- MongoDB saudável
- Health check local
- Pipeline CI/CD
- Deploy em staging
- Deploy em produção
- Testes automatizados

---

# Repositório

```text
https://github.com/Cauanm/inovaflow-devops
```

---

# Considerações finais

O projeto InovaFlow foi evoluído com práticas de DevOps, incluindo containerização, orquestração, integração contínua e entrega contínua.

A aplicação possui um fluxo automatizado:

```text
Build
↓
Testes
↓
Docker Build
↓
Deploy Staging
↓
Validação
↓
Deploy Produção
↓
Validação
```

Com isso, o processo de compilação, testes e publicação da aplicação é automatizado e validado antes que uma nova versão chegue ao ambiente de produção.