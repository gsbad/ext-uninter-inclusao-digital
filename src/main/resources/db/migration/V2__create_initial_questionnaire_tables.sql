CREATE TABLE initial_question (
    id BIGSERIAL PRIMARY KEY,
    text VARCHAR(255) NOT NULL,
    order_index INT NOT NULL
);

CREATE TABLE initial_question_option (
    id BIGSERIAL PRIMARY KEY,
    question_id BIGINT NOT NULL REFERENCES initial_question(id),
    text VARCHAR(255) NOT NULL,
    order_index INT NOT NULL
);

CREATE TABLE initial_answer (
    id BIGSERIAL PRIMARY KEY,
    participant_id BIGINT NOT NULL REFERENCES participant(id),
    question_id BIGINT NOT NULL REFERENCES initial_question(id),
    option_id BIGINT NOT NULL REFERENCES initial_question_option(id),
    answered_at TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT uk_initial_answer_participant_question UNIQUE (participant_id, question_id)
);
