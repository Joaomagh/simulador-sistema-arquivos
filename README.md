Markdown:

# Simulador de Sistema de Arquivos com Journaling

## Metodologia

Este simulador foi desenvolvido em *Java* e implementa as principais operações de um sistema de arquivos, incluindo o conceito de *Journaling* para garantir a integridade dos dados. O programa processa comandos simulando as funcionalidades básicas de sistemas de arquivos e exibe os resultados na tela.

---

## Parte 1: Introdução ao Sistema de Arquivos com Journaling

### O que é um Sistema de Arquivos?
Um sistema de arquivos é uma abstração que organiza, armazena e recupera dados em dispositivos de armazenamento, como HDs e SSDs. Ele é fundamental para a manipulação eficiente e segura de informações, garantindo a persistência dos dados.

### Journaling
O *Journaling* é uma técnica utilizada para aumentar a confiabilidade do sistema de arquivos. Ele registra as operações em um log antes de aplicá-las, possibilitando a recuperação em caso de falhas.

#### Tipos de Journaling
1. *Write-ahead Logging*: Registra todas as alterações antes de aplicá-las no sistema.
2. *Log-structured Journaling*: Organiza os dados no log de forma sequencial, otimizando a escrita.
3. *Ordered Journaling*: Apenas os metadados são registrados no log, enquanto os dados são gravados diretamente.

---

## Parte 2: Arquitetura do Simulador

### Estrutura de Dados
O simulador utiliza classes Java para representar os elementos de um sistema de arquivos:
- *File*: Representa arquivos, contendo nome, conteúdo e referência ao diretório pai.
- *Directory*: Representa diretórios, contendo uma lista de filhos (arquivos e subdiretórios) e referência ao diretório pai.
- *FileSystemSimulator*: Gerencia as operações do sistema de arquivos.
- *Journal*: Gerencia o log das operações realizadas.

### Estrutura do Log
O log registra informações sobre cada operação realizada:
- *Operação*: Nome da ação executada (ex.: "Criar Diretório").
- *Timestamp*: Data e hora da operação.
- *Detalhes*: Informações adicionais, como o caminho do arquivo ou diretório.

### Recuperação após Falhas
Caso ocorra uma falha, o log é utilizado para restaurar o sistema de arquivos ao estado consistente mais recente.

---

## Parte 3: Implementação em Java

### Classe FileSystemSimulator
Esta é a classe principal do simulador. Métodos implementados:
- *createDirectory(String path)*: Cria um novo diretório.
- *copy(String sourcePath, String destinationPath)*: Copia arquivos ou diretórios.
- *rename(String oldPath, String newName)*: Renomeia arquivos ou diretórios.
- *delete(String path)*: Exclui arquivos ou diretórios.
- *listContents()*: Lista o conteúdo do diretório atual.

### Classe File
Representa um arquivo com os seguintes atributos:
- Nome.
- Conteúdo (String).
- Referência ao diretório pai.

### Classe Directory
Representa um diretório com:
- Nome.
- Lista de filhos (arquivos e subdiretórios).
- Referência ao diretório pai.

### Classe Journal
Gerencia o log de operações:
- *logOperation(String operation, String details)*: Registra uma operação no log.
- *readLog()*: Retorna as entradas do log para auditoria ou recuperação.

---

## Exemplo de Uso

### Criar Diretório
```java
create criandodiretorio


Saída:

Logged: Operation: Create Directory, Details: Path: docs
Contents of directory root:
Dir: docs

Copiar Arquivo:

copy docs backup
list

Saída:

Logged: Operation: Copy, Details: Source: docs, Destination: backup
Contents of directory root:
Dir: docs
Dir: backup

Logs de Operações
Os logs são salvos em um arquivo chamado journal.txt, permitindo auditoria das operações realizadas. Cada entrada de log contém:

Nome da operação.
Caminho ou detalhes da ação.
Timestamp indicando quando a operação foi registrada.

Como Executar o Simulador

Clone este repositório:

git clone <url_do_repositorio>

Compile os arquivos .java:

javac src/filesystem/*.java

Execute o simulador:
java src/filesystem/FileSystemShell

Estrutura do Projeto


Sistema_Arquivos/
├── src/
│   └── filesystem/
│       ├── Directory.java
│       ├── File.java
│       ├── FileSystemNode.java
│       ├── FileSystemSimulator.java
│       ├── FileSystemShell.java
│       ├── Journal.java
│       └── journal.txt
└── README.md