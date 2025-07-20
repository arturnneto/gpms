# GPMS - Microsserviço de Gerenciamento de Ordens de Venda
Este projeto foi desenvolvido com o intuito de gerenciar ordens de forma simples e rápida.

Para utilizá-lo, é necessario ter instalado o Docker, pois a aplicação será rodada dentro de um container em conjunto com o banco de dados MongoDB e o gerenciador de filas RabbitMQ.

É possível acessar a documentação da API através do SwaggerUI, pelo link http://localhost:8080/swagger-ui/index.html#

### Tecnologias utilizadas:
- Java JDK 17
- Spring
- RabbitMQ
- MongoDB
- Docker

Este projeto tem possibilidade de ser utilizado através de um Front End produzido com React e TypeScript, localizado em https://github.com/arturnneto/gpmsfe.

Ao rodar os dois projetos em conjunto, é necessário atualizar as portas de requisição da API para a porta em que o servidor backend estiver rodando. Por padrão o projeto usa a porta 8080. 

É necessário também atualizar a anotação @CrossOrigin na classe SaleOrderController para aceitar requisições CORS do servidor do frontend. 

## Como rodar o projeto:

No diretório do projeto, rode o seguintes comando:

### `docker compose up -d`
Este comando instalará as dependências necessárias do projeto, assim como iniciará um container por default na porta 8080 do seu dispositivo.
