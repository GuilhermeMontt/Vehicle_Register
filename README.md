# Registro de Veículos (Vehicle Register)

Este projeto é um sistema de registro de veículos desenvolvido com Spring Boot. Ele demonstra uma arquitetura simples e robusta, utilizando uma API REST para receber dados, Apache Kafka para processamento assíncrono de mensagens e PostgreSQL para persistência de dados.

## Funcionalidades

*   **Publicação de Veículos**: Envio de dados de um novo veículo através de um endpoint REST.
*   **Processamento Assíncrono**: Os veículos recebidos são enviados para um tópico Kafka para serem processados de forma assíncrona, desacoplando a escrita no banco de dados da requisição inicial.
*   **Persistência de Dados**: Um consumidor Kafka escuta o tópico, processa os dados do veículo e os salva em um banco de dados PostgreSQL.
*   **Consulta de Veículos**: Endpoint para listar todos os veículos cadastrados.
*   **Filtragem Avançada**: Endpoint para buscar veículos com base em critérios como placa, modelo, fabricante e ano.

## Tecnologias Utilizadas

*   **Backend**: Java 21, Spring Boot 3
*   **Persistência**: Spring Data JPA, PostgreSQL
*   **Mensageria**: Spring for Apache Kafka
*   **API**: Spring Web (REST)
*   **Validação**: Spring Boot Starter Validation (Jakarta Bean Validation)
*   **Build & Dependências**: Maven
*   **Testes**: JUnit 5, Mockito, Testcontainers (para testes de integração com PostgreSQL)
*   **Infraestrutura (Local)**: Docker, Docker Compose

## Pré-requisitos

Antes de começar, certifique-se de ter as seguintes ferramentas instaladas em sua máquina:

*   [Docker](https://www.docker.com/get-started)
*   [Docker Compose](https://docs.docker.com/compose/install/)

## Passo a Passo para Rodar o Projeto

### 1. Clone o Repositório (se ainda não o fez)

Abra seu terminal e clone o projeto para a sua máquina.

```bash
git clone <URL_DO_SEU_REPOSITORIO>
cd Vehicle-Register
```

### 2. Inicie a Infraestrutura com Docker Compose

O projeto inclui um arquivo `docker-compose.yml` que sobe os contêineres necessários (PostgreSQL e Kafka) para a aplicação funcionar.

Execute o seguinte comando na raiz do projeto:

```bash
# O comando -d (detached) executa os contêineres em segundo plano.
docker-compose up -d
```

Aguarde alguns instantes para que os serviços sejam iniciados completamente.

### 3. Execute a Aplicação Spring Boot

Com a infraestrutura rodando, você pode iniciar a aplicação Spring Boot usando o Maven.

```bash
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`.

## Endpoints da API

### Publicar um Veículo

*   **Método**: `POST`
*   **URL**: `/vehicles/publish`
*   **Corpo da Requisição (JSON)**:

    ```json
    {
      "plate": "XYZ-1234",
      "model": "Fusca",
      "manufacturer": "Volkswagen",
      "year": 1975
    }
    ```
*   **Resposta de Sucesso (202 Accepted)**: `Veículo recebido e enviado para processamento.`

### Listar Todos os Veículos

*   **Método**: `GET`
*   **URL**: `/vehicles`

### Filtrar Veículos

*   **Método**: `GET`
*   **URL**: `/vehicles/filter`
*   **Parâmetros de Query (opcionais)**:
    *   `plate` (string)
    *   `model` (string)
    *   `manufacturer` (string)
    *   `year` (int)
*   **Exemplo**: `http://localhost:8080/vehicles/filter?manufacturer=Volkswagen&year=1975`

## Executando os Testes

Para rodar a suíte de testes (unitários e de integração), execute o comando:

```bash
mvn test
```
