-- tabela agendador
create table agendador (
id int auto_increment primary key,
tipo varchar(255) not null,
quantidade int not null);

-- tabela api
create table api (
    id int auto_increment primary key,
    nome varchar(255) not null,
    descricao varchar(1000),
    url varchar(255),
    formato varchar(255),
    datacriacao date,
    ultimaatualizacao date,
    agendador_id int,
    ativo boolean not null,
    constraint fk_api_agendador foreign key (agendador_id) references agendador(id)
);

-- tabela apidados
create table apidados (
    id int auto_increment primary key,
    conteudo text,
    descricao varchar(255),
    api_id int,
    constraint fk_apidados_api foreign key (api_id) references api(id)
);

-- tabela noticia
create table noticia (
    id int auto_increment primary key,
    titulo varchar(255) not null,
    data date,
    conteudo text,
    autor varchar(255),
    portal_id int,
    constraint fk_noticia_portal foreign key (portal_id) references portal(id)
);

-- tabela portal
create table portal (
    id int auto_increment primary key,
    nome varchar(255) not null,
    url varchar(255),
    seletorjornalista varchar(254),
    seletordatapublicacao varchar(254),
    seletorconteudo varchar(254),
    seletortitulo varchar(254),
    seletorcaminhonoticia varchar(254),
    ultimaatualizacao date,
    datacriacao date,
    agendador_id int,
    ativo boolean not null,
    constraint fk_portal_agendador foreign key (agendador_id) references agendador(id)
);

-- tabela tag
create table tag (
    id int auto_increment primary key,
    nome varchar(255) not null,
    nomesinonimo varchar(255)
);

-- tabela api_tag (relacionamento n:m entre api e tag)
create table api_tag (
    api_id int,
    tag_id int,
    constraint fk_api_tag_api foreign key (api_id) references api(id),
    constraint fk_api_tag_tag foreign key (tag_id) references tag(id),
    primary key (api_id, tag_id)
);

-- tabela portal_tag (relacionamento n:m entre portal e tag)
create table portal_tag (
    portal_id int,
    tag_id int,
    constraint fk_portal_tag_portal foreign key (portal_id) references portal(id),
    constraint fk_portal_tag_tag foreign key (tag_id) references tag(id),
    primary key (portal_id, tag_id)
);

-- constraints de verificação (check constraints)
alter table agendador
add constraint chk_quantidade check (quantidade (1, 7, 30));

alter table agendador
add constraint chk_quantidade check (quantidade (1, 7, 30));

alter table api
add constraint chk_api_formato check (formato in ('json', 'xml', 'csv', 'text'));

alter table portal
add constraint chk_ativo check (ativo in (0, 1));

alter table api
add constraint chk_ativo check (ativo in (0, 1));
