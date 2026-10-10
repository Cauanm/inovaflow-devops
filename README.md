# InovaFlow Backend

Backend desenvolvido para a Sprint 2 do projeto InovaFlow, uma plataforma de inovação corporativa para gestão de ideias, estratégias, projetos, indicadores e integração com IA.

## Tecnologias utilizadas

- Java
- Spring Boot
- Spring Security
- JWT
- MongoDB
- Maven
- Gemini API

## Funcionalidades

### Autenticação

- Cadastro de usuários
- Login com geração de token JWT
- Criptografia de senha com BCrypt
- Controle de acesso por perfil

Perfis disponíveis:

- OPERADOR
- GESTOR
- LIDERANCA

### Estratégias

- Liderança pode cadastrar, editar e excluir estratégias
- Todos os perfis autenticados podem consultar estratégias
- Cada estratégia possui categoria, campanha, status e data de criação

### Ideias de inovação

- Operadores podem cadastrar e consultar suas ideias
- Gestores podem consultar ideias recebidas
- Gestores podem aprovar, reprovar e priorizar ideias
- Ideias são vinculadas a uma estratégia

### Projetos e iniciativas

- Gestores podem cadastrar, editar e excluir projetos
- Gestores e liderança podem consultar projetos
- Projetos possuem investimento, retorno financeiro, lucro, ROI, prazo, etapa e status
- Projetos são vinculados a uma estratégia e podem ser vinculados a uma ideia aprovada

### Dashboard

- Liderança pode consultar indicadores gerais dos projetos
- O dashboard retorna:
    - total de projetos
    - investimento total
    - retorno total
    - lucro total
    - ROI médio
    - produtividade média
    - projetos por status

### Inteligência Artificial

O sistema possui integração com a Gemini API em duas funcionalidades:

1. Assistente IA  
   Permite que usuários autenticados conversem com a IA para receber sugestões sobre ideias, projetos, indicadores e apresentações.

2. Análise automática de ideias  
   Permite que gestores solicitem uma análise automática de uma ideia. A IA retorna:
    - pontuação
    - prioridade sugerida
    - justificativa

## Requisitos para execução

Antes de executar o backend, é necessário ter instalado:

- Java 21 ou superior
- Maven
- MongoDB Community Server
- MongoDB Compass, opcional
- IntelliJ IDEA ou outra IDE Java

## Configuração do MongoDB

O MongoDB deve estar rodando localmente na porta padrão:

```txt
localhost:27017
```

O banco utilizado pelo projeto é:

```txt
inovaflow_db
```

As collections são criadas automaticamente conforme os dados são inseridos no sistema.

Collections principais:

```txt
usuarios
estrategias
ideias
projetos
analises_ia
```

No arquivo `application.properties`, a configuração do MongoDB deve ficar assim:

```properties
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=inovaflow_db
```

## Configuração da chave da Gemini

Para utilizar as funcionalidades de Inteligência Artificial, é necessário configurar uma chave da Gemini API.

Por segurança, a chave não deve ser colocada diretamente no código-fonte. O projeto utiliza uma variável de ambiente chamada `GEMINI_API_KEY`.

No arquivo `application.properties`, a configuração deve ficar assim:

```properties
gemini.api.key=${GEMINI_API_KEY}
gemini.model=gemini-3.6-flash
```

No IntelliJ IDEA, a variável pode ser configurada em:

```txt
Run > Edit Configurations > Environment variables
```

Adicione a variável no seguinte formato:

```txt
GEMINI_API_KEY=sua_chave_da_gemini
```

Depois disso, basta salvar a configuração e executar o backend novamente.

