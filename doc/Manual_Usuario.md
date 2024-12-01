![API 2º SEMESTRE EM BANCO DE DADOS (4)](https://github.com/user-attachments/assets/3571af67-2302-4e3a-be66-c63206ce7b5c)

<p align="center">
  <a href ="#requisitos"> REQUISITOS </a>  •
  <a href ="#configuração-do-banco-de-dados"> CONFIGURAÇÃO DO BANCO DE DADOS </a>  •
  <a href ="#instalaçao"> INSTALAÇÃO </a>  •
  <a href ="#cadastro-de-tag"> CADASTRO DE TAG </a>  •
  <a href ="#cadastro-de-portal"> CADASTRO DE PORTAL </a>  •
  <a href ="#cadastro-de-seletores"> CADASTRO DE SELETORES </a>  •
  <a href ="#cadastro-de-api"> CADASTRO DE API </a>  •
  <a href="#inspeção-da-página"> INSPEÇÃO DA PÁGINA </a> •
  <a href="#exemplos-de-cadastro"> EXEMPLOS DE CADASTRO </a>
</p>

# REQUISITOS

- **Java 22** ou superior
- **Spring Boot 3.3.3**
- **Maven** para gerenciamento de dependências
- **MySQL** configurado e rodando

## Configuração do Banco de Dados

1. Certifique-se de que o MySQL está instalado e em execução.

2. Crie um banco de dados chamado **porygon**.

`create database porygon;`

3. Configure o arquivo `application.properties` da aplicação para incluir as informações de conexão com o banco de dados:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/porygon
    spring.datasource.username=seu_usuario
    spring.datasource.password=sua_senha
    spring.jpa.hibernate.ddl-auto=update
    ```

## Instalação

1. Clone o repositório para o seu ambiente local:

    ```bash
    git clone https://github.com/PorygonAPI/Porygon2.git
    ```

2. Navegue até o diretório do projeto:

    ```bash
    cd PorygonAPI/Porygon2
    ```

3. Execute o comando Maven para construir o projeto:

    ```bash
    mvn clean install
    ```

# CADASTRO DE TAG

### Para começar a utilizar a aplicação, é necessário o cadastro de algumas tags. Elas serão responsáveis por auxiliar no direcionamento de notícias e APIs relacionadas a busca feita pelo usuário.

> O cadastramento da tag não precisa seguir um padrão de letras maiúsculas e minúsculas, o próprio sistema faz a padronização antes de armazenar no banco.

![image](https://github.com/user-attachments/assets/20152f41-79b8-4f6e-8e70-6f732e664914)

###  Mensagem de sucesso:
![image](https://github.com/user-attachments/assets/5a95130c-e4d5-4d79-a8ad-63f0286c2325)

> A aplicação reconhece quando uma palavra é sinônimo de outra que já foi cadastrada no banco, dando a possibilidade de prosseguir com o cadastro ou não.

![image](https://github.com/user-attachments/assets/b1d76940-144a-49d3-b923-4a171907337e)

### Mensagem de confirmação:
![image](https://github.com/user-attachments/assets/4fe0a4a1-c10f-40c6-adca-3705fe70e354)

> No exemplo abaixo, o usuário escolher prosseguir com o cadastro da tag:

![image](https://github.com/user-attachments/assets/a3b93e61-92e2-4b4d-ad7c-0a5d82023ae0)

#### O cadastro da tag não possui validação de erro ou exclusão, mas é possível alterar a tag a qualquer momento. 

> Exemplo abaixo com a tag "Govreno", cadastrada errada:

![image](https://github.com/user-attachments/assets/d0c41558-8438-4cbb-aa92-c39bdaaec461)

> Clique em Editar na linha da tag que pretende alterar. A palavra retornará na caixa de texto para alteração

![image](https://github.com/user-attachments/assets/a8226821-1a89-41ad-85f9-7f519faead2d)

### Mensagem de sucesso de tag atualizada:

![image](https://github.com/user-attachments/assets/504d0c76-9baf-4a09-81e1-a71f2f68db5b)

# CADASTRO DE PORTAL

### Para cadastrar um portal, é necessário cadastrar ao menos uma tag primeiramente. A associação de uma tag por portal é obrigatória. 

#### Exemplo com o portal G1, escolhendo um nome e colando a URL do site, com ``https://``:
![image](https://github.com/user-attachments/assets/358ee674-95ec-4ffb-91a8-d5f6404870b6)

> Existe uma validação de URL. Se ela for inválida, uma exceção aparecerá na tela solicitando que o usuário verifique se o link está correto. 

![image](https://github.com/user-attachments/assets/cf1441a1-79d5-4077-ba54-1f8fd95bff8a)

### O agendador das APIs e dos portais é referente ao prazo de verificação de requisições e notícias recentes por meio do scraping. 
#### O prazo pode ser diário, semanal (de 7 em 7 dias) e mensal (de 30 em 30 dias), iniciando pela data de cadastramento do portal e continuando sucessivamente.
> O agendamento padrão é diário e pode ser alterado pelo usuário a qualquer momento.

![image](https://github.com/user-attachments/assets/355276b0-e7bb-4c59-80a4-38ad79805f5c)

> Para que o portal cadastrado já receba o web scraping, precisa ser mantido como "Ativo". Portais salvos como Desativos apenas armazenam as informações, mas não realizam o scraping.
  
![image](https://github.com/user-attachments/assets/60a6306c-97ee-470e-85d1-3d9873e50bed)

#### Para selecionar as tags no cadastro de um portal, o usuário pode digitar a palavra completa ou o começo dela e a tag aparecerá na tela à direita para seleção.

Caso o usuário queira selecionar várias tags de uma vez, clicar em Ctrl e selecionar as palavras escolhidas.

> Se preferir escolher uma a uma, deve selecionar uma tag. Assim que clicar, aparecerá embaixo da caixa de texto. Apagar o que foi digitado e digitar outra tag, selecionando uma por vez sem a necessidade do Ctrl.

![image](https://github.com/user-attachments/assets/21d96c38-dcc8-4402-bc12-fd9e24a87488)
![image](https://github.com/user-attachments/assets/1d4a74ee-0313-4fad-8972-4318408d02ae)
![image](https://github.com/user-attachments/assets/b4f8f018-51be-4486-8161-7b153ab2c23f)
![image](https://github.com/user-attachments/assets/d5ad0096-9396-4cea-8031-3d27508a26c5)

# CADASTRO DE SELETORES
![image](https://github.com/user-attachments/assets/a0fb400b-2f5d-472d-8211-39e94412c4a2)
![image](https://github.com/user-attachments/assets/eb9d03ee-070d-4799-b721-d6fcd3b21042)
![image](https://github.com/user-attachments/assets/e23df91d-6594-4410-8e3e-fc210746d5a6)

# Exemplos de Cadastro

## Portais de Notícia

Para facilitar os testes do sistema, utilize os exemplos abaixo para popular o banco com portais e notícias

## 1. Canal Rural

- **URL**: [https://www.canalrural.com.br](https://www.canalrural.com.br)
- **Caminho da Lista de Notícias**: `feed-link`
- **Título**: `h1.content-title`
- **Jornalista**: `p.content-author`
- **Conteúdo**: `div.single-content`
- **Data da Publicação da Notícia**: `time`

## 2. G1

- **URL**: [https://g1.globo.com](https://g1.globo.com)
- **Caminho da Lista de Notícias**: `feed-post-link`
- **Título**: `h1.content-head__title`
- **Jornalista**: `p.content-publication-data__from`
- **Conteúdo**: `p.content-text__container`
- **Data da Publicação da Notícia**: `time`

## 3. G1 - Agro

- **URL**: [https://g1.globo.com/economia/agronegocios/](https://g1.globo.com/economia/agronegocios/)
- **Caminho da Lista de Notícias**: `feed-post-link`
- **Título**: `h1.content-head__title`
- **Jornalista**: `p.content-publication-data__from`
- **Conteúdo**: `p.content-text__container`
- **Data da Publicação da Notícia**: `time`

> **Nota**: O G1 - Agro compartilha os mesmos seletores HTML que o portal G1, mas está focado em notícias do setor agro. Pode haver duplicação de notícias entre os dois portais se uma notícia sobre o agronegócio aparecer na página principal do G1. Este portal é ideal para testes de verificação de duplicidade.

![image](https://github.com/user-attachments/assets/478ef74d-980f-4675-8725-3a97b784bef5)
