# Teste Técnico - Back End - Attornatus

## Endpoints

| Description                | Method | URL                                       | Body                                                                                                   |
|:---------------------------|:------:|:------------------------------------------|:-------------------------------------------------------------------------------------------------------|
| Index people               |  GET   | /person                                   |                                                                                                        |
| Show person                |  GET   | /person/:personId                         |                                                                                                        |
| Create person              |  POST  | /person                                   | `{ "name": "eduardo", "birthDate": "2000-08-29" }`                                                     |
| Update person              |  PUT   | /person/:personId                         | `{ "name": "eduardo", "birthDate": "2000-08-29" }`                                                     |
| Delete person              | DELETE | /person/:personId                         |                                                                                                        |
| Index person addresses     |  GET   | /person/:personId/address                 |                                                                                                        |
| Create person address      |  POST  | /person/:personId/address                 | `{ "logradouro": "Avenida das Palmeiras", "cep": "87456321", "number": "547-C", "city": "São Paulo" }` |
| Set person main address    |  POST  | /person/:personId/address/:addressId/main |                                                                                                        |
| Remove person main address | DELETE | /person/:personId/address/main            |                                                                                                        |
| Update address             |  PUT   | /address/:addressId                       | `{"logradouro": "Avenida das Palmeiras", "cep": "87456321", "number": "547-C", "city": "São Paulo" }`  |
| Delete address             | DELETE | /address/:addressId                       |                                                                                                        |

> [Postman collection](/docs/attornatus.postman_collection.json)

--------------

## Texto base do teste técnico

Usando Spring boot, crie uma API simples para gerenciar Pessoas. Esta API deve permitir:

- Criar uma pessoa
- Editar uma pessoa
- Consultar uma pessoa
- Listar pessoas
- Criar endereço para pessoa
- Listar endereços da pessoa
- Poder informar qual endereço é o principal da pessoa

Uma Pessoa deve ter os seguintes campos:

- Nome
- Data de nascimento
- Endereço:
    - Logradouro
    - CEP
    - Número
    - Cidade

Requisitos:

- Todas as respostas da API devem ser JSON;
- Banco de dados H2
- Testes unitários
- Clean Code

Será levado em avaliação;

- Estrutura, arquitetura e organização do projeto;
- Boas práticas de programação;
- Alcance dos objetivos propostos.

📩 A entrega deverá ser feita da seguinte forma:

- Responder essa pergunta com o link do código público em sua conta do GitHub/Gitllab/Bitbucket.
- Opcionalmente, caso você consiga fazer o build da aplicação, poderá também informar o link de acesso