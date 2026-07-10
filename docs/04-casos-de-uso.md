# Casos de Uso

## Atores

- **Participante**: idoso que realiza a oficina, opera o sistema diretamente.
- **Facilitador/Administrador**: organizador da oficina, consulta dados
  agregados. Não requer login no MVP, mas as rotas usadas por este ator
  ficam isoladas em `/admin/**` (ver `02-arquitetura.md`).

## UC01 — Cadastrar Participante

- **Ator**: Participante
- **Pré-condição**: nenhuma
- **Fluxo principal**:
  1. Participante acessa a tela inicial da oficina.
  2. Informa nome completo.
  3. Seleciona faixa etária em botões pré-definidos.
  4. (Opcional) Informa telefone de contato.
  5. Confirma cadastro.
  6. Sistema persiste o participante e avança para UC02.
- **Fluxo alternativo**: nome em branco → mensagem de erro simples,
  permanece na mesma tela (RNF06).

## UC02 — Responder Questionário Inicial

- **Ator**: Participante
- **Pré-condição**: UC01 concluído
- **Fluxo principal**:
  1. Sistema apresenta uma pergunta por tela, com opções em botões.
  2. Participante seleciona uma opção e avança.
  3. Repete até a última pergunta.
  4. Sistema persiste todas as respostas vinculadas ao participante.
  5. Avança para o material educativo (UC03).

## UC03 — Consultar Material Educativo

- **Ator**: Participante (com apoio do facilitador durante a oficina)
- **Fluxo principal**:
  1. Sistema apresenta lista de temas (golpes, links falsos, PIX,
     WhatsApp, senhas, privacidade).
  2. Participante navega pelos temas em qualquer ordem.
  3. Participante avança para o quiz (UC04) quando pronto.

## UC04 — Responder Quiz

- **Ator**: Participante
- **Pré-condição**: UC02 concluído
- **Fluxo principal**:
  1. Sistema apresenta uma pergunta de múltipla escolha por tela.
  2. Participante seleciona uma opção e avança.
  3. Repete até a última pergunta.
  4. Sistema corrige automaticamente e calcula a pontuação (RF07).
  5. Sistema persiste `QuizAnswer` (detalhe) e `QuizResult` (agregado).
  6. Avança para UC05.

## UC05 — Visualizar Resultado do Quiz

- **Ator**: Participante
- **Fluxo principal**:
  1. Sistema exibe a pontuação obtida em linguagem simples e positiva
     (evitar tom de reprovação — o objetivo é conscientizar, não avaliar).
  2. Tela de agradecimento encerra o fluxo do participante.

## UC06 — Visualizar Estatísticas da Oficina

- **Ator**: Facilitador/Administrador
- **Fluxo principal**:
  1. Acessa `/admin/dashboard`.
  2. Sistema exibe: total de participantes, pontuação média do quiz,
     distribuição de respostas do questionário inicial.
  3. Facilitador pode listar participantes individualmente (RF02) e ver
     as respostas detalhadas de cada um.

## UC07 — Exportar Resultados em CSV

- **Ator**: Facilitador/Administrador
- **Pré-condição**: UC06
- **Fluxo principal**:
  1. Facilitador clica em "Exportar CSV" no painel administrativo.
  2. Sistema gera arquivo com participantes, respostas e pontuações.
  3. Download é iniciado pelo navegador.
