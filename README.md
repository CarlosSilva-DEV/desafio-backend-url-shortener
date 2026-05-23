<h1 align="center" style="font-weight: bold">DESAFIO BACK-END BRASIL - ENCURTADOR DE URL
    <img src="https://fonts.gstatic.com/s/e/notoemoji/latest/1f587_fe0f/512.gif" alt="Link emoji" width="32" height="32">
</h1>

<p align="center">
  <a href="#visão-geral">Visão Geral</a> •
  <a href="#tecnologias-utilizadas">Tecnologias Utilizadas</a> •
  <a href="#técnicas-utilizadas">Técnicas Utilizadas</a> •
  <a href="#executando-com-docker-compose">Executando com Docker Compose</a> • 
  <br>
  <a href="#documentação-da-api">Documentação da API</a> •
  <a href="#testes-automatizados-com-maven-wrapper">Testes automatizados com Maven Wrapper</a> •
  <a href="#contribuir-para-o-projeto">Contribuir para o projeto</a>
</p>

<br>

<div align="center">
  <img src=https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white>
  <img src=https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white>
  <img src=https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white>
  <img src=https://img.shields.io/badge/apache%20maven-C71A36.svg?style=for-the-badge&logo=apachemaven&logoColor=white>
  <img src=https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white>
  <img src=https://img.shields.io/badge/postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white>
  <br>
  <img src=https://img.shields.io/badge/swagger-limegreen?style=for-the-badge&logo=swagger&logoColor=white>
  <img src=https://img.shields.io/badge/junit6-%23dc524a?style=for-the-badge&logo=junit5&logoColor=white>
  <img src=https://custom-icon-badges.demolab.com/badge/mockito-white.svg?style=for-the-badge&logo=mockito>
  <img src=https://custom-icon-badges.demolab.com/badge/assertj-grey.svg?style=for-the-badge&logo=assertj>
  <img src=https://custom-icon-badges.demolab.com/badge/testcontainers-%230c93a9.svg?style=for-the-badge&logo=testcontainers&logoColor=white>
  <img src=https://custom-icon-badges.demolab.com/badge/spring%20tools%20suite-darkgreen.svg?style=for-the-badge&logo=spring-tools-suite&logoColor=white>
</div>

<br>

## VISÃO GERAL

Esse projeto foi desenvolvido para resolver o [desafio proposto pela comunidade Back-End Brasil](https://github.com/backend-br/desafios/blob/master/url-shortener/PROBLEM.md) e consiste em uma aplicação web encurtadora de URLs. A aplicação foi desenvolvida com base nos requisitos definidos na proposta do desafio, utilizando **Java** com **Spring Boot** e **Redis** como banco de dados principal da aplicação. Além disso, o projeto conta com **testes unitários** e **testes de integração** usando **Testcontainers** e **Docker Compose** para containerização da aplicação e banco de dados em ambiente de desenvolvimento, desobrigando a instalação local do Redis.

<br>

## TECNOLOGIAS UTILIZADAS

### BACKEND
- **Java 21**.
- **Spring Boot 4.0.6**.
- **Spring Data Redis** (API de persistência de dados com Redis).
- **Spring Validation** (Validação de DTOs em Controller).
- **Maven** (Gerenciador de Dependências).
- **Redis Server** (Banco de dados NoSQL).
- **Redis CLI** (Interface em linha de comando para acesso ao banco de dados).
- **Docker Compose** (Containerização de aplicação e banco de dados em ambiente de desenvolvimento).
- **Postman** (Testes manuais de requisições HTTP em API).
- **Swagger** (Documentação de endpoints da API). 
- **JUnit 6** (Framework para criação de testes unitários automatizados).
- **Mockito** (Criação de bean mocks para dependências em testes unitários).
- **AssertJ** (Validações de resultados em testes unitários).
- **Testcontainers** (Containers temporários para testes de integração entre Repositories e Redis).

<br>

## TÉCNICAS UTILIZADAS

- Desenvolvimento com **Spring Tools Suite** (Eclipse).
- Persistência de dados em **bancos de dados chave-valor** (NoSQL).
- Políticas de vida útil de dados através de **Time To Live** (TTL).
- Padrão de Projeto **MVC** (Model-View-Controller).
- **API RESTful**.
- Tratamento de Exceções com GlobalExceptionHandler.
- Automatização de **testes unitários** automatizados.
- **Testes de integração** com containers temporários.
- **Data Transfer Objects** (DTO) com **Records**.

<br>

## EXECUTANDO COM DOCKER COMPOSE

### Pré-requisitos

- Instale o [Docker Engine](https://docs.docker.com/engine/install).
- Instale o [Docker Compose](https://docs.docker.com/compose/install).
- Instale um cliente HTTP como [Postman](https://www.postman.com/downloads/) ou [Insomnia](https://insomnia.rest/download).

### 1. Clonar repositório
Em seu computador, navegue até um diretório de sua preferência para armazenar o projeto. Execute um dos comandos abaixo para clonar o repositório remoto para seu computador: 

```bash
# Clonar utilizando URL do repositório remoto
git clone https://github.com/CarlosSilva-DEV/desafio-backend-url-shortener.git

# Clonar utilizando chave SSH
git clone git@github.com:CarlosSilva-DEV/desafio-backend-url-shortener.git
```

### 2. Orquestrar containers com Docker Compose
Após clonar o projeto, execute o comando abaixo para orquestração dos containers. Através do arquivo [docker-compose.yml](https://github.com/CarlosSilva-DEV/desafio-backend-url-shortener/blob/main/docker-compose.yml), o Docker cria uma instância para a aplicação (via [Dockerfile](https://github.com/CarlosSilva-DEV/desafio-backend-url-shortener/blob/main/Dockerfile)) e cria uma instância do Redis, inicializando ambos os containers dentro da mesma rede isolada. Aguarde a conclusão e a aplicação será inicializada:

```bash
# Orquestar containers localmente (Spring + Redis)
docker-compose up --build
```

> **ATENÇÃO**: Certifique-se de que o Docker está em execução antes de executar o comando dessa etapa.
> ```bash
> # Verifica se o serviço Docker está em execução
> sudo systemctl status docker
>
> # Inicia o serviço Docker
> sudo systemctl start docker
> ```

### 3. Interaja com a aplicação através de um cliente HTTP
Após a conclusão e inicialização da aplicação, você poderá enviar requisições para a API a partir de um cliente HTTP. Para mais informações, consulte a seção sobre [documentação da API](#documentação-da-api).

### Adicionais
- Enquanto os containers estiverem em execução, é possível acessar o banco de dados containerizado através do **Redis CLI** ou outra ferramenta como **Redis Insight**. Para acessar o container do Redis através do terminal, execute o seguinte comando:

```bash
# Executa o comando 'redis-cli' dentro do container 'url-shortener-redis', conectando ao banco
docker exec -it url-shortener-redis redis-cli
```

- Posteriormente, caso queira verificar ou excluir a orquestração de containers criada pelo Docker Compose, utilize os comandos abaixo:

```bash
# Verifica as instâncias de container criadas (em execução e desativadas)
docker-compose ps -a

# Exclui as instâncias de container criadas e o volume do banco de dados
docker-compose down -v
```

<br>

## DOCUMENTAÇÃO DA API 

Para interagir com a aplicação através de um cliente HTTP, siga as demonstrações abaixo de como realizar requisições nos principais endpoints da aplicação, respeitando o contrato determinado pela API. Para consultar a **documentação do Swagger**, [execute a aplicação](#executando-com-docker-compose) e acesse-a através do endereço: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html#/)

### POST /shorten-url
- Principal endpoint da aplicação, responsável por receber uma URL longa e encurtá-la. A URL curta retornada deverá ter entre 5-10 caracteres alfanuméricos.

> Sucesso: Deve retornar status 200, caso não exista nenhuma chave no Redis igual à URL curta gerada.
> 
> Falha: Deve retornar status 400, caso o corpo da requisição enviada seja inválido (e.g. campo vazio ou em formato diferente do protocolo HTTPS)

| Endpoint               | Descrição
|------------------------|-----------------------------------------------------
| <kbd>POST /shorten-url</kbd>     | Recebe uma URL longa e gera uma URL curta (5-10 caracteres) 

<br>

**ESTRUTURA PADRÃO PARA REQUISIÇÕES:**
```json
{
    "url": "https://google.com" // campo não pode ser vazio e deve conter 'https://'
}
```

**RESPOSTA ESPERADA (STATUS 200):**
```json
{
  "url": "http://localhost:8080/0NYtyFLQNJ" // URL curta retornada com sucesso
}
```

<br>

### GET /{request}
- Endpoint responsável por receber uma URL curta e redirecionar o usuário para o endereço apontado pela URL original. O corpo da requisição deve ser vazio, com a URL curta sendo fornecida no caminho URI (e.g. http://localhost:8080/0NYtyFLQNJ).

> Sucesso: Deve retornar status 302, caso a URL curta fornecida exista no Redis. Caso a requisição seja testada através de um navegador, o usuário será redirecionado à página original. Caso seja enviada através de um cliente HTTP, o corpo da resposta deverá conter o código-fonte da página HTML do endereço original.
> 
> Falha: Deve retornar 404, caso a URL curta fornecida não exista no Redis, lançando uma UrlNotFoundException.

| Endpoint               | Descrição
|------------------------|-----------------------------------------------------
| <kbd>GET /{request}</kbd>     | Recebe uma URL curta e redireciona o usuário para endereço original

<br>

**ESTRUTURA PADRÃO PARA REQUISIÇÕES:**
```json
// Requisição deve ser enviada sem corpo
```

**RESPOSTA ESPERADA (STATUS 302):**
```json
// Corpo da resposta deverá conter o código-fonte HTML do endereço original
```

<br>

## TESTES AUTOMATIZADOS COM MAVEN WRAPPER

Esse projeto conta com testes unitários automatizados com **JUnit 6** e testes de integração com **Testcontainers**. O código-fonte das classes relacionadas aos testes automatizados podem ser encontradas [aqui](https://github.com/CarlosSilva-DEV/desafio-backend-url-shortener/tree/main/src/test/java/com/carlossilvadev/desafio_backend_url_shortener).

> Para executar os testes unitários, você poderá utilizar o **Maven Wrapper** presente no projeto, desconsiderando a necessidade de instalar o Maven localmente.
> 
> Para executar os testes de integração, você deve [instalar o Docker](#pré-requisitos). Certifique-se de que o [serviço Docker está em execução](#2.-orquestrar-containers-com-docker-compose).
>
> Todos os logs gerados pela execução dos testes estarão no diretório `/target/surefire-reports` (Maven default).

```bash
# Executar todos os testes utilizando o Maven Wrapper
./mvnw test

# Executar testes de uma classe específica (e.g. UrlShortenerServiceTest)
./mvnw test -Dtest=nomeClasse

# Executar um teste específico de uma classe específica 
# (e.g. UrlShortenerServiceTest, shouldSuccessShortenUrl_whenNoKeyConflictExists)
./mvnw test -Dtest=nomeClasse#nomeMétodo
```

<br>

## CONTRIBUIR PARA O PROJETO

Caso queira contribuir de alguma forma para o projeto, sinta-se a vontade para seguir esses passos:

1. Clone esse repositório para a sua máquina utilizando o comando: `git clone git@github.com:CarlosSilva-DEV/desafio-backend-url-shortener.git`
2. Crie uma branch específica para promover suas alterações.
3. Abra um Pull Request neste repositório explicando sobre as alterações propostas. Em caso de alterações visuais da aplicação, anexe capturas de telas e aguarde a revisão.

### Documentações auxiliares:

[📝 Como criar um Pull Request?](https://www.atlassian.com/br/git/tutorials/making-a-pull-request)

[💾 Padrões de commits](https://gist.github.com/joshbuchea/6f47e86d2510bce28f8e7f42ae84c716)
