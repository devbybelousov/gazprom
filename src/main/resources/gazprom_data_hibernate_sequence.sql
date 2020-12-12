create table hibernate_sequence
(
    next_val bigint null
)
    engine = MyISAM;

INSERT INTO gazprom_data.hibernate_sequence (next_val) VALUES (3);