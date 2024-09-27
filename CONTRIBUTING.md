<h1 align="center">Regras para Contribuição - Equipe Porygon</h1>

<h1 align="center">Sumário</h1>

<p align="center">
  <a href ="#requisitos"> REQUISITOS </a>  •
  <a href ="#conventional-commits"> CONVENTIONAL COMMITS </a>  •
  <a href ="#estrutura"> ESTRUTURA </a>  •
  <a href="#tipos-e-exemplos"> TIPOS E EXEMPLOS </a> •
  <a href="#feature-branch"> FEATURE BRANCH </a> •
  <a href ="#fix-branch"> FIX BRANCH </a>  •
  <a href ="#pull-request"> PULL REQUEST </a>
</p>

# REQUISITOS

- **Java 22** ou superior
- **Spring Boot 3.3.3**
- **Maven** para gerenciamento de dependências
- **MySQL** configurado e rodando

## Configuração do Banco de Dados

1. Certifique-se de que o MySQL está instalado e em execução.
2. Crie um banco de dados chamado `porygon`.
3. Configure o arquivo `application.properties` para incluir as informações de conexão com o banco de dados:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/porygon
    spring.datasource.username=seu_usuario
    spring.datasource.password=sua_senha
    spring.jpa.hibernate.ddl-auto=update
    ```

## Instalação

1. Clone o repositório para o seu ambiente local:

    ```bash
    git clone [https://github.com/seu-usuario/Porygon.git](https://github.com/PorygonAPI/Porygon2.git)
    ```

2. Navegue até o diretório do projeto:

    ```bash
    cd PorygonAPI/Porygon2
    ```

3. Execute o comando Maven para construir o projeto:

    ```bash
    mvn clean install
    ```

# CONVENTIONAL COMMITS

O padrão de commit mais utilizado é o Conventional Commits. Esse padrão segue uma convenção que facilita o entendimento das mudanças no histórico do projeto, a automação do versionamento semântico, e a geração de changelogs.

## ESTRUTURA

#### Tudo escrito em letra minúscula, menos os IDs da User Story e task. Os IDs devem ser separados por espaço.
##### O ideal é fazer vários commits menores rastreando a User Story e a task correspondente, do que fazer apenas 1 commit para várias tasks.  

#### ID-user-story ID-task tipo(escopo opcional): descrição

``git commit -m "PYG-2 PYG-5 feat(autenticacao): adiciona endpoint de login"``

- **ID da User Story**: ID presente no Jira. Terá o modelo PYG-Número
- **ID da Task**: ID presente no Jira. Terá o modelo PYG-Número
-	**Tipo**: O tipo de mudança que o commit representa.
-	**Escopo** (opcional): Parte do código impactada pela mudança (por exemplo, uma função ou módulo específico).
-	**Descrição**: Uma breve descrição da mudança.

## TIPOS E EXEMPLOS

1.	feat: Usado para uma nova funcionalidade.

```feat(autenticacao): adiciona endpoint de login com autenticação JWT```

2.	fix: Usado para correção de bugs.

```fix(usuario): corrige o upload da foto de perfil do usuário```

3.	chore: Usado para tarefas de manutenção que não afetam o código de produção.

```chore: atualiza dependências do projeto```

4.	docs: Usado para mudanças na documentação.
   
```docs(readme): atualiza as instruções de instalação```

5.	style: Usado para mudanças no estilo que não afetam o significado do código (espaços em branco, formatação, ponto e vírgula, etc.).
   
```style: corrige a indentação no main.css```

6.	refactor: Usado para refatoração, mudanças que melhoram o código sem corrigir bugs ou adicionar funcionalidades.
    
```refactor(serviço-usuário): remove verificações redundantes na validação```

7.	perf: Usado para mudanças de desempenho.
    
```perf(api): reduz o tempo de resposta do endpoint de busca de usuários```

8.	test: Usado para adicionar ou corrigir teste.
    
```test(usuário): adiciona testes unitários para atualização de perfil```

9.	build: Usado para mudanças que afetam o sistema de construção ou dependências externas.
    
```build: adiciona configuração Docker para deployment```

10.	ci: Usado para mudanças de integração contínua nos arquivos e scripts de configuração do CI (Continuous Integration).
    
```ci: atualiza o workflow do GitHub Actions para verificações em pull requests```

11.	revert: Usado para reverter um commit anterior.
    
```revert: reverte "feat(auth): adiciona endpoint de login com autenticação JWT"```

12.	hotfix: Usado para correção urgente, normalmente em produção.
    
```hotfix: corrige falha ao registrar usuário em produção```

### FEATURE BRANCH

Uma feature branch é uma ramificação criada para desenvolver uma nova funcionalidade (feature) ou melhoria no projeto. Essa branch é criada a partir da branch principal e tem como objetivo isolar o desenvolvimento da nova funcionalidade para que outros desenvolvedores possam continuar a trabalhar sem serem afetados por mudanças instáveis.

Deve ser escrito com letra minúscula, conter o ID da User Story e da task (com letra maiúscula), com todas as informações separadas por barra (/). Não deve conter espaço na branch, portanto, a descrição da funcionalidade deve ser descrita com hífen (-).

```git checkout -b PYG-1/PYG-3/feature/nome-da-nova-funcionalidade```

### FIX BRANCH

Uma fix branch é uma ramificação criada para corrigir um bug ou erro no código. Essa branch também é criada a partir da branch principal, mas seu foco é em correções de problemas, em vez de adicionar novas funcionalidades.

Deve ser escrito com letra minúscula, conter o ID da User Story e da task (com letra maiúscula), com todas as informações separadas por barra (/). Não deve conter espaço na branch, portanto, a descrição do reparo deve ser descrita com hífen (-).

```git checkout -b PYG-1/PYG-3/fix/descricao-do-bug```

#### VANTAGENS

-	**Isolamento**: As mudanças feitas em uma feature ou fix branch não afetam o código na branch principal até que estejam completas e sejam mescladas.
-	**Colaboração**: Várias pessoas podem trabalhar em diferentes funcionalidades ou correções ao mesmo tempo, sem interferir no trabalho umas das outras.
-	**Controle de Qualidade**: Permite revisões de código e testes antes de integrar as mudanças na branch principal, garantindo que o código esteja estável.

### PULL REQUEST

Após concluir a funcionalidade ou correção, abra uma PR com todos os IDs (User Story e tasks desenvolvidas) e uma descrição das implementações realizadas.
Essa solicitação poderá ser realizada através do Jira, na opção de solicitação de Pull Request, ou através do Github.

``PYG-2 PYG-3 PYG-5: Descritivo da PR``