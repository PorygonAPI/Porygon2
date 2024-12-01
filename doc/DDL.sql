-- Tabela Agendador
create table Agendador (
    id int auto_increment primary key,
    tipo varchar(255),
    quantidade int
);

-- Tabela API
create table Api (
    id int auto_increment primary key,
    nome varchar(255),
    descricao varchar(1000),
    url varchar(255),
    datacriacao date,
    ultimaatualizacao date,
    agendador_id int,
    formato_id int,
    ativo boolean,
    foreign key (agendador_id) references agendador(id),
    foreign key (formato_id) references formato(id)
);

-- Tabela API Dados
create table ApiDados (
    id int auto_increment primary key,
    conteudo text,
    descricao varchar(255),
    datacoleta date,
    api_id int not null,
    foreign key (api_id) references api(id)
);

-- Tabela Formato
create table Formato (
    id int auto_increment primary key,
    nome varchar(255)
);

-- Tabela Jornalista
create table Jornalista (
    id int auto_increment primary key,
    nome varchar(255) unique
);

-- Tabela Notícia
create table Noticia (
    id int auto_increment primary key,
    titulo varchar(255),
    data date,
    conteudo varchar(20000),
    href varchar(255) unique,
    portal_id int,
    jornalista_id int,
    foreign key (portal_id) references portal(id),
    foreign key (jornalista_id) references jornalista(id)
);

-- Tabela Portal
create table Portal (
    id int auto_increment primary key,
    nome varchar(255),
    url varchar(255),
    seletorjornalista varchar(254),
    seletordatapublicacao varchar(254),
    seletorconteudo varchar(254),
    seletortitulo varchar(254),
    seletorcaminhonoticia varchar(254),
    ultimaatualizacao date,
    datacriacao date,
    agendador_id int,
    ativo boolean,
    hasscrapedtoday boolean,
    foreign key (agendador_id) references agendador(id)
);

-- Tabela Sinônimo
create table Sinonimo (
    id bigint auto_increment primary key,
    nome varchar(46),
    tag_id int not null,
    foreign key (tag_id) references tag(id),
    unique (nome, tag_id)
);

-- Tabela Tag
create table Tag (
    id int auto_increment primary key,
    nome varchar(46) unique
);

-- Tabela de relacionamento Api_Tag (Muitos-para-muitos entre API e Tag)
create table api_tag (
    api_id int,
    tag_id int,
    primary key (api_id, tag_id),
    foreign key (api_id) references api(id),
    foreign key (tag_id) references tag(id)
);

-- Tabela de relacionamento ApiDados_Tag (Muitos-para-muitos entre API Dados e Tag)
create table api_dados_tag (
    api_dados_id int,
    tag_id int,
    primary key (api_dados_id, tag_id),
    foreign key (api_dados_id) references apidados(id),
    foreign key (tag_id) references tag(id)
);

-- Tabela de relacionamento Portal_Tag (Muitos-para-muitos entre Portal e Tag)
create table portal_tag (
    portal_id int,
    tag_id int,
    primary key (portal_id, tag_id),
    foreign key (portal_id) references portal(id),
    foreign key (tag_id) references tag(id)
);

-- Tabela de relacionamento Noticia_Tag (Muitos-para-muitos entre Noticia e Tag)
create table noticia_tag (
    noticia_id int,
    tag_id int,
    primary key (noticia_id, tag_id),
    foreign key (noticia_id) references noticia(id),
    foreign key (tag_id) references tag(id)
);

