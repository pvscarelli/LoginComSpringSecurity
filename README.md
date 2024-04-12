# Aplicativo de Gerenciamento de Usuários
Este é um aplicativo web de gerenciamento de usuários desenvolvido usando Spring Boot e Docker.

## Instruções de Configuração

### Pré-requisitos
- Spring 3.2.0
- Docker
- Docker Compose
- JDK 17

### Configuração do Banco de Dados
1. Certifique-se de ter o docker compose rodando, e que sua conta esteja logada.
2. Abra o arquivo `docker-compose.yml` no diretório raiz do projeto e verifique a configuração do banco de dados.
3. Execute o comando `docker-compose up -d` no terminal para iniciar o banco de dados e o FlywayDB.

### Inicialização do Servidor Backend
1. Navegue até o arquivo `UserApplication.java`.
2. Execute o aplicativo clicando em "run" na sua IDE.
3. Se a porta 8080 não estiver disponível ele vai iniciar em outra porta.

### Acesso ao Aplicativo
1. Caso a porta do backend não seja a 8080 deve se mudar os fetchs dos arquivos .js no front para de acordo com a porta que ele está rodando.
2. Rode o arquivo `index.html` da pasta `front` na sua IDE.
3. Configure o CORS para a porta em que o front-end está sendo executado.
4. Rode o arquivo `index.html` novamente para acessar o aplicativo.
5. Faça o registro e, em seguida, o login para ver todos os usuários.

## Documentação da API

### Registro de Usuário
curl -X POST \
http://localhost:8080/register \
-H 'Content-Type: application/json' \
-d '{ "name": "exampleUser", "mail": "user@example.com", "login": "exampleUser", "password": "password123" }'

### Login de Usuário
curl -X POST \
http://localhost:8080/login \
-H 'Content-Type: application/json' \
-d '{ "login": "exampleUser", "password": "password123" }'

### Obter Usuário
curl -X GET http://localhost:8080/users/{id}
Escolha Token Bearer como meio de autenticação E Insira o token devolvido no JSON ao efetuar o método login

#### Obter Todos os Usuários
curl -X GET http://localhost:8080/api/users
Escolha Token Bearer como meio de autenticação E Insira o token devolvido no JSON ao efetuar o método login

### Atualizar Usuário
curl -X PUT
http://localhost:8080/api/users/{id}
-H 'Content-Type: application/json'
-d '{ "name": "exampleUser", "mail": "user@example.com", "login": "exampleUser", "password": "password123" }'
Escolha Token Bearer como meio de autenticação E Insira o token devolvido no JSON ao efetuar o método login

### Excluir Usuário
curl -X DELETE http://localhost:8080/deleteUser/{id}
Escolha Token Bearer como meio de autenticação E Insira o token devolvido no JSON ao efetuar o método login

### Excluir Todos os Usuários
curl -X DELETE http://localhost:8080/deleteAllUsers
Escolha Token Bearer como meio de autenticação E Insira o token devolvido no JSON ao efetuar o método login
