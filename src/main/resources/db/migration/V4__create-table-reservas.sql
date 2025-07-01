create table reservas(
    id varchar(100) PRIMARY KEY NOT NULL,

    abertura time NOT NULL,
    encerramento time NOT NULL,

    data date NOT NULL,

    usuario_id BIGINT NOT NULL,
    quadra_id BIGINT NOT NULL,

    CONSTRAINT fk_quadra
        FOREIGN KEY (quadra_id)
        REFERENCES quadras(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
)