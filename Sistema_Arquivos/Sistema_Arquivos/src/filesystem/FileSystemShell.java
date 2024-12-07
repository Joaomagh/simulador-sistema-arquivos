package filesystem;

import java.util.Scanner;

public class FileSystemShell {
    private FileSystemSimulator simulator;

    public FileSystemShell() {
        this.simulator = new FileSystemSimulator();
    }

    public void start() {
        System.out.println("Bem-vindo ao Shell do Simulador de Sistema de Arquivos!");
        System.out.println("Digite 'help' para ver a lista de comandos disponíveis.");

        Scanner scanner = new Scanner(System.in);
        String command;

        while (true) {
            System.out.print("\nfs> ");
            command = scanner.nextLine().trim();
            if (command.equalsIgnoreCase("exit")) {
                System.out.println("Saindo do Shell do Simulador de Sistema de Arquivos. Até logo!");
                break;
            } else if (command.equalsIgnoreCase("help")) {
                showHelp();
            } else {
                processCommand(command);
            }
        }

        scanner.close();
    }

    private void showHelp() {
        System.out.println("Comandos disponíveis:");
        System.out.println("  create <path>          - Cria um diretório ou arquivo no caminho especificado.");
        System.out.println("                          Exemplo: create /home/usuario/diretorio");
        System.out.println("  list [<path>]          - Lista o conteúdo do diretório especificado. Se nenhum caminho for dado, lista o diretório raiz.");
        System.out.println("                          Exemplo: list /home/usuario/diretorio ou list");
        System.out.println("  copy <source> <dest>   - Copia um diretório ou arquivo da origem para o destino.");
        System.out.println("                          Exemplo: copy /home/usuario/arquivo.txt /home/usuario/arquivo_copiado.txt");
        System.out.println("  rename <old> <new>     - Renomeia um diretório ou arquivo de um nome antigo para um novo nome.");
        System.out.println("                          Exemplo: rename /home/usuario/arquivo_antigo.txt novo_nome.txt");
        System.out.println("  delete <path>          - Exclui um diretório ou arquivo no caminho especificado.");
        System.out.println("                          Exemplo: delete /home/usuario/arquivo_a_deletar.txt");
        System.out.println("  logs                   - Exibe os logs de operações do sistema de arquivos.");
        System.out.println("  save <filePath>        - Salva o estado atual do sistema de arquivos em um arquivo.");
        System.out.println("                          Exemplo: save /home/usuario/sistemaDeArquivos.dat");
        System.out.println("  load <filePath>        - Carrega o estado do sistema de arquivos de um arquivo especificado.");
        System.out.println("                          Exemplo: load /home/usuario/sistemaDeArquivos.dat");
        System.out.println("  exit                   - Sair do shell.");
    }

    private void processCommand(String command) {
        String[] parts = command.trim().split("\\s+");
        if (parts.length == 0) {
            System.out.println("Comando inválido. Digite 'help' para a lista de comandos disponíveis.");
            return;
        }

        String cmd = parts[0].toLowerCase();

        switch (cmd) {
            case "save":
                if (parts.length != 2) {
                    System.out.println("Uso: save <filePath>");
                    System.out.println("  Exemplo: save /home/usuario/sistemaDeArquivos.dat");
                    return;
                }
                simulator.saveToFile(parts[1]);
                break;

            case "load":
                if (parts.length != 2) {
                    System.out.println("Uso: load <filePath>");
                    System.out.println("  Exemplo: load /home/usuario/sistemaDeArquivos.dat");
                    return;
                }
                simulator.loadFromFile(parts[1]);
                break;

            case "create":
                if (parts.length != 2) {
                    System.out.println("Uso: create <path>");
                    System.out.println("  Exemplo: create /home/usuario/novoDiretorio");
                    return;
                }
                simulator.createDirectory(parts[1]);
                break;

            case "list":
                if (parts.length == 1) {
                    simulator.listContents("/"); // Lista a raiz se nenhum caminho for fornecido
                } else {
                    simulator.listContents(parts[1]); // Lista o conteúdo do diretório especificado
                }
                break;

            case "copy":
                if (parts.length != 3) {
                    System.out.println("Uso: copy <source> <dest>");
                    System.out.println("  Exemplo: copy /home/usuario/arquivo.txt /home/usuario/arquivo_copiado.txt");
                    return;
                }
                simulator.copy(parts[1], parts[2]);
                break;

            case "rename":
                if (parts.length != 3) {
                    System.out.println("Uso: rename <old> <new>");
                    System.out.println("  Exemplo: rename /home/usuario/arquivoAntigo.txt novoNome.txt");
                    return;
                }
                simulator.rename(parts[1], parts[2]);
                break;

            case "delete":
                if (parts.length != 2) {
                    System.out.println("Uso: delete <path>");
                    System.out.println("  Exemplo: delete /home/usuario/arquivo_a_deletar.txt");
                    return;
                }
                simulator.delete(parts[1]);
                break;

            case "logs":
                simulator.displayJournalLogs();
                break;

            default:
                System.out.println("Comando desconhecido: " + cmd);
                System.out.println("Digite 'help' para a lista de comandos disponíveis.");
                break;
        }
    }

    public static void main(String[] args) {
        FileSystemShell shell = new FileSystemShell();
        shell.start();
    }
}
