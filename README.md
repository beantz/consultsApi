# API rest Consults

#### Minha API feita em java foi desenvolvida com o objetivo de fornecer um ambiente de gerenciamento de pacientes, consultas e procedimentos a serem feitos em um ambiente clinico.

### Stack Tecnológica:
Java 17 | Spring Boot 3 | Spring Security | JUnit 5 (Testes Unitários/Integração) | MySQL 8 | Swagger/OpenAPI 3 | Docker

### Como usar

1 . Clone repositório `git clone https://github.com/beantz123/projeto_java`.

2 . No arquivo __application.properties__ configure seu banco de dados inserindo nome do banco, seu nome usuario e a senha.

    spring.datasource.url=jdbc:mysql://localhost:3306/nome_do_banco
    spring.datasource.username=usuario
    spring.datasource.password=senha
    
3 . A API vai ta disponivel em `http://localhost:8090`

4 . Acessar as rotas 

     - apenas rota Consultas não precisa de autenticação todas as outras irão precisar, então, no postman você vai em Authorization, clique em Auth Type, selecione Basic Auth e forneça o usuario e senha definidos no arquivo CustomBasicAuthFilter, apos isso só inserir a url.

     GET http://localhost:8090/<nome do controller>
     GET http://localhost:8090/<nome do controller>/<id_para_retornar_um_paciente>
     POST http://localhost:8090/<nome do controller>/<id_para_cadastrar>
     PUT http://localhost:8090/<nome do controller>/<id_para_atualizar>
     DELETE http://localhost:8090/<nome do controller>/<id_para_excluirr>
     
5 . Docker
    - Construir e rodar a imagem
        
        docker run -p 8090:8090 spring-boot-projeto_java:1.0
        docker build -t spring-boot-projeto_java:1.0 .

6 . Acessar e testar as rotas pelo Swagger
   - jogar essa url no na navegador http://localhost:8090/swagger-ui/index.html#/
    

     
