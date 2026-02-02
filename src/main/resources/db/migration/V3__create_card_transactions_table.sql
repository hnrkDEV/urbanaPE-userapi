CREATE TABLE card_transactions (
                                   id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

                                   card_id BIGINT NOT NULL,

                                   type VARCHAR(50) NOT NULL,

                                   valor DOUBLE PRECISION NOT NULL,

                                   saldo_anterior DOUBLE PRECISION NOT NULL,

                                   saldo_atual DOUBLE PRECISION NOT NULL,

                                   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                   CONSTRAINT fk_card_transactions_card
                                       FOREIGN KEY (card_id)
                                           REFERENCES cards(id)
                                           ON DELETE CASCADE
);
