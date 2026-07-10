# Modelo de Dados

## Diagrama ER

```mermaid
erDiagram
    PARTICIPANT ||--o| INITIAL_ANSWER : responde
    PARTICIPANT ||--o| QUIZ_ANSWER : responde
    PARTICIPANT ||--o| QUIZ_RESULT : possui

    INITIAL_QUESTION ||--|{ INITIAL_QUESTION_OPTION : possui
    INITIAL_QUESTION ||--o{ INITIAL_ANSWER : recebe
    INITIAL_QUESTION_OPTION ||--o{ INITIAL_ANSWER : selecionada

    QUIZ_QUESTION ||--|{ QUIZ_OPTION : possui
    QUIZ_QUESTION ||--o{ QUIZ_ANSWER : recebe
    QUIZ_OPTION ||--o{ QUIZ_ANSWER : selecionada

    PARTICIPANT {
        bigint id PK
        varchar full_name
        varchar age_range
        varchar phone "opcional"
        timestamp created_at
    }

    INITIAL_QUESTION {
        bigint id PK
        varchar text
        int order_index
    }

    INITIAL_QUESTION_OPTION {
        bigint id PK
        bigint question_id FK
        varchar text
        int order_index
    }

    INITIAL_ANSWER {
        bigint id PK
        bigint participant_id FK
        bigint question_id FK
        bigint option_id FK
        timestamp answered_at
    }

    QUIZ_QUESTION {
        bigint id PK
        varchar text
        int order_index
    }

    QUIZ_OPTION {
        bigint id PK
        bigint question_id FK
        varchar text
        boolean is_correct
        int order_index
    }

    QUIZ_ANSWER {
        bigint id PK
        bigint participant_id FK
        bigint question_id FK
        bigint option_id FK
    }

    QUIZ_RESULT {
        bigint id PK
        bigint participant_id FK
        int score
        int total_questions
        timestamp completed_at
    }
```

## Justificativas de modelagem

- **`Participant` sem oficina/turma associada**: conforme decisão do
  planejamento, o evento é tratado como único no MVP; se no futuro for
  necessário rastrear múltiplas edições, basta introduzir uma entidade
  `Workshop` e uma FK opcional em `Participant`, sem quebrar o restante do
  modelo.
- **Questionário inicial e Quiz como módulos paralelos e independentes**,
  em vez de um modelo genérico único de "pergunta/resposta". O Quiz precisa
  de `is_correct` na opção para permitir correção automática (RF07); o
  questionário inicial não tem essa noção. Compartilhar uma tabela geraria
  uma coluna `is_correct` sempre nula para o questionário inicial — um
  cheiro de modelagem pior do que duplicar duas tabelas simples.
- **`QuizResult` é uma tabela de resumo, não recalculada em toda consulta.**
  O `Service` calcula a pontuação no momento da submissão do quiz e persiste
  o resultado agregado; `QuizAnswer` guarda o detalhe de cada resposta para
  a funcionalidade "Visualizar respostas" do Epic 7. Isso evita recalcular
  agregações a cada carregamento do dashboard (RF09).
- **Perguntas e opções (`INITIAL_QUESTION*`, `QUIZ_*QUESTION/OPTION`) são
  seed data via Flyway**, não cadastradas pela interface — não há tela de
  CRUD de perguntas no MVP, mantendo o escopo simples.
- **Sem exclusão física de participante no MVP** — apenas leitura/edição
  (RF02); exclusão pode ser avaliada futuramente se necessário para LGPD.
