create table indisponibilidades(
    id BIGSERIAL PRIMARY KEY,
    data date NOT NULL,

    quadra_id BIGINT NOT NULL,

    CONSTRAINT fk_quadra
        FOREIGN KEY (quadra_id)
        REFERENCES quadras(id)
);