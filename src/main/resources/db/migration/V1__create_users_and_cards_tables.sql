
CREATE TABLE users (
                       id BIGINT BIGSERIAL  PRIMARY KEY,

                       nome VARCHAR(100) NOT NULL,

                       email VARCHAR(150) NOT NULL UNIQUE,

                       senha VARCHAR(255) NOT NULL,

                       role VARCHAR(50) NOT NULL
);

CREATE TABLE cards (
                       id BIGINT BIGSERIAL  PRIMARY KEY,

                       numero_cartao BIGINT NOT NULL,

                       nome VARCHAR(100) NOT NULL,

                       status BOOLEAN NOT NULL,

                       tipo_cartao VARCHAR(50) NOT NULL,

                       user_id BIGINT NOT NULL,

                       CONSTRAINT fk_cards_user
                           FOREIGN KEY (user_id)
                               REFERENCES users(id)
                               ON DELETE CASCADE
);
