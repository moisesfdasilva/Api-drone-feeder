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
  1. container docker db, com o banco de dados em MySQL; e
  2. container docker spring-boot-app, com a aplicação em Java 11 (Spring Boot).

D. Acessar as rotas da API a seguir em http://localhost:8080.

### POST /drone/new
A requisição contendo no body: "name", String possuindo somente letras maiúsculas e números, com 4 caracteres; "model", String com até 32 caracteres; e "capacityWeightInKg", Float.

```
{ 
	"name": "P306",
	"model": "DJI Phantom Pro 4",
	"capacityWeightInKg": 2.72
}
```

Retorna status 200 e body com uma instância do drone cadastrado.

```
{ 
	"id": 4,
	"name": "P306",
	"model": "DJI Phantom Pro 4",
	"capacityWeightInKg": 2.72
}
```
### GET /drone/all
Retorna status 200 e body com as instâncias de drones cadastrados.

```
[
	...,
	{ 
		"id": 4,
		"name": "P306",
		"model": "DJI Phantom Pro 4",
		"capacityWeightInKg": 2.72
	}
]
```
### GET /drone/{id}
A requisição contendo um id existente na rota retorna status 200 e body com uma instância do drone cadastrado.

```
{ 
	"id": 4,
	"name": "P306",
	"model": "DJI Phantom Pro 4",
	"capacityWeightInKg": 2.72
}
```
### DELETE /drone/delete/{id}
A requisição contendo um id existente na rota retorna status 200 e body com a mensagem que o drone foi deletado.

```
{ 
	"message": "Id 4 has been removed."
}
```
### PUT /drone/update/{id}
A requisição contendo um id existente na rota e no body: "name", String possuindo somente letras maiúsculas e números, com 4 caracteres; "model", String com até 32  caracteres; e "capacityWeightInKg", Float.

```
{ 
	"name": "M307",
	"model": "DJI Matrice 600 Pro",
	"capacityWeightInKg": 6.8
}
```
Retorna status 200 e body com uma instância do drone atualizado.

```
{ 
	"id": 4,
	"name": "M307",
	"model": "DJI Matrice 600 Pro",
	"capacityWeightInKg": 6.8
}
```

###POST /delivery/new
A requisição contendo no body: "receiverName", String com até 32 caracteres; "address", String com até 32 caracteres; "zipCode", String no formato padrão de CEP (12345-678); "latitude", no formato padrão de latitude; "longitude", String no formato padrão de longitude; e "weightInKg", Float.

```
{
    "receiverName": "Francisco Pereira Passos",
    "address": "Av. Rio Branco, 63, Centro, Rio de Janeiro, RJ.",
    "zipCode": "20090-004",
    "latitude": "-22.901163",
    "longitude": "-43.178857",
    "weightInKg": 3.5
}
```
Retorna status 200 e body com uma instância da entrega cadastrada.

```
{
    "id": 3,
    "receiverName": "Francisco Pereira Passos",
    "address": "Av. Rio Branco, 63, Centro, Rio de Janeiro, RJ.",
    "zipCode": "20090-004",
    "latitude": "-22.901163",
    "longitude": "-43.178857",
    "status": "TO_DELIVER",
    "weightInKg": 3.5,
    "videoName": "None"
}
```
###POST /delivery/{id}/uploadVideo
###GET /delivery/allVideos
###GET /delivery/video/{id}
###GET /delivery/all
###GET /delivery/{id}
###DELETE /delivery/delete/{id}
###PUT /delivery/update/{id}
###GET /delivery/{id}/downloadVideo
###DELETE /delivery/{id}/deleteVideo


## Contribuintes
|Nome|GitHub|
| -------- | -------- |
|Moisés Fernandes|https://github.com/moisesfdasilva|

## Contato
[GitHub: Repositório-Api-drone-feeder](https://github.com/moisesfdasilva/Api-drone-feeder)
