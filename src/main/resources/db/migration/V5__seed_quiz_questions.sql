-- Perguntas fixas do quiz de fixação, ligadas aos temas do Epic 5.

INSERT INTO quiz_question (id, text, order_index) VALUES
    (1, 'Se alguém te ligar dizendo ser do banco e pedir sua senha, o que você deve fazer?', 1),
    (2, 'Como reconhecer um link falso?', 2),
    (3, 'Antes de fazer um PIX para alguém, o que é mais importante?', 3),
    (4, 'Você recebe uma mensagem no WhatsApp de um "familiar" pedindo dinheiro com urgência. O que fazer?', 4),
    (5, 'Qual é uma boa prática para senhas?', 5);

INSERT INTO quiz_option (id, question_id, text, is_correct, order_index) VALUES
    (101, 1, 'Informar a senha rapidamente para resolver o problema', FALSE, 1),
    (102, 1, 'Desligar e ligar de volta para o número oficial do banco', TRUE, 2),
    (103, 1, 'Enviar a senha por WhatsApp para confirmar', FALSE, 3),

    (201, 2, 'Ele promete prêmios muito bons e tem erros de português', TRUE, 1),
    (202, 2, 'Ele foi enviado por um amigo, então é sempre seguro', FALSE, 2),
    (203, 2, 'Todos os links são seguros se tiverem "https"', FALSE, 3),

    (301, 3, 'Fazer rápido, para não perder tempo', FALSE, 1),
    (302, 3, 'Conferir o nome de quem vai receber o PIX', TRUE, 2),
    (303, 3, 'Compartilhar sua senha do banco com a pessoa', FALSE, 3),

    (401, 4, 'Enviar o dinheiro imediatamente', FALSE, 1),
    (402, 4, 'Ligar para a pessoa para confirmar antes de enviar qualquer valor', TRUE, 2),
    (403, 4, 'Responder pelo WhatsApp confirmando o valor', FALSE, 3),

    (501, 5, 'Usar a mesma senha em todos os aplicativos', FALSE, 1),
    (502, 5, 'Usar a data de nascimento para não esquecer', FALSE, 2),
    (503, 5, 'Usar senhas longas e diferentes para cada aplicativo', TRUE, 3);

SELECT setval('quiz_question_id_seq', (SELECT MAX(id) FROM quiz_question));
SELECT setval('quiz_option_id_seq', (SELECT MAX(id) FROM quiz_option));
