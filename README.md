### **Estrutura de Pastas Explicada**

```
src/
├── main/
│   ├── java/
│   │   └── edu/fatec/Porygon/
│   │       ├── controller/             
│   │       ├── service/                
│   │       ├── repository/             
│   │       ├── model/                            
│   ├── resources/
│   │   ├── static/                      
│   │   │   ├── css/                    # Arquivos CSS
│   │   │   ├── js/                     # Arquivos JavaScript
│   │   │   └── images/                 # Imagens
│   │   └── templates/                  
│   │       └── fragments/              # telas html reutilizáveis de Thymeleaf
│   └── application.properties       
└── test/                                                              
```

- **`controller/` (Controladores)**
  - **Responsabilidade**: Esta pasta contém as classes responsáveis por lidar com as requisições HTTP que chegam à aplicação. Em outras palavras, os controladores expõem os endpoints da API. 
  - **Função**: O controlador recebe uma requisição, chama o serviço apropriado para processar a lógica de negócio, e então retorna a resposta ao cliente.

- **`service/` (Serviços)**
  - **Responsabilidade**: Esta pasta contém a lógica de negócio da aplicação. Aqui você implementa o que realmente acontece quando um controlador faz uma solicitação.
  - **Função**: O serviço pode chamar repositórios para acessar o banco de dados, aplicar regras de negócio, realizar validações, entre outros. 

- **`repository/` (Repositórios)**
  - **Responsabilidade**: Esta pasta contém as interfaces que serão usadas para acessar e manipular os dados no banco de dados. 
  - **Função**: O Spring Data JPA cuida da implementação das operações CRUD (Create, Read, Update, Delete) e de consultas customizadas a partir das interfaces definidas aqui.

- **`model/` (Modelos)**
  - **Responsabilidade**: Esta pasta contém as classes que representam as tabelas no banco de dados. Cada entidade é mapeada para uma tabela específica do banco.
  - **Função**: As entidades possuem atributos que correspondem às colunas da tabela e são utilizadas para armazenar e manipular os dados recuperados do banco de dados.

#### **2. `src/main/resources/`**
Essa pasta contém recursos não compilados que sua aplicação vai precisar em tempo de execução.Aqui fica o front, css, javascripts.

- **`application.properties`**
  - **Responsabilidade**: Este arquivo contém as configurações da aplicação, como detalhes de conexão com o banco de dados, configurações de logging, variáveis de ambiente, entre outras.


#### **3. `src/test/`**
Esta pasta é destinada aos testes da aplicação.
- **Responsabilidade**: Aqui é onde você escreverá os testes unitários e de integração para o seu código.

