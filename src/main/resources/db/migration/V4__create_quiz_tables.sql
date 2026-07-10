CREATE TABLE quiz_question (
    id BIGSERIAL PRIMARY KEY,
    text VARCHAR(255) NOT NULL,
    order_index INT NOT NULL
);

CREATE TABLE quiz_option (
    id BIGSERIAL PRIMARY KEY,
    question_id BIGINT NOT NULL REFERENCES quiz_question(id),
    text VARCHAR(255) NOT NULL,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE,
    order_index INT NOT NULL
);

CREATE TABLE quiz_answer (
    id BIGSERIAL PRIMARY KEY,
    participant_id BIGINT NOT NULL REFERENCES participant(id),
    question_id BIGINT NOT NULL REFERENCES quiz_question(id),
    option_id BIGINT NOT NULL REFERENCES quiz_option(id),
    answered_at TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT uk_quiz_answer_participant_question UNIQUE (participant_id, question_id)
);

CREATE TABLE quiz_result (
    id BIGSERIAL PRIMARY KEY,
    participant_id BIGINT NOT NULL UNIQUE REFERENCES participant(id),
    score INT NOT NULL,
    total_questions INT NOT NULL,
    completed_at TIMESTAMP NOT NULL DEFAULT now()
);
