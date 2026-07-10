# Arquitetura

## Visão geral

Monólito Spring Boot MVC com renderização server-side via Thymeleaf. Não há
SPA nem API REST separada no MVP: menos JavaScript client-side significa
menos pontos de falha e menor barreira de acessibilidade para o público-alvo.

```
Browser (Thymeleaf + Bootstrap 5)
        │
        ▼
  Controller  (rotas HTTP, sem lógica de negócio)
        │
        ▼
   Service    (regras de negócio, cálculo de pontuação, validações de domínio)
        │
        ▼
  Repository  (Spring Data JPA)
        │
        ▼
   PostgreSQL
```

Pacotes: `controller`, `service`, `repository`, `entity`, `dto`, `mapper`,
`validation`, `config`, `exception`, `util` — conforme definido no CLAUDE.md.

## Separação de rotas por ator

O sistema tem dois públicos com necessidades de UX radicalmente diferentes:

| Área | Rota (proposta) | Ator | Características |
|---|---|---|---|
| Fluxo da oficina | `/oficina/**` | Idoso (participante) | Wizard linear, fontes grandes, sem menus |
| Painel administrativo | `/admin/**` | Facilitador/organizador | Tabelas, filtros, exportação CSV |

Essa separação já prepara o terreno para aplicar autenticação apenas em
`/admin/**` no futuro (RNF07), sem precisar refatorar rotas depois.

## Decisões técnicas e justificativas

- **Flyway para versionamento de schema.** Em vez de depender de
  `ddl-auto` do Hibernate, migrations versionadas em
  `src/main/resources/db/migration` garantem reprodutibilidade do schema —
  pré-requisito natural para o empacotamento Docker (Epic 9/10) e prática
  padrão de mercado.
- **Materiais educativos como templates Thymeleaf estáticos**, não como
  entidade de banco. O conteúdo é fixo e curado pela equipe do projeto;
  não há requisito de edição via interface. Evita uma tabela e uma tela de
  CRUD que não agregam valor ao MVP.
- **Perguntas do questionário inicial e do quiz persistidas via seed
  (Flyway)**, não hardcoded no código Java. Diferente dos materiais
  educativos, essas perguntas são referenciadas por respostas de usuários
  (chave estrangeira), então já nascem como entidades — e ficam editáveis
  sem novo deploy.
- **DTOs + Mapper** desacoplam o modelo de persistência da camada de
  apresentação, permitindo simplificar o formulário do idoso (poucos
  campos) mesmo que a entidade tenha metadados internos (timestamps, etc.).
- **`@ControllerAdvice` global** traduz qualquer exceção não tratada em uma
  mensagem simples para o participante, evitando stack traces ou erros
  técnicos na tela do idoso (RNF06).
- **Sem Spring Security no MVP.** A estrutura `config` fica reservada, mas
  a dependência só é adicionada quando a autenticação for de fato
  implementada, evitando complexidade prematura (RNF07).

## Testes

Testes unitários de `Service` (JUnit 5 + Mockito) acompanham cada Epic de
funcionalidade (3, 4, 6, 7), não ficam concentrados no Epic 9. O Epic 9 foca
em testes de integração (`@SpringBootTest` + `MockMvc`), Docker e deploy.
