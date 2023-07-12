# Api-drone-feeder

## Sobre o Projeto
Aplicação desenvolvida para disponibilizar uma API com funcionalidades de criar, ler, modificar e deletar informações relativas a entregas de pacotes e encomendas realizadas por drones, incluindo upload e download de vídeos.

## Tecnologias e Ferramentas empregadas
Java 11;
Spring Boot;
JUnit 5;
Maven;
MySQL; e
Docker.

## Status do Projeto
Aplicação em funcionamento, incluindo os testes unitários.

## Acesso à Aplicação
### Antes da instalação
Fazer o Clone do repositório.

### Instalação e Execução
A. Entrar no diretório raiz da aplicação:

  $ cd Api-drone-feeder

B. Instalar a aplicação pelo Docker Compose, através do comando:
  
  $ docker-compose up -d

C. Serão iniciados os dois containers:
  1. container docker db, com o banco de dados em MongoDB; e
  2. container docker spring-boot-app, com a aplicação em Java 11 (Spring Boot).

D. Acessar a API, nas seguintes rotas: --------------------------------------------------> AJUSTAR AS ROTAS ABAIXO:
  1. Post, em http://localhost:3001/cars, contendo o body { "model": string, "year": number, "color": string, "status": boolean, "buyValue": number, "doorsQty": number, "seatsQty": number }, cadastra um novo carro.
  2. Get, em http://localhost:3001/cars, exibe a lista dos carros cadastrados.
  3. Get, em http://localhost:3001/cars/:id, exibe os dados do carro conforme o id especificado na rota.
  4. Put, em http://localhost:3001/cars/:id, contendo o body { "model": string, "year": number, "color": string, "status": boolean, "buyValue": number, "doorsQty": number, "seatsQty": number }, atualiza os dados de um carro cadastrado.
  5. Delete, em http://localhost:3001/cars/:id, deleta o cadastro do carro conforme o id especificado na rota.  
  6. Post, em http://localhost:3001/motorcycles, contendo o body { "model": string, "year": number, "color": string, "status": boolean, "buyValue": number, "category": string, "engineCapacity": number }, cadastra uma nova moto.
  7. Get, em http://localhost:3001/motorcycles, exibe a lista das motos cadastradas.
  8. Get, em http://localhost:3001/motorcycles/:id, exibe os dados da moto conforme o id especificado na rota.
  9. Put, em http://localhost:3001/motorcycles/:id, contendo o body { "model": string, "year": number, "color": string, "status": boolean, "buyValue": number, "category": string, "engineCapacity": number }, atualiza os dados de uma moto cadastrada.
  10. Delete, em http://localhost:3001/motorcycles/:id, deleta o cadastro de uma moto conforme o id especificado na rota.

## Contribuintes
|Nome|GitHub|
| -------- | -------- |
|Moisés Fernandes|https://github.com/moisesfdasilva|

## Contato
[GitHub: Repositório-Api-drone-feeder](https://github.com/moisesfdasilva/Api-drone-feeder)