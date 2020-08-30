# API Instagram Challenge

API feita com Spring Boot para um desafio, onde é necessário construir uma API com endpoints "similares" a rede social Instagram nas versões iniciais. 

## Rotas

| URL  | Tipo |  Description |
| ---- | ---- |---- |
|/ | GET | Rota raiz do projeto.
|/user | POST | Cria um usuário
|/user/{username} | GET | Busca um usuário com o username digitado na URL. 
|/user/{username} | DELETE | Remove um usuário com o username digitado na URL. 
|/user/{username} | PUT | Atualiza um usuário com o username digitado na URL. 
|/user/{username}/posts | GET | Busca os posts (paginados) de um usuário com o username digitado na URL. 
|/posts | POST | **Não implementado!!** Mais informações nas observações.
|/posts/{id} | GET | Busca um post pelo id.
|/posts/{id} | DELETE | Remove um post pelo id.
|/posts/{id} | PUT | Atualiza um post pelo id.
|/posts/{id}/comments | POST | Cria um comentário para um post.
|/posts/{id}/comments | GET | Busca os comentários (paginados) de um post.
|/posts/{id}/comments/{commentId} | PUT | Atualiza um comentário de um post.
|/posts/{id}/comments/{commentId} | DELETE | Remove um comentário de um post.
|/posts/{id}/likes | GET | Busca os likes (paginados) de um post.
|/posts/{id}/likes | PUT | Adiciona ou remove um like de um usuário para um post.

## Iniciar o projeto

**1 - Banco de dados**

Essa aplicação utiliza MySQL.

É possível configurar as informações do banco no arquivo `application.properties` ou inserindo o prefixo com configurações do banco ao executar o projeto no terminal (exemplo: executando arquivo `.jar` com prefixo `-D`, como `-Dserver.port=8080`).

**2 - Compilar e executar**

Abaixo apresento duas formas de iniciar o projeto.

<ins>Forma 1 - Plugin Spring Boot)</ins>

Executar os seguintes comandos no terminal:

```shell script
mvn clean verify
mvn spring-boot:run
```

<ins>Forma 2 - Executando o `.jar` gerado)</ins>

Executar os seguintes comandos no terminal:

```shell script
mvn clean verify
java -jar target/api_instagram-0.0.1-SNAPSHOT.jar
```

## Testes

Esta aplicação foi inteiramente feita com TDD.

Executar Testes de Unidade:

```shell script
mvn clean test
```

Executar Testes de Unidade e Testes de Integração:

```shell script
mvn clean verify
```

## Principais entidades

- `User`: usuário da aplicação
- `Post`: post de uma foto
- `PostComment`: comentário de um `Post`
- `PostLike`: like de um `Post` relacionado com um `User`
- `PostView`: visualização de `Post` que pode estar relacionada a um `User`
- `Notification`: seria equivalente a notificação de um usuário sobre um evento. Não foi implementado service e controller sobre essa entidade.

## Observações

- Não foi implementada autenticação. O objetivo era implementar autenticação com OAuth2, mas outras tarefas foram priorizadas antes.
- **A rota para criar um `Post` não foi implementada. Está faltando a lógica de converter a imagem recebida por requisição para um arquivo local (ou num CDN) e gerar a URL da foto.**
