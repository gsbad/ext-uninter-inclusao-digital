<p align="center">
  <img src="docs/logo.svg" alt="Inclusão Digital" width="380">
</p>

![Java](https://img.shields.io/badge/Java-21-2E6B99?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-2E6B99?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-2E6B99?logo=postgresql&logoColor=white)
![Status](https://img.shields.io/badge/status-MVP%20completo-2E6B99)
![Licença](https://img.shields.io/badge/licença-acadêmica-2E6B99)

Projeto desenvolvido como Atividade Extensionista do curso de Engenharia de Software da UNINTER.

## Demonstração

> 🎬 GIF demonstrativo do fluxo completo (cadastro → questionário → materiais → quiz → resultado, e o painel do facilitador) — em breve.

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

## Deploy

A aplicação está publicada no **Render**, no plano gratuito (Free/Hobby):

**🔗 https://inclusao-digital.onrender.com**

### Infraestrutura

- **Web Service** (Docker, plano free, região Virginia) — build a partir do [`Dockerfile`](Dockerfile) deste repositório, com deploy automático a cada push na branch `main`.
- **PostgreSQL** (plano free, região Virginia) — schema criado automaticamente pelo Flyway na primeira inicialização.
- Infraestrutura documentada como código em [`render.yaml`](render.yaml).

### Como foi feito (resumo)

```bash
render login
render postgres create --name inclusao-digital-db --plan free --region virginia --confirm
render services create --type web_service --runtime docker \
  --repo https://github.com/gsbad/ext-uninter-inclusao-digital --branch main \
  --name inclusao-digital --plan free --region virginia --health-check-path / \
  --env-var SPRING_PROFILES_ACTIVE=prod --confirm
```

As variáveis `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER` e `DB_PASSWORD` do Web Service foram vinculadas ao banco de dados diretamente pelo Dashboard do Render (Environment → variável ligada à propriedade do banco), para a senha nunca precisar ser digitada ou exibida em texto puro. Detalhes completos das decisões técnicas em [`docs/07-deploy-render.md`](docs/07-deploy-render.md).

### Limitações do plano gratuito (importante)

- **O banco de dados PostgreSQL gratuito expira 30 dias após a criação** (mais 14 dias de carência antes da exclusão definitiva dos dados). Passado esse prazo sem upgrade para um plano pago, os dados cadastrados são perdidos.
- **O Web Service gratuito hiberna após 15 minutos sem tráfego** e "acorda" na próxima requisição — o primeiro acesso após um período de inatividade pode levar cerca de 1 a 2 minutos para responder (a JVM tem partida mais lenta que runtimes interpretados, e o plano free tem apenas 0.1 vCPU).

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

✅ MVP completo e **em produção** no Render — todas as Epics do [`BACKLOG.md`](BACKLOG.md) implementadas, testadas e publicadas em https://inclusao-digital.onrender.com.

---

## Licença

Projeto acadêmico desenvolvido para fins educacionais.
