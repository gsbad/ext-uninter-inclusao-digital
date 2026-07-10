-- Perguntas fixas do questionário diagnóstico inicial. Não há "resposta
-- certa/errada" aqui; o objetivo é conhecer o perfil de uso digital do
-- participante antes da oficina.

INSERT INTO initial_question (id, text, order_index) VALUES
    (1, 'Você costuma usar a internet no dia a dia?', 1),
    (2, 'Você usa aplicativos de mensagem, como o WhatsApp?', 2),
    (3, 'Você sabe o que é um link falso (phishing)?', 3),
    (4, 'Você costuma fazer pagamentos usando o PIX?', 4),
    (5, 'Você já recebeu alguma mensagem suspeita pedindo dinheiro ou dados pessoais?', 5);

INSERT INTO initial_question_option (id, question_id, text, order_index) VALUES
    (101, 1, 'Sim', 1),
    (102, 1, 'Não', 2),
    (103, 1, 'Um pouco', 3),

    (201, 2, 'Sim', 1),
    (202, 2, 'Não', 2),
    (203, 2, 'Às vezes', 3),

    (301, 3, 'Sim, sei bem', 1),
    (302, 3, 'Já ouvi falar', 2),
    (303, 3, 'Não sei o que é', 3),

    (401, 4, 'Sim', 1),
    (402, 4, 'Não', 2),
    (403, 4, 'Às vezes', 3),

    (501, 5, 'Sim', 1),
    (502, 5, 'Não', 2),
    (503, 5, 'Não tenho certeza', 3);

SELECT setval('initial_question_id_seq', (SELECT MAX(id) FROM initial_question));
SELECT setval('initial_question_option_id_seq', (SELECT MAX(id) FROM initial_question_option));
