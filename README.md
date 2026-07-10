# Inclusão Digital para Pessoas Idosas

Projeto desenvolvido como Atividade Extensionista do curso de Engenharia de Software da UNINTER.

## Objetivo

Desenvolver uma aplicação web para apoiar ações de inclusão digital voltadas a pessoas idosas, promovendo a conscientização sobre segurança online por meio de oficinas presenciais e materiais educativos.

A aplicação será utilizada como suporte às atividades realizadas junto ao grupo de convivência da **Paróquia Bom Jesus dos Migrantes**, localizada em **Sobradinho - Distrito Federal**.

---

## Funcionalidades

- Cadastro de participantes
- Questionário inicial
- Materiais educativos
- Quiz sobre segurança digital
- Registro dos resultados
- Painel do facilitador com estatísticas e exportação em CSV

---

## Tecnologias

### Frontend

- HTML5, CSS3, Bootstrap 5 (via WebJars, sem CDN)
- Thymeleaf
- Bootstrap Icons

### Backend

- Java 21
- Spring Boot 3

### Banco de Dados

- PostgreSQL
- Flyway (versionamento de schema)

### Build e Testes

- Maven
- JUnit 5, Mockito
- Testcontainers (testes de integração contra PostgreSQL real)

### Infraestrutura

- Docker / Docker Compose

---

## Arquitetura

Arquitetura MVC em camadas, com rotas separadas para o fluxo do participante (`/oficina/**`) e para o painel do facilitador (`/admin/**`). Detalhes completos de requisitos, modelo de dados, casos de uso e decisões técnicas estão documentados em [`docs/`](docs/):

- [`docs/01-requisitos.md`](docs/01-requisitos.md)
- [`docs/02-arquitetura.md`](docs/02-arquitetura.md)
- [`docs/03-modelo-dados.md`](docs/03-modelo-dados.md)
- [`docs/04-casos-de-uso.md`](docs/04-casos-de-uso.md)
- [`docs/05-wireframes.md`](docs/05-wireframes.md)

Estrutura de pacotes:

```
controller
service
repository
entity
dto
mapper
config
```

`validation`, `exception` e `util` (previstos no CLAUDE.md) não foram necessários como pacotes dedicados: a validação usa Bean Validation diretamente nos DTOs/entidades, e o tratamento de erro usa o mecanismo padrão do Spring Boot (`templates/error.html`) — nenhuma lógica customizada o suficiente para justificar uma camada própria.

---

## Como executar

### Pré-requisitos

- Java 21
- Maven (ou use o `.tool-versions` do repositório com [asdf](https://asdf-vm.com/))
- Docker e Docker Compose

### 1. Banco de dados local

```bash
docker compose up -d
```

Sobe apenas o PostgreSQL, usando as credenciais padrão em [`.env.example`](.env.example) (copie para `.env` se quiser customizar).

### 2. Aplicação

```bash
mvn spring-boot:run
```

Acesse `http://localhost:8080`.

### Alternativa: tudo containerizado

Para rodar a aplicação e o banco juntos, sem precisar de Java/Maven na máquina:

```bash
docker compose --profile full up -d --build
```

---

## Testes

```bash
mvn test
```

Roda os testes unitários e de controller (rápidos, sem dependências externas).

```bash
mvn verify
```

Além dos testes unitários, roda também o teste de integração de ponta a ponta (`*IT`), que sobe um PostgreSQL real via Testcontainers e valida o fluxo completo do participante (cadastro → questionário → materiais → quiz → resultado). **Requer Docker instalado.**

> **Nota:** em algumas versões recentes do Docker Desktop no WSL2, o
> Testcontainers pode falhar ao negociar a versão da API do Docker contra
> `/var/run/docker.sock`, mesmo com `docker info`/`docker pull`/`docker
> compose` funcionando normalmente (erro típico: `BadRequestException
> Status 400` seguido de `NullPointerException` em
> `DockerDesktopClientProviderStrategy`). Se isso ocorrer, tente
> `DOCKER_API_VERSION=1.43 mvn verify`. É uma incompatibilidade de
> ambiente conhecida, não um defeito do teste ou da aplicação.

---

## Público-alvo

Pessoas idosas participantes das oficinas de inclusão digital.

---

## Objetivos do Projeto

- promover inclusão digital;
- conscientizar sobre golpes virtuais;
- incentivar boas práticas de segurança online;
- registrar informações para análise dos resultados das oficinas.

---

## Status

✅ MVP completo — todas as Epics do [`BACKLOG.md`](BACKLOG.md) implementadas, testadas e prontas para implantação via Docker.

---

## Licença

Projeto acadêmico desenvolvido para fins educacionais.
