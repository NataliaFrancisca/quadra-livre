create table horario_funcionamento(
    id BIGSERIAL PRIMARY KEY,

    dia varchar(10) NOT NULL,
    abertura time NOT NULL,
    fechamento time NOT NULL,
    disponibilidade boolean NOT NULL,

    quadra_id BIGINT NOT NULL,

    CONSTRAINT fk_quadra
        FOREIGN KEY (quadra_id)
        REFERENCES quadras(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT uq_quadra_dia UNIQUE (quadra_id, dia)
);