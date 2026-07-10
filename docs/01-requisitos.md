# Requisitos

## Contexto

Aplicação de apoio a oficinas presenciais de inclusão digital e segurança online,
realizadas com o grupo de convivência de idosos da Paróquia Bom Jesus dos
Migrantes (Sobradinho - DF).

**Decisão de uso confirmada:** o próprio idoso preenche cadastro, questionário
inicial e quiz, em um dispositivo (tablet/notebook) durante a oficina, sem
mediação obrigatória de um facilitador digitando por ele. Isso torna a
simplicidade de interface o requisito não funcional mais crítico do projeto.

**Decisão de modelagem confirmada:** a oficina é tratada como evento único —
não há entidade "Oficina/Turma" nem rastreamento de múltiplas edições no MVP.

---

## Requisitos Funcionais (RF)

| ID | Requisito | Observação |
|----|-----------|------------|
| RF01 | Cadastrar participante (nome completo, faixa etária, contato opcional) | Campos mínimos, sem digitação de dados sensíveis (sem CPF/endereço) |
| RF02 | Listar participantes cadastrados | Uso do facilitador/administrador, não do idoso |
| RF03 | Aplicar questionário inicial de diagnóstico (perguntas de múltipla escolha) | Sem "resposta certa/errada" |
| RF04 | Persistir respostas do questionário inicial vinculadas ao participante | |
| RF05 | Disponibilizar materiais educativos por tema (golpes, links falsos, PIX, WhatsApp, senhas, privacidade) | Conteúdo fixo, não editável via sistema no MVP |
| RF06 | Aplicar quiz de múltipla escolha ao final da oficina | |
| RF07 | Corrigir o quiz automaticamente e calcular pontuação | |
| RF08 | Registrar resultado do quiz (pontuação e respostas) vinculado ao participante | |
| RF09 | Exibir estatísticas agregadas (nº de participantes, taxa média de acerto, distribuição de respostas) | Uso do facilitador/administrador |
| RF10 | Exportar participantes e resultados em CSV | Uso do facilitador/administrador |

## Requisitos Não Funcionais (RNF)

| ID | Requisito |
|----|-----------|
| RNF01 | Interface com fontes grandes, alto contraste, botões grandes e linguagem simples — usabilidade para idosos é prioridade sobre qualquer outra preocupação de design |
| RNF02 | Fluxo do participante organizado como assistente (wizard) linear, uma ação por tela, sem navegação lateral/menus complexos |
| RNF03 | Minimizar digitação de texto livre; preferir seleção por botões/opções sempre que possível |
| RNF04 | Responsivo (tablet e notebook) |
| RNF05 | Validação de todas as entradas via Bean Validation |
| RNF06 | Tratamento global de exceções, com mensagens de erro em linguagem simples e não técnica para o participante |
| RNF07 | Nenhuma senha em texto plano; estrutura de `config` pronta para autenticação futura (aplicável apenas às rotas administrativas) |
| RNF08 | Minimização de dados pessoais coletados (compatível com principios da LGPD) |
| RNF09 | Código em inglês, Clean Code, SOLID, arquitetura MVC em camadas |
| RNF10 | Testes unitários por camada de serviço, incrementais a cada entrega (não concentrados apenas no Epic de finalização) |
| RNF11 | Aplicação pronta para empacotamento via Docker em ambiente Linux |

## Fora de escopo do MVP

- Autenticação/login (apenas a estrutura é preparada).
- Múltiplas oficinas/turmas com histórico comparativo.
- Edição de conteúdo educativo via interface administrativa.
- Aplicativo mobile nativo.
