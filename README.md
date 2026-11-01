# MsSystems

**Sistema de Controle de Recepção e Armazenagem de Matérias-Primas**

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue)
![Flyway](https://img.shields.io/badge/Flyway-migrations-red)
![Docker](https://img.shields.io/badge/Docker-compose-2496ED)
![Maven](https://img.shields.io/badge/Maven-wrapper-C71A36)
![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)

O **MsSystems** é um sistema integrado para o controle de entradas, inspeção de qualidade, pesagem, descarregamento, gestão de lotes internos e alocação em armazéns/silos de matérias-primas provenientes de fornecedores.

> 🚧 Projeto em desenvolvimento. O ambiente base (Spring Boot, PostgreSQL via Docker, Flyway e perfis) já está configurado e os módulos descritos abaixo estão sendo implementados conforme a especificação de requisitos.

---

## 📋 Sumário

- [Funcionalidades](#-funcionalidades)
- [Fluxo da operação](#-fluxo-da-operação)
- [Modelo de domínio](#-modelo-de-domínio)
- [Tecnologias](#-tecnologias)
- [Pré-requisitos](#-pré-requisitos)
- [Como executar](#-como-executar)
- [Variáveis de ambiente](#-variáveis-de-ambiente)
- [Banco de dados e migrações](#-banco-de-dados-e-migrações)
- [Estrutura do projeto](#-estrutura-do-projeto)
- [Testes](#-testes)
- [Requisitos não funcionais](#-requisitos-não-funcionais)
- [Fora do escopo (fase atual)](#-fora-do-escopo-fase-atual)
- [Padrão de commits](#-padrão-de-commits)
- [Roadmap](#-roadmap)
- [Solução de problemas](#-solução-de-problemas)
- [Autor](#-autor)

---

## ✅ Funcionalidades

### RF01 – Recepção e Portaria
- Registro de chegada: placa do caminhão, nome do motorista, transportadora, nota fiscal, data/hora de entrada, fornecedor de origem e estado inicial.
- Pesagem inicial (**peso bruto**) e final (**peso tara**).
- Cálculo automático do **peso líquido** (`calcularPesoLiquido()` = peso bruto − peso tara).
- Controle do ciclo de vida da recepção via enum `StatusRecepcao`:
  `AGUARDANDO_INSPECAO`, `APROVADO_QUALIDADE`, `DESCARREGADO`, `REJEITADO`.

### RF02 – Controle de Qualidade e Inspeção
- Identificação obrigatória do operador responsável pela recepção/qualidade e do operador de empilhador.
- Registro de aprovação ou rejeição da carga via `aprovarInspecao()` enquanto o motorista aguarda no pátio.

### RF03 – Descarga e Lotes Internos
- Descarregamento físico realizado pelo operador de empilhador após a aprovação da qualidade.
- Geração automática do **lote interno** após a descarga, com código de rastreabilidade único (`gerarCodigoRastreabilidade()`) e prefixo conforme o tipo de matéria-prima:

  | Prefixo | Categoria                 |
  |---------|---------------------------|
  | `OA` | Origem Animal             |
  | `OV` | Origem Vegetal            |
  | `MI` | Mineral / Micro-Nutriente |

- Atributos do lote: quantidade inicial, saldo atual, data de fabricação, data de validade, matéria-prima e status (`StatusLote`).
- Estados do lote e cores no painel:

  | Status | Cor |
  |--------|-----|
  | `BLOQUEADO` | 🔴 Vermelho |
  | `AGUARDANDO_USO` | 🟡 Amarelo |
  | `EM_USO` | 🟢 Verde |
  | `ESGOTADO` | 🔵 Azul claro |

### RF04 – Locais de Armazenamento
- Cadastro de armazéns e silos com rua, posição e capacidade máxima.
- Validação de capacidade com `verificarEspacoDisponivel()` antes da alocação do lote (`movimentarPara(LocalArmazenagem)`).

### RF05 – Matérias-Primas e Categorias
- Cadastro com nome, fornecedor, descrição e categoria (`CategoriaMateriaPrima`):
  `ORIGEM_ANIMAL_OA`, `ORIGEM_VEGETAL_OV`, `MINERAL_MICRO_MI`.

### RF06 – Dashboard
- Painel centralizado, em tempo real, com código do lote (com prefixo), matéria-prima, saldo atual, localização e estado com a cor correspondente.

### RF07 – Controle de Inventário
- Registro de entradas de estoque e consumo manual de saldo via `consumirSaldo(BigDecimal quantidade)`.

---

## 🔄 Fluxo da operação

```
Chegada do caminhão
        │
        ▼
Pesagem inicial (peso bruto)
        │
        ▼
Inspeção de qualidade ──► REJEITADO
        │
        ▼ APROVADO_QUALIDADE
Descarregamento (operador de empilhador)
        │
        ▼
Pesagem final (peso tara) → cálculo do peso líquido
        │
        ▼
Geração do lote interno + código de rastreabilidade
        │
        ▼
Alocação em armazém/silo (verificação de capacidade)
        │
        ▼
Dashboard + consumo de saldo
```

---

## 🧩 Modelo de domínio

Principais conceitos previstos na especificação:

- **Recepcao** — dados da chegada, pesagens e `StatusRecepcao`
- **Lote** — lote interno rastreável, com saldo e `StatusLote`
- **MateriaPrima** — material recebido, com `CategoriaMateriaPrima`
- **Fornecedor** — origem das matérias-primas e das cargas recebidas
- **LocalArmazenagem** — armazém/silo com rua, posição e capacidade máxima
- **Operadores / perfis** — Operador de Portaria, Qualidade e Operador de Empilhador

---

## 🛠 Tecnologias

- **Java 21**
- **Spring Boot 4.1.1**
  - Spring Web MVC
  - Spring Data JPA
  - Spring Security
  - Bean Validation
  - Actuator (endpoints `health` e `info`)
  - Spring Boot DevTools
- **PostgreSQL 17** (via Docker Compose)
- **Flyway** (versionamento do schema)
- **Maven** (via Maven Wrapper)
- **JUnit 5** / Spring Boot Test

---

## 📦 Pré-requisitos

- [JDK 21](https://adoptium.net/)
- [Docker](https://www.docker.com/) com Docker Compose
- Git
- (Opcional) IntelliJ IDEA e pgAdmin 4

> Não é necessário instalar o Maven nem o PostgreSQL: o projeto inclui o Maven Wrapper (`mvnw` / `mvnw.cmd`) e o banco sobe em container.

---

## ▶️ Como executar

### 1. Clonar o repositório

```bash
git clone https://github.com/MatheuSouz4/MatheuSouz4-MsSystems-Recepcao.Armazenagem-Backend
cd <nome-do-repositorio>
```

### 2. Configurar as variáveis de ambiente

Copie o arquivo de exemplo e preencha com seus valores:

```bash
cp .env.example .env        # Linux/macOS
copy .env.example .env      # Windows (cmd)
```

Edite o `.env` (veja [Variáveis de ambiente](#-variáveis-de-ambiente)). **Nunca versione o `.env`.**

> ⚠️ Se a senha contiver o caractere `$,!,#,&,*`, coloque o valor entre aspas simples, por exemplo `DB_PASSWORD='minha$senha'`, para que o Docker Compose não o interprete como variável.

### 3. Subir o banco de dados

```bash
docker compose up -d
docker ps                   # o container mssystems-db deve ficar "healthy"
```

### 4. Executar a aplicação

O Spring Boot **não lê o arquivo `.env` sozinho**. Escolha uma das opções:

**IntelliJ IDEA:** em *Run → Edit Configurations*, na configuração da aplicação, carregue o `.env` em *Environment variables* (ou use o plugin EnvFile) e execute.

**Linha de comando (PowerShell):**

```powershell
$env:DB_USER="mssystems"; $env:DB_PASSWORD="sua_senha"; $env:DB_PORT="5433"; $env:DB_NAME="mssystems_recepcao_armazenagem"
./mvnw spring-boot:run
```

**Linha de comando (Linux/macOS):**

```bash
export $(grep -v '^#' .env | xargs)
./mvnw spring-boot:run
```

Ao iniciar, o Flyway aplica as migrações automaticamente. O Hibernate roda com `ddl-auto: validate`, então o schema deve estar sempre alinhado às migrações.

### 5. Verificar

```bash
curl http://localhost:8080/actuator/health
```

Resposta esperada: `{"status":"UP"}`.

---

## 🔐 Variáveis de ambiente

| Variável      | Descrição                            | Exemplo                          |
|---------------|--------------------------------------|----------------------------------|
| `DB_NAME`     | Nome do banco de dados               | `mssystems_recepcao_armazenagem` |
| `DB_USER`     | Usuário do PostgreSQL                | `mssystems`                      |
| `DB_PASSWORD` | Senha do PostgreSQL (obrigatória)    | *definir localmente*             |
| `DB_PORT`     | Porta publicada no host para o banco | `5433`                           |
| `DB_URL`      | (Opcional) URL JDBC completa         | `jdbc:postgresql://localhost:5433/mssystems_recepcao_armazenagem` |

O perfil padrão é `dev` (`application-dev.yml`).

> ⚠️ Não versione credenciais reais. Apenas o `.env.example` (com valores fictícios) vai para o repositório.

---

## 🗄 Banco de dados e migrações

- O schema é versionado com **Flyway**, em `src/main/resources/db/migration`.
- Nomeie os arquivos como `V<numero>__<descricao>.sql` (ex.: `V2__criar_tabela_materia_prima.sql`).
- **Não edite** uma migração já aplicada; crie uma nova versão.
- O Hibernate não cria nem altera tabelas (`ddl-auto: validate`); ele apenas valida o mapeamento contra o schema.

Para recriar o banco local do zero (**apaga todos os dados**):

```bash
docker compose down -v
docker compose up -d
```

---

## 📁 Estrutura do projeto

```
.
├── .mvn/wrapper/               # Configuração do Maven Wrapper
├── docker-compose.yml          # PostgreSQL 17 (container mssystems-db)
├── .env.example                # Modelo das variáveis de ambiente
├── pom.xml
├── mvnw / mvnw.cmd
└── src
    ├── main
    │   ├── java/br/com/mssystems
    │   │   ├── armazenagem             # Locais de armazenamento (armazém/silo)
    │   │   ├── fornecedor              # Fornecedores
    │   │   ├── materiaprima            # Matérias-primas e categorias
    │   │   ├── shared                  # Componentes compartilhados
    │   │   └── MssystemsBackendApplication.java
    │   └── resources
    │       ├── db/migration            # Migrações Flyway
    │       ├── application.yml
    │       └── application-dev.yml
    └── test
```

---

## 🧪 Testes

```bash
./mvnw test      # Linux/macOS
mvnw.cmd test    # Windows
```

---

## 🔒 Requisitos não funcionais

- **Desempenho (RNF01):** painel de lotes e operações de pesagem com resposta rápida, evitando retenções no pátio.
- **Segurança e auditoria (RNF02):** registro obrigatório do perfil de usuário (Portaria, Qualidade, Operador de Empilhador) em todas as transações críticas — pesagem, inspeção, descarga e alocação.
- **Rastreabilidade (RNF03):** cadeia completa desde a nota fiscal, motorista e fornecedor até o lote interno armazenado e seu consumo.

---

## 🚫 Fora do escopo (fase atual)

O módulo de produção automatizada (lançamento de receitas para consumo diário e em tempo real) está **excluído desta fase** e reservado para implementações futuras.

---

## 📝 Padrão de commits

O projeto segue o [Conventional Commits](https://www.conventionalcommits.org/pt-br/):

```
<tipo>(<escopo opcional>): <descrição>
```

Tipos usados: `feat`, `fix`, `docs`, `refactor`, `test`, `chore`, `build`, `ci`.

Exemplos:

```
feat(materiaprima): adicionar cadastro de matérias-primas
fix(recepcao): validar peso bruto maior que a tara
docs: atualizar README com instruções de execução
```

---

## 🗺 Roadmap

- [x] Ambiente de desenvolvimento (Docker, PostgreSQL, Flyway, perfis)
- [ ] Tratamento padronizado de erros
- [ ] Cadastros base: Fornecedor, Matéria-Prima, Local de Armazenagem
- [ ] Recepção e pesagem (peso bruto, tara e líquido)
- [ ] Inspeção de qualidade
- [ ] Descarga e geração de lote interno com código de rastreabilidade
- [ ] Alocação em armazém/silo e consumo de saldo
- [ ] Dashboard de lotes
- [ ] Segurança por perfil (Portaria, Qualidade, Empilhador) e auditoria
- [ ] Frontend em Angular

---

## 🩺 Solução de problemas

**`password authentication failed for user "mssystems"`**
- Confirme que `DB_PASSWORD` chegou à aplicação (o Spring não lê `.env` sozinho).
- O Postgres só aplica `POSTGRES_PASSWORD` na primeira criação do volume. Para trocar a senha, recrie o volume: `docker compose down -v && docker compose up -d`.
- Verifique se outra instância do PostgreSQL (por exemplo, instalada no Windows) não está usando a mesma porta: `netstat -ano | findstr :5433`.

**Container não fica `healthy`**
- Veja os logs: `docker logs mssystems-db`.

---

## 👤 Autor

**Matheus de Souza Gonçalves**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-matheusouz4-blue?logo=linkedin)](https://linkedin.com/in/matheusouz4)


