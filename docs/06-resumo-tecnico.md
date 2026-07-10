# Resumo Técnico da Solução

> Documento de apoio à Atividade Extensionista do curso de Engenharia de
> Software da UNINTER, com o objetivo de detalhar as tecnologias, a
> arquitetura e as decisões técnicas do projeto "Inclusão Digital para
> Pessoas Idosas", para inclusão no relatório entregue à professora
> orientadora.

---

## 1. Arquitetura da aplicação

### Padrão arquitetural adotado

O sistema foi construído segundo o padrão **MVC (Model-View-Controller)**,
utilizando o framework Spring Boot. A aplicação é um monólito com
renderização de páginas realizada no próprio servidor (*server-side
rendering*), por meio do motor de templates Thymeleaf — não há uma API
separada consumida por um front-end independente (como uma SPA em React ou
Angular).

### Organização das camadas do sistema

O código-fonte é dividido nos seguintes pacotes:

| Pacote | Responsabilidade |
|---|---|
| `controller` | Recebe as requisições HTTP, delega a lógica ao `service` e define qual página (view) deve ser exibida. Não contém regra de negócio. |
| `service` | Concentra as regras de negócio: cálculo de progresso do questionário/quiz, cálculo de pontuação, geração do arquivo CSV, agregação de estatísticas. |
| `repository` | Interfaces de acesso a dados, usando Spring Data JPA. |
| `entity` | Classes mapeadas para as tabelas do banco de dados (JPA/Hibernate). |
| `dto` | Objetos de transferência de dados entre as camadas — tanto de entrada (formulários) quanto de saída (dados exibidos no painel administrativo). |
| `mapper` | Conversão entre o formulário de cadastro e a entidade correspondente. |
| `config` | Configurações transversais da aplicação, como o interceptador de sessão que protege as etapas da oficina. |

Além disso, as rotas foram separadas por área de uso: `/oficina/**` para o
fluxo do participante (idoso) e `/admin/**` para o painel do facilitador,
sem que uma dependa da outra.

**Observação sobre o planejamento inicial:** os pacotes `validation`,
`exception` e `util`, previstos na estrutura original do projeto, não
foram criados como camadas dedicadas. A validação de formulários é feita
diretamente sobre os DTOs por meio de anotações do Bean Validation, e o
tratamento de erros utiliza o mecanismo padrão do Spring Boot (uma página
de erro personalizada), sem lógica customizada suficiente para justificar
uma camada própria.

---

## 2. Tecnologias utilizadas

### Frontend

- HTML5 e CSS3 (folha de estilo própria do projeto, complementando o
  framework de UI).
- Bootstrap 5.3, incluindo o pacote de ícones Bootstrap Icons.
- Thymeleaf, como motor de templates server-side.

### Backend

- Java 21.
- Spring Boot 3.3, com os módulos Spring MVC, Spring Data JPA e Spring
  Validation (Bean Validation).

### Banco de Dados

- PostgreSQL 16.
- Flyway, para versionamento e migração automática do esquema do banco.

### Ferramentas de desenvolvimento

- Maven, para build e gerenciamento de dependências.
- Docker e Docker Compose, para o ambiente de banco de dados local e para
  o empacotamento da aplicação.
- asdf, para fixar a versão exata de Java e Maven usada no projeto
  (arquivo `.tool-versions`).
- Git, para controle de versão.

### Frameworks

- Spring Boot (Spring MVC, Spring Data JPA, Spring Validation).
- Bootstrap.

### Bibliotecas relevantes

- WebJars e `webjars-locator-core`: permitem que o Bootstrap e o
  Bootstrap Icons sejam distribuídos como dependências do próprio backend
  (embutidos no `.jar`), em vez de carregados de um serviço externo
  (CDN).
- JUnit 5, Mockito e AssertJ (via `spring-boot-starter-test`): testes
  unitários e de camada web.
- Testcontainers: testes de integração executados contra um banco de
  dados PostgreSQL real, iniciado automaticamente em um contêiner Docker
  durante a execução dos testes.

---

## 3. Justificativa técnica

**Java e Spring Boot** foram escolhidos por serem tecnologias amplamente
utilizadas no mercado de trabalho de Engenharia de Software, com
documentação extensa e grande produtividade para um projeto de escopo
acadêmico, sem abrir mão de práticas profissionais (camadas bem definidas,
testes automatizados, versionamento de banco de dados).

**Thymeleaf com renderização no servidor**, em vez de uma aplicação
front-end separada consumindo uma API, reduz a complexidade de manutenção:
existe um único projeto, uma única linguagem no backend, e a aplicação
depende menos de JavaScript no navegador do usuário final. Essa escolha é
especialmente relevante considerando que o público final (pessoas idosas)
pode utilizar dispositivos e navegadores mais simples.

**Bootstrap distribuído via WebJars, sem uso de CDN**, garante que a
interface continue funcionando mesmo que a conexão de internet do local da
oficina esteja instável no momento do uso — uma preocupação concreta, já
que a aplicação é pensada para ser usada durante encontros presenciais na
paróquia.

**PostgreSQL com Flyway** oferece um banco de dados relacional maduro e
gratuito, adequado ao volume de dados de uma oficina (algumas dezenas de
participantes por edição). O Flyway garante que o esquema do banco seja
reproduzível a partir de arquivos de migração versionados junto com o
código-fonte, evitando divergências entre o ambiente de desenvolvimento e
qualquer ambiente futuro de implantação.

**Testes automatizados (JUnit, Mockito, Testcontainers)** sustentam a
manutenibilidade do projeto ao longo do tempo, permitindo verificar que
alterações futuras não quebrem funcionalidades já entregues. O
Testcontainers foi escolhido especificamente para o teste de integração
porque valida o comportamento da aplicação contra o mesmo tipo de banco de
dados usado em produção (PostgreSQL), evitando o uso de um banco diferente
(como H2) que poderia mascarar problemas específicos do PostgreSQL.

**Docker e Docker Compose** adequam o projeto tanto à entrega como
portfólio quanto a uma eventual implantação em servidor, permitindo que
qualquer pessoa execute a aplicação sem precisar instalar manualmente Java,
Maven ou PostgreSQL.

**Simplicidade deliberada:** em pontos específicos, optou-se por não
adicionar bibliotecas quando a necessidade não justificava a complexidade
extra — por exemplo, a conversão entre o formulário de cadastro e a
entidade correspondente foi implementada manualmente em uma classe simples
(em vez de uma biblioteca de geração automática de código), e o conteúdo
dos materiais educativos foi implementado como texto fixo no código-fonte,
em vez de uma tabela de banco de dados editável, já que não há requisito
de edição desse conteúdo pela interface.

---

## 4. Estrutura do banco de dados

O esquema é criado e evoluído por cinco arquivos de migração do Flyway,
aplicados automaticamente quando a aplicação é iniciada. As principais
entidades são:

- **Participant**: dados do participante cadastrado (nome completo, faixa
  etária, telefone opcional e data de cadastro). É a entidade central,
  referenciada pelas respostas do questionário inicial e do quiz.
- **InitialQuestion / InitialQuestionOption**: perguntas fixas e suas
  respectivas alternativas do questionário diagnóstico inicial, que não
  possui resposta certa ou errada.
- **InitialAnswer**: resposta de um participante a uma pergunta do
  questionário inicial.
- **QuizQuestion / QuizOption**: perguntas fixas e alternativas do quiz de
  fixação, com indicação de qual alternativa é a correta.
- **QuizAnswer**: resposta de um participante a uma pergunta do quiz.
- **QuizResult**: resultado consolidado do quiz de um participante
  (pontuação obtida e total de perguntas), calculado uma única vez, no
  momento em que o participante conclui a última pergunta.

O modelo de dados completo, incluindo diagrama, está detalhado em
[`docs/03-modelo-dados.md`](03-modelo-dados.md).

---

## 5. Funcionalidades implementadas

- Cadastro de participante (nome completo, faixa etária, telefone
  opcional).
- Questionário inicial diagnóstico, com cinco perguntas fixas de múltipla
  escolha, sem certo ou errado, respondidas uma por tela.
- Materiais educativos sobre segurança digital, organizados em seis
  temas: o que são golpes, links falsos, PIX seguro, WhatsApp, senhas
  seguras e privacidade.
- Quiz de fixação, com cinco perguntas de múltipla escolha, correção
  automática e cálculo de pontuação.
- Tela de resultado individual do quiz, exibida ao participante ao final
  da oficina, em tom positivo e incentivador.
- Painel administrativo, voltado ao facilitador da oficina, com:
  - listagem de todos os participantes cadastrados;
  - estatísticas agregadas (total de participantes, quantidade que
    concluiu o quiz, percentual médio de acerto);
  - visualização detalhada das respostas de cada participante,
    individualmente;
  - exportação dos dados dos participantes em arquivo CSV, compatível com
    a abertura direta no Excel em português do Brasil.

---

## 6. Recursos de acessibilidade

Considerando que o público principal do sistema são pessoas idosas, as
seguintes decisões foram tomadas ao longo de todo o desenvolvimento, e não
apenas como um ajuste final:

- **Tipografia e tamanho dos elementos**: fontes maiores que o padrão do
  navegador em toda a aplicação, e botões grandes com boa área de toque,
  adequados a telas sensíveis ao toque e a usuários com menor destreza
  motora.
- **Contraste de cores**: a paleta de cores adotada tem contraste
  calculado de aproximadamente 5,7:1 entre a cor principal do sistema e o
  texto branco exibido sobre ela — acima do mínimo de 4,5:1 recomendado
  pelas diretrizes de acessibilidade (WCAG) para leitura confortável.
- **Navegação em formato de assistente**: o fluxo do participante é
  dividido em etapas lineares (uma decisão por tela, sem menus), com um
  indicador de progresso visual (barra) e textual ("Passo X de 4"), para
  que o usuário sempre saiba em que ponto da oficina está.
- **Seleção de respostas por botões grandes**, em vez de campos de texto
  livre ou listas suspensas, reduzindo a necessidade de digitação.
- **Agrupamento semântico das opções de resposta** (uso das tags HTML
  `fieldset`/`legend` e de atributos de acessibilidade — ARIA), para que
  leitores de tela consigam anunciar corretamente o propósito de cada
  grupo de botões.
- **Mensagens de erro em linguagem simples e direta**, associadas
  visualmente (borda destacada no campo) e programaticamente (para
  leitores de tela) ao campo correspondente do formulário.
- **Página de erro amigável**, sem termos técnicos, orientando o
  participante a buscar ajuda do responsável pela oficina em caso de
  problema.

---

## 7. Utilização de Inteligência Artificial

O desenvolvimento deste projeto contou com o apoio da ferramenta **Claude
Code**, um assistente de programação baseado em inteligência artificial
desenvolvido pela Anthropic, utilizado sob supervisão direta e contínua do
aluno responsável pelo projeto, do início ao fim do desenvolvimento.

**Papel desempenhado pela ferramenta:**

- Apoio na elaboração dos documentos de planejamento (levantamento de
  requisitos, definição de arquitetura, modelagem do banco de dados,
  elaboração de casos de uso e wireframes), sempre apresentados para
  aprovação antes de qualquer linha de código ser escrita.
- Geração de código Java, templates HTML e scripts de banco de dados,
  seguindo diretrizes de arquitetura, qualidade e acessibilidade definidas
  explicitamente pelo aluno no início do projeto.
- Sugestão e explicação de decisões técnicas (por exemplo, o uso de
  Testcontainers em vez de um banco de dados de testes diferente do de
  produção, ou a separação de rotas entre o fluxo do participante e o
  painel administrativo), sempre acompanhadas da justificativa
  correspondente.
- Execução de builds e da suíte de testes automatizados para verificação
  técnica de cada entrega, incluindo a identificação e a correção de uma
  falha real encontrada durante o desenvolvimento (cálculo prematuro da
  pontuação do quiz quando acessado antes do término).

**Papel desempenhado pelo aluno:**

- Definição dos requisitos, do escopo e das prioridades do projeto.
- Aprovação de cada etapa de planejamento antes da implementação, e de
  cada funcionalidade antes de avançar para a próxima.
- Validação funcional de cada entrega em ambiente local, incluindo a
  execução da aplicação, do banco de dados e dos contêineres Docker na
  própria máquina.
- Tomada de decisão nos pontos em que havia mais de uma alternativa
  técnica razoável apresentada pela ferramenta (por exemplo, definir se o
  próprio idoso preencheria os formulários ou se haveria mediação de um
  facilitador, ou como proceder diante de uma falha específica do
  ambiente local de testes).
- Revisão final e aceite do resultado de cada etapa do desenvolvimento.

Em nenhum momento a ferramenta tomou decisões de negócio, de escopo ou de
arquitetura de forma autônoma: todas as sugestões técnicas apresentadas
pelo Claude Code foram avaliadas e aprovadas pelo aluno antes de sua
efetivação no projeto.

---

## 8. Conclusão técnica

A solução implementada atende aos objetivos da Atividade Extensionista ao
entregar uma aplicação web funcional, simples e acessível, capaz de apoiar
oficinas presenciais de inclusão digital e segurança online para o grupo
de convivência da Paróquia Bom Jesus dos Migrantes, em Sobradinho-DF. A
arquitetura MVC em camadas, com renderização de páginas no servidor e uso
comedido de dependências externas, resultou em um sistema de manutenção
simples e compatível com o escopo de um projeto acadêmico, ainda que
estruturado segundo boas práticas de mercado — versionamento controlado do
banco de dados, testes automatizados em múltiplos níveis e containerização
com Docker.

As decisões de acessibilidade tomadas em cada etapa do desenvolvimento —
da tipografia à navegação em formato de assistente — refletem o cuidado
com o público-alvo prioritário do projeto: pessoas idosas, muitas vezes
com pouca familiaridade com tecnologia. O sistema cobre de ponta a ponta o
ciclo idealizado para a oficina: cadastro, diagnóstico inicial, conteúdo
educativo, avaliação de fixação e, por fim, um painel que permite ao
facilitador acompanhar e analisar os resultados obtidos.

O uso do Claude Code como ferramenta de apoio permitiu acelerar tarefas de
implementação e documentação ao longo de todas as etapas do projeto, sem
substituir o julgamento técnico e as decisões finais, que permaneceram sob
responsabilidade do aluno, responsável por validar, testar e aprovar cada
entrega antes de avançar para a etapa seguinte.

---

## Tecnologias não utilizadas em relação ao planejamento inicial

Para transparência, ficam registradas as tecnologias inicialmente
mencionadas no planejamento do projeto que não chegaram a ser utilizadas,
e a alternativa efetivamente adotada em cada caso:

- **JavaScript customizado**: previsto como parte do frontend no
  planejamento inicial, mas não foi necessário escrever nenhuma linha de
  JavaScript própria do projeto. O único script carregado pelas páginas é
  o pacote já compilado do próprio Bootstrap, usado apenas para o
  comportamento padrão de seus componentes visuais. Toda a interação do
  usuário (avançar etapas, validar formulários) é resolvida por
  navegação de página comum (requisições HTTP tradicionais), sem
  necessidade de JavaScript adicional.
- **Autenticação (Spring Security)**: prevista no planejamento como algo
  a preparar para o futuro, mas não implementada nesta fase do projeto.
  A separação das rotas entre `/oficina/**` (participante) e `/admin/**`
  (facilitador) foi feita antecipando essa necessidade, mas atualmente
  não há login nem controle de acesso — qualquer pessoa com acesso ao
  endereço da aplicação pode utilizá-la.
- **Biblioteca de mapeamento automático (ex.: MapStruct)**: considerada
  durante o planejamento para a conversão entre formulários e entidades,
  mas substituída por uma classe de conversão escrita manualmente, dado o
  pequeno número de campos envolvidos e para evitar uma dependência
  adicional sem benefício claro nesta escala.

---

## Versão resumida (seção "Tecnologias Utilizadas" do relatório)

> O sistema foi desenvolvido em Java 21 com o framework Spring Boot 3,
> seguindo o padrão arquitetural MVC em camadas (controller, service,
> repository, entity, dto, mapper e config), com renderização de páginas
> no servidor por meio do motor de templates Thymeleaf. O frontend
> utiliza HTML5, CSS3 e o framework Bootstrap 5, incluindo o pacote de
> ícones Bootstrap Icons, ambos incorporados ao próprio backend por meio
> de WebJars, sem depender de serviços externos (CDN) durante o uso da
> aplicação. O armazenamento de dados é feito em banco de dados
> relacional PostgreSQL, com o esquema versionado e migrado
> automaticamente pela ferramenta Flyway. O build e o gerenciamento de
> dependências são feitos com Maven, e a aplicação está containerizada
> com Docker e Docker Compose, tanto para o ambiente de desenvolvimento
> quanto para uma futura implantação. A qualidade do código é verificada
> por testes automatizados com JUnit 5, Mockito e Testcontainers, este
> último responsável por validar o funcionamento da aplicação contra um
> banco de dados PostgreSQL real, executado automaticamente em um
> contêiner Docker.
