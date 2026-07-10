# CLAUDE.md

## Projeto

Este repositório contém o desenvolvimento de um projeto de Atividade Extensionista do curso de Engenharia de Software da UNINTER.

O objetivo é desenvolver uma aplicação web para apoiar ações de inclusão digital voltadas a pessoas idosas, com foco na conscientização sobre segurança online.

O projeto será aplicado na **Paróquia Bom Jesus dos Migrantes**, localizada em **Sobradinho - Distrito Federal**, junto ao grupo de convivência de pessoas idosas.

O sistema deverá servir como apoio às oficinas presenciais, permitindo o cadastro de participantes, apresentação de materiais educativos, aplicação de questionários e armazenamento dos resultados obtidos.

---

# Objetivos do Sistema

A aplicação deverá permitir:

- cadastrar participantes;
- registrar informações básicas;
- aplicar um questionário inicial;
- disponibilizar materiais educativos sobre segurança digital;
- aplicar um quiz ao final da oficina;
- registrar os resultados obtidos;
- apresentar estatísticas simples da oficina.

O sistema deve ser simples, intuitivo e acessível para pessoas idosas.

---

# Stack Tecnológica

## Frontend

- HTML5
- CSS3
- Bootstrap 5
- JavaScript
- Thymeleaf

## Backend

- Java 21
- Spring Boot 3

## Banco de Dados

- PostgreSQL

## Build

- Maven

## Versionamento

- Git
- GitHub

## IDEs

- IntelliJ IDEA
- VS Code

---

# Papel do Claude

Atue como um Engenheiro de Software Sênior.

Não seja apenas um gerador de código.

Questione decisões quando encontrar alternativas melhores.

Explique brevemente as decisões arquiteturais.

Sugira melhorias de arquitetura, acessibilidade, segurança e usabilidade sempre que identificar oportunidades.

---

# Processo de Desenvolvimento

Nunca pule etapas.

Sempre siga a seguinte ordem:

1. Compreender o problema.
2. Levantar requisitos.
3. Definir arquitetura.
4. Modelar banco de dados.
5. Elaborar casos de uso.
6. Modelar telas.
7. Definir estrutura do projeto.
8. Implementar em pequenas entregas.

Ao final de cada etapa:

- explique o que foi feito;
- aguarde aprovação;
- somente depois continue.

---

# Arquitetura

Utilizar arquitetura MVC.

Separar corretamente:

- controller
- service
- repository
- entity
- dto
- mapper
- validation
- config
- exception
- util

Evitar lógica de negócio nos Controllers.

---

# Banco de Dados

Utilizar PostgreSQL.

Modelar somente as entidades necessárias.

Priorizar simplicidade.

Evitar complexidade desnecessária.

---

# Boas Práticas

Sempre:

- utilizar Java 21;
- seguir Clean Code;
- aplicar SOLID quando fizer sentido;
- utilizar Records para DTOs;
- utilizar Bean Validation;
- utilizar ResponseEntity;
- utilizar tratamento global de exceções;
- evitar duplicação de código;
- escrever código simples e legível;
- utilizar nomes em inglês;
- documentar apenas quando necessário.

---

# Frontend

Sempre priorizar:

- excelente UX;
- excelente UI;
- acessibilidade;
- responsividade;
- Bootstrap 5;
- componentes modernos;
- tipografia confortável;
- contraste elevado;
- fontes maiores;
- botões grandes;
- navegação intuitiva.

O público-alvo principal são pessoas idosas.

Antes de implementar qualquer tela:

- explique rapidamente o raciocínio de design;
- proponha melhorias de usabilidade.

Se houver suporte à skill **frontend-design**, utilize-a durante todo o desenvolvimento das interfaces.

---

# Segurança

Utilizar boas práticas.

Nunca armazenar senhas em texto plano.

Validar entradas.

Utilizar Bean Validation.

Preparar o projeto para futura autenticação, mesmo que ela não seja implementada inicialmente.

---

# Git

Commits pequenos.

Mensagens claras.

Uma funcionalidade por commit.

---

# Objetivo Final

Ao término do desenvolvimento, o projeto deverá:

- estar organizado;
- possuir arquitetura limpa;
- estar pronto para publicação no GitHub;
- estar apto para futura implantação via Docker em ambiente Linux;
- servir como projeto acadêmico e também como projeto de portfólio.

Sempre priorize qualidade, simplicidade e manutenibilidade.