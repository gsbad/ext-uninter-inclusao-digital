# Wireframes (baixa fidelidade)

## Raciocínio de design

O público-alvo é idoso, muitas vezes com pouca familiaridade com telas
touch. Princípios aplicados em todas as telas do fluxo do participante:

- Uma única ação por tela (nunca formulários com múltiplos campos).
- Botões grandes (mínimo ~48px de altura, texto ≥18px), com espaçamento
  generoso entre opções para evitar toques acidentais.
- Progresso sempre visível ("Passo 2 de 4"), para reduzir ansiedade sobre
  "quanto falta".
- Sem menus de navegação lateral, sem breadcrumbs, sem múltiplos caminhos
  possíveis — o idoso nunca precisa decidir "para onde ir", só "Próximo"
  ou "Voltar".
- Confirmações explícitas e em tom positivo após cada envio.

O painel administrativo (facilitador) segue os padrões usuais de Bootstrap
5 (tabelas, cards de estatística), pois seu usuário não tem as mesmas
restrições de acessibilidade motora/cognitiva do idoso — mas ainda com
contraste elevado e tipografia legível, por ser também usado em ambiente
com iluminação variável (salão paroquial).

---

## Tela 1 — Boas-vindas

```
┌─────────────────────────────────────────┐
│               [Logo/Título]              │
│                                           │
│   Bem-vindo à Oficina de Segurança       │
│   Digital!                               │
│                                           │
│   ┌───────────────────────────────────┐  │
│   │        COMEÇAR                    │  │
│   └───────────────────────────────────┘  │
└─────────────────────────────────────────┘
```

## Tela 2 — Cadastro (Passo 1 de 4)

```
┌─────────────────────────────────────────┐
│  ●───○───○───○   Passo 1 de 4            │
│                                           │
│   Qual é o seu nome?                     │
│   ┌───────────────────────────────────┐  │
│   │ ________________________________  │  │
│   └───────────────────────────────────┘  │
│                                           │
│   Qual é a sua faixa de idade?           │
│   [ 60-69 ]  [ 70-79 ]  [ 80+ ]           │
│                                           │
│              ┌───────────┐               │
│              │  PRÓXIMO  │               │
│              └───────────┘               │
└─────────────────────────────────────────┘
```

## Tela 3 — Questionário Inicial (Passo 2 de 4, uma pergunta por tela)

```
┌─────────────────────────────────────────┐
│  ●───●───○───○   Passo 2 de 4            │
│                                           │
│   Você costuma usar o WhatsApp?          │
│                                           │
│   ┌───────────────────────────────────┐  │
│   │             Sim                    │  │
│   └───────────────────────────────────┘  │
│   ┌───────────────────────────────────┐  │
│   │             Não                    │  │
│   └───────────────────────────────────┘  │
│   ┌───────────────────────────────────┐  │
│   │          Às vezes                  │  │
│   └───────────────────────────────────┘  │
│                                           │
│   [ VOLTAR ]              [ PRÓXIMO ]    │
└─────────────────────────────────────────┘
```

## Tela 4 — Material Educativo (Passo 3 de 4)

```
┌─────────────────────────────────────────┐
│  ●───●───●───○   Passo 3 de 4            │
│                                           │
│   Vamos aprender um pouco?               │
│                                           │
│   [ O que são golpes? ]                  │
│   [ Links falsos       ]                 │
│   [ PIX seguro         ]                 │
│   [ WhatsApp           ]                 │
│   [ Senhas seguras     ]                 │
│   [ Privacidade        ]                 │
│                                           │
│              ┌───────────────┐           │
│              │ IR PARA O QUIZ│           │
│              └───────────────┘           │
└─────────────────────────────────────────┘
```

Cada tema abre uma página com texto curto, ilustração e botão "Voltar aos
temas" — sem necessidade de wireframe adicional (conteúdo estático).

## Tela 5 — Quiz (Passo 4 de 4, uma pergunta por tela)

Mesmo padrão visual da Tela 3, com indicador "Pergunta 1 de N" no lugar do
step global.

## Tela 6 — Resultado do Quiz

```
┌─────────────────────────────────────────┐
│                                           │
│         Parabéns por participar!         │
│                                           │
│      Você acertou 4 de 5 perguntas       │
│                                           │
│   Continue praticando o que aprendeu     │
│   hoje para navegar com mais segurança.  │
│                                           │
│              ┌───────────┐               │
│              │  CONCLUIR │               │
│              └───────────┘               │
└─────────────────────────────────────────┘
```

## Tela 7 — Painel Administrativo (Facilitador)

```
┌─────────────────────────────────────────────────────┐
│  Oficina de Segurança Digital — Painel               │
├─────────────────────────────────────────────────────┤
│  [ 32 participantes ]  [ Média quiz: 78% ]           │
│                                          [Exportar CSV]│
├─────────────────────────────────────────────────────┤
│  Nome            | Idade | Quiz  | Detalhes           │
│  Maria Silva     | 70-79 | 4/5   | [ver]               │
│  José Santos     | 60-69 | 5/5   | [ver]               │
│  ...                                                  │
└─────────────────────────────────────────────────────┘
```

## Melhorias de usabilidade propostas (para validação)

1. Botão "Voltar" sempre disponível nas telas de perguntas, para corrigir
   seleção acidental — reduz ansiedade de "errei e não posso voltar".
2. Leitura em voz alta (TTS) das perguntas é uma melhoria futura relevante
   para acessibilidade, mas fora do escopo do MVP (não listada no
   BACKLOG.md); registrar como ideia para o Epic 8 ou versão futura.
3. Confirmação visual (ex.: borda verde) imediatamente ao tocar numa opção,
   antes de avançar — feedback tátil/visual reduz a incerteza de "cliquei
   certo?".
