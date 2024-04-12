# Aplicativo de Gerenciamento de Usuários
Aplicativo web de gerenciamento de usuários desenvolvido usando Spring Boot, Spring Security e Docker.

## Instruções de Configuração

### Pré-requisitos
- Docker
- Docker Compose

### Inicialização do Servidor Backend
1. No terminal navegue até a pasta User/user
2. Execute o comando `docker-compose up` para iniciar a api.
3. Se a porta 8080 não estiver disponível ele vai iniciar em outra porta.

### Acesso ao Aplicativo
1. Caso a porta do backend não seja a 8080 deve se mudar os fetchs dos arquivos .js no front para de acordo com a porta que ele está rodando.
2. Rode o arquivo `index.html` da pasta `front` na sua IDE.
3. Configure o CORS para a porta em que o front-end está sendo executado.
4. Rode o arquivo `index.html` novamente para acessar o aplicativo.
5. Faça o registro e em seguida o login para ver todos os usuários.

## Documentação da API

### Registro de Usuário
curl -X POST http://localhost:8080/register -H 'Content-Type: application/json' -d '{ "name": "exampleUser", "mail": "user@example.com", "login": "exampleUser", "password": "password123" }'

### Login de Usuário e Obtenção do token
token=$(curl -X POST http://localhost:8080/login -H 'Content-Type: application/json' -d '{ "login": "exampleUser", "password": "password123" }' | jq -r '.token')

### Obter Todos os Usuários
curl -X GET http://localhost:8080/users -H "Authorization: Bearer $token" | jq '.'

### Obter Usuário
curl -X GET http://localhost:8080/users/<login do usuario retornado ao obter todos os usuarios> -H "Authorization: Bearer $token" | jq '.'

### Atualizar Usuário
curl -X PUT http://localhost:8080/editUser/<id do usuario a ser atualizado aqui>  -H 'Content-Type: application/json' -H "Authorization: Bearer $token" -d '{ "name": "exampleUserUpdated", "mail": "update@example.com", "login": "exampleUpdate", "password": "newPassword" }'

- Se você atualizar o usuário com que fez login precisara fazer o login denovo.

### Excluir Usuário
curl -X DELETE http://localhost:8080/deleteUser/<insira um id retornado pelo obter todos os usuarios aqui> -H "Authorization: Bearer $token"

### Excluir Todos os Usuários
curl -X DELETE http://localhost:8080/deleteAllUsers -H "Authorization: Bearer $token"

- Depois de excluir todos os usuários o token atual não funcionará mais
- Será necessário cadastrar outro usuário e usar o novo token para verificar que os usuários anteriores foram deletados
