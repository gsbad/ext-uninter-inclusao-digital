# Deploy no Render

Documento de referência da implantação em produção, no plano gratuito
(Free/Hobby) do [Render](https://render.com).

**URL pública:** https://inclusao-digital.onrender.com

---

## Arquivos modificados

| Arquivo | Motivo |
|---|---|
| `src/main/resources/application.yml` | `server.port` passou de `${SERVER_PORT:8080}` para `${PORT:${SERVER_PORT:8080}}`, para honrar a variável `PORT` que o Render injeta em todo Web Service, sem quebrar o fluxo local (`docker-compose.yml`, que define `SERVER_PORT`). |
| `src/main/resources/application-prod.yml` (novo) | Perfil de produção, ativado por `SPRING_PROFILES_ACTIVE=prod`. Habilita cache do Thymeleaf, exige SSL na conexão com o Postgres (`?sslmode=require`) e reduz o pool de threads do Tomcat (200 → 50), adequando a aplicação aos 0.1 vCPU / 512 MB de RAM do plano free. |
| `Dockerfile` | `ENTRYPOINT` ganhou `-XX:MaxRAMPercentage=70.0 -XX:+UseSerialGC`, tuning de JVM recomendado para containers de memória e CPU reduzidas (evita que a JVM reserve RAM demais e troca o coletor de lixo padrão, que usa múltiplas threads, por um single-thread). |
| `render.yaml` (novo) | Blueprint (Infraestrutura-como-Código) documentando os dois recursos provisionados. Validado com `render blueprints validate`. |
| `README.md` | Nova seção "Deploy" com a URL pública, resumo dos comandos usados e limitações do plano gratuito. |

Nenhuma mudança em regra de negócio, entidade, repositório, migration ou rota.

---

## Variáveis de ambiente utilizadas (Web Service)

| Variável | Origem | Observação |
|---|---|---|
| `PORT` | Injetada automaticamente pelo Render | Não configurada manualmente. |
| `SPRING_PROFILES_ACTIVE` | Definida na criação do serviço | Valor `prod`. |
| `DB_HOST` | Vinculada ao Postgres pelo Dashboard | Propriedade "Host" do banco `inclusao-digital-db`. |
| `DB_PORT` | Vinculada ao Postgres pelo Dashboard | Propriedade "Port". |
| `DB_NAME` | Vinculada ao Postgres pelo Dashboard | Propriedade "Database". |
| `DB_USER` | Vinculada ao Postgres pelo Dashboard | Propriedade "User". |
| `DB_PASSWORD` | Vinculada ao Postgres pelo Dashboard | Propriedade "Password" — nunca inserida como texto em nenhum comando ou arquivo do repositório. |

## Serviços criados no Render

| Recurso | Nome | Plano | Região | Runtime |
|---|---|---|---|---|
| Web Service | `inclusao-digital` | Free | Virginia (US East) | Docker (build a partir do `Dockerfile` do repositório) |
| PostgreSQL | `inclusao-digital-db` | Free | Virginia (US East) | PostgreSQL 18 |

Ambos na mesma região para permitir conexão via rede interna do Render
entre o Web Service e o banco.

---

## Processo de implantação

1. `render login` (autenticação de conta; exigiu abrir manualmente a URL de
   autorização de dispositivo, já que este ambiente WSL não tinha um
   abridor de navegador padrão instalado).
2. `render postgres create --name inclusao-digital-db --plan free --region virginia --confirm`
3. `render services create --type web_service --runtime docker --repo <url> --branch main --name inclusao-digital --plan free --region virginia --health-check-path / --env-var SPRING_PROFILES_ACTIVE=prod --confirm`
4. Vínculo manual, pelo Dashboard do Render, das 5 variáveis de conexão com
   o banco (`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`) —
   este passo específico não foi feito via CLI porque a versão da CLI
   usada não expõe um mecanismo de vínculo a propriedades de banco para
   variáveis de ambiente (só o `render.yaml`, cuja sincronização via CLI
   também não está disponível nesta versão — apenas `validate`).
5. Deploy automático disparado pela atualização das variáveis; acompanhado
   via `render deploys list` e `render logs`.
6. Validação funcional na URL pública: página inicial, formulário de
   cadastro, painel administrativo e um fluxo real de cadastro
   (confirmado no painel `/admin/dashboard`, que passou a mostrar 1
   participante após o teste).

### Problema encontrado e corrigido durante o processo

Na primeira tentativa de configurar as variáveis de conexão com o banco,
elas foram preenchidas com o texto literal dos nomes das propriedades
("Host", "Port", "Database"...) em vez dos valores reais, resultando na
URL JDBC inválida `jdbc:postgresql://Host:Port/Database?sslmode=require`
nos logs, e falha do Flyway ao conectar. Diagnosticado a partir dos
logs (`render logs`), e corrigido preenchendo as variáveis com os valores
reais do banco.

---

## Limitações do plano gratuito

- **PostgreSQL expira 30 dias após a criação**, com 14 dias de carência
  antes da exclusão definitiva dos dados. Aceito conscientemente para
  esta fase (portfólio/demonstração acadêmica); ver recomendação abaixo
  para uso continuado.
- **Web Service hiberna após 15 minutos sem tráfego**, com tempo de
  "acordar" de cerca de 1 a 2 minutos no request seguinte — mais lento
  que o típico de runtimes interpretados, por causa do tempo de
  inicialização da JVM e do contexto do Spring Boot (observado ~108s de
  boot completo nos logs desta implantação).
- **512 MB de RAM e 0.1 vCPU** — suficiente para esta aplicação (MVC
  simples, sem processamento pesado), mas sem margem para crescimento
  significativo sem upgrade de plano.
- **1 GB de armazenamento** no banco — mais que suficiente para o volume
  de dados de oficinas presenciais.

## Recomendações para uma futura produção real

- Fazer upgrade do Postgres para um plano pago antes de qualquer uso
  real e contínuo (evita a expiração de 30 dias).
- Fazer upgrade do Web Service para o plano "Starter" (ou superior) para
  eliminar a hibernação após inatividade — relevante se a aplicação for
  usada no dia da oficina sem aviso prévio para "acordá-la" antes.
- Trocar a senha do banco de dados gerada na criação (via Dashboard do
  Render, opção de redefinir credenciais), já que ela chegou a ser
  temporariamente exposta em texto durante o processo de configuração
  manual das variáveis de ambiente.
- Considerar adicionar o Spring Boot Actuator com um endpoint de health
  check dedicado (`/actuator/health`), hoje substituído pela própria
  página inicial (`/`) por simplicidade — suficiente para o Render
  detectar que a aplicação está de pé, mas menos informativo que um
  health check dedicado.
