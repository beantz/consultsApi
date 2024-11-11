# API rest Consults

#### Minha API feita em java foi desenvolvida com o objetivo de fornecer um ambiente de gerenciamento de pacientes, consultas e procedimentos a serem feitos em um ambiente clinico.

### Requisitos

- java 17
- MySQL
- Docker

### Como usar

1 . Clone repositório `git clone https://github.com/beantz123/projeto_java`.

2 . No arquivo __application.properties__ configure seu banco de dados inserindo nome do banco, seu nome usuario e a senha.

    ```
    spring.datasource.url=jdbc:mysql://localhost:3306/nome_do_banco
    spring.datasource.username=usuario
    spring.datasource.password=senha
    
3 . A API vai ta disponivel em `http://localhost:8090`

4 . Acessar as rotas que possuem ou não id

5 . Docker
    - Construir e rodar a imagem
        ``` docker run -p 8090:8090 spring-boot-projetojava:1.0
            docker build -t spring-boot-projetojava:1.0 .

     
    
