create table usuarios(
    id BIGSERIAL NOT NULL UNIQUE,

    login varchar(100) NOT NULL UNIQUE,
    senha varchar(255) NOT NULL,

    nome varchar(100) NOT NULL,
    telefone varchar(50) NOT NULL,
    cpf varchar(25) NOT NULL UNIQUE,
    role varchar(25) NOT NULL,

    primary key (id)
);