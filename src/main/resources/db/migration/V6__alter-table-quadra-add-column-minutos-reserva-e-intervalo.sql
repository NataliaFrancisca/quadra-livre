ALTER TABLE quadras ADD COLUMN minutosReserva int;
ALTER TABLE quadras ADD COLUMN minutosIntervalo int;

UPDATE quadras SET minutosReserva = 60, minutosIntervalo = 10;

ALTER TABLE quadras ALTER COLUMN minutosReserva SET NOT NULL;
ALTER TABLE quadras ALTER COLUMN minutosIntervalo SET NOT NULL;