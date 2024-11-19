CREATE TABLE Agendador (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(255),
    quantidade INT
);

CREATE TABLE Formato (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255)
);

CREATE TABLE Api (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) UNIQUE, -- Garantir unicidade no nome da API
    descricao VARCHAR(1000),
    url VARCHAR(255) UNIQUE, -- Garantir unicidade na URL da API
    dataCriacao DATE,
    ultimaAtualizacao DATE,
    agendador_id INT,
    formato_id INT,
    ativo BOOLEAN,
    FOREIGN KEY (agendador_id) REFERENCES Agendador(id),
    FOREIGN KEY (formato_id) REFERENCES Formato(id)
);

CREATE TABLE Tag (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255)
);

CREATE TABLE Api_Tag (
    api_id INT,
    tag_id INT,
    PRIMARY KEY (api_id, tag_id),
    FOREIGN KEY (api_id) REFERENCES Api(id),
    FOREIGN KEY (tag_id) REFERENCES Tag(id)
);

CREATE TABLE ApiDados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    conteudo TEXT,
    descricao VARCHAR(255),
    api_id INT,
    UNIQUE (conteudo), -- Garantir que não haja conteúdo duplicado
    FOREIGN KEY (api_id) REFERENCES Api(id)
);

CREATE TABLE Portal (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) UNIQUE, -- Garantir unicidade no nome do portal
    url VARCHAR(255) UNIQUE, -- Garantir unicidade na URL do portal
    seletorJornalista VARCHAR(254),
    seletorDataPublicacao VARCHAR(254),
    seletorConteudo VARCHAR(254),
    seletorTitulo VARCHAR(254),
    seletorCaminhoNoticia VARCHAR(254),
    ultimaAtualizacao DATE,
    dataCriacao DATE,
    agendador_id INT,
    ativo BOOLEAN,
    hasScrapedToday BOOLEAN,
    FOREIGN KEY (agendador_id) REFERENCES Agendador(id)
);

CREATE TABLE Noticia (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255),
    data DATE,
    conteudo TEXT,
    autor VARCHAR(255),
    href VARCHAR(255) UNIQUE, -- Garantir que não haja duplicidade nos links das notícias
    portal_id INT,
    FOREIGN KEY (portal_id) REFERENCES Portal(id)
);

CREATE TABLE Portal_Tag (
    portal_id INT,
    tag_id INT,
    PRIMARY KEY (portal_id, tag_id),
    FOREIGN KEY (portal_id) REFERENCES Portal(id),
    FOREIGN KEY (tag_id) REFERENCES Tag(id)
);
