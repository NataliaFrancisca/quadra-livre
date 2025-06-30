create table quadras(
    id BIGSERIAL PRIMARY KEY,
    nome varchar(120) NOT NULL,
    local varchar(120) NOT NULL,
    rua varchar(120) NOT NULL,
    numero varchar(25) NOT NULL,
    bairro varchar(100) NOT NULL,
    cidade varchar(100) NOT NULL,
    estado varchar(2) NOT NULL,

    gestor_id BIGINT NOT NULL,

    CONSTRAINT fk_gestor
        FOREIGN KEY (gestor_id)
        REFERENCES usuarios(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);