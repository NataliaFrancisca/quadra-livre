# 🤾🏽‍♀️❇️ QUADRA LIVRE

**quadra livre** é uma `API` desenvolvida para simplificar o gerenciamento de reserva de quadras esportivas. Com ela, os gestores dos espaços podem cadastrar suas quadras e gerar automaticamente horários de reservas para diferentes dias da semana.

## Stack:
- Java
- Spring (JPA, Security, Web)
- PostegreSQL
- Flyway
- Java JWT
- Lombok
- Swagger

## Funcionalidades:
1. **cadastro de usuário:** criação de usuário 2 tipos de usuários: _GESTOR_ (responsável pelo espaço) e _USUARIO_ (quem reserva o espaço.);
2. **cadastro de quadras:** permite que gestores registrem quadras e configurem data e horários de funcionamento.
3. **datas de indisponibilidade:** permite que o gestor indique datas em que a quadra vai estar indisponível.
4. **reserva:** os usuários realizam reservas para quadras em horários específicos que são gerados automaticamente.

## Como rodar essa aplicação na minha máquina?

### 1. Faça o clone do projeto:
abra o terminal e execute:
```bash
  git clone https://github.com/NataliaFrancisca/quadra-livre-v2
  cd quadralivre
```

### 2. Instalar dependências:
certifique-se de ter as seguintes ferramentas instaladas:
- JDK 17+
- Maven
- PostgreSQL

### 3. Configurar o banco de dados:
abra o terminal do PostgreSQL e execute o comando:
```bash
  CREATE DATABASE quadralivre
```
altere os dados no arquivo ``application.properties``:
```bash
  spring.datasource.url=jdbc:postgresql://localhost/quadralivre
  spring.datasource.username=${DB_USER}
  spring.datasource.password=${DB_PASSWORD}
  spring.datasource.driver-class-name=org.postgresql.Driver
  spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
  
  spring.jpa.hibernate.ddl-auto=update
```

### 4. Rode a aplicação:
#### Via IDE:
- abra o arquivo `QuadralivreApplication.java`
- clique no botão Run (disponível em IDEs como IntelliJ ou VS Code)

#### Via terminal:
- rode o comando: `mvn spring-boot:run`
