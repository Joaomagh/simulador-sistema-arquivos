package filesystem;

import java.io.*;
import java.util.List;
import java.util.Optional;

public class FileSystemSimulator implements Serializable {
    private static final long serialVersionUID = 1L;
    private Directory rootDirectory;
    private Journal journal;
    private boolean isLoaded;

    public FileSystemSimulator() {
        this.rootDirectory = new Directory("root");
        this.journal = new Journal("journal.txt"); // Default log file path
        this.isLoaded = false;
    }

    // Get the root directory
    public Directory getRootDirectory() {
        return rootDirectory;
    }

    // Save the file system state to a file
    public void saveToFile(String filePath) {
        if (!isLoaded) {
            System.out.println("Erro: O sistema de arquivos não foi carregado.");
            return;
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(rootDirectory);
            oos.writeObject(journal);
            oos.flush();
            System.out.println("Sistema de arquivos salvo com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao salvar o sistema de arquivos: " + e.getMessage());
        }
    }

    // Load the file system state from a file
    public void loadFromFile(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            this.rootDirectory = (Directory) ois.readObject();
            this.journal = (Journal) ois.readObject();
            this.isLoaded = true;
            System.out.println("Sistema de arquivos carregado com sucesso.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar o sistema de arquivos: " + e.getMessage());
        }
    }

    // Create a directory
    public void createDirectory(String path) {
        if (path == null || path.isEmpty()) {
            System.out.println("Erro: Caminho inválido.");
            return;
        }

        String[] parts = path.split("/");
        Directory current = rootDirectory;

        for (String dirName : parts) {
            if (dirName.isEmpty()) continue;

            Optional<FileSystemNode> nodeOpt = Optional.ofNullable(current.findChildByName(dirName));

            if (nodeOpt.isPresent()) {
                FileSystemNode node = nodeOpt.get();
                if (node instanceof Directory) {
                    current = (Directory) node;
                    System.out.println("Aviso: Diretório já existe: " + dirName);
                } else {
                    System.out.println("Erro: Caminho intermediário não é um diretório: " + dirName);
                    return;
                }
            } else {
                Directory newDir = new Directory(dirName);
                current.addChild(newDir);
                current = newDir;
                System.out.println("Diretório criado: " + dirName); // Mensagem de sucesso sem detalhes extras
            }
        }
    }

    // List contents of a directory
    public void listContents(String path) {
        FileSystemNode node = rootDirectory.findNodeByPath(path);
        if (!(node instanceof Directory)) {
            System.out.println("Erro: Caminho inválido ou não é um diretório.");
            return;
        }

        Directory dir = (Directory) node;
        System.out.println("Conteúdo do diretório " + dir.getName() + ":");
        dir.getSubDirectories().forEach(subDir -> System.out.println("Dir: " + subDir.getName()));
        dir.getFiles().forEach(file -> System.out.println("File: " + file.getName()));
    }

    // Copy a file or directory
    public void copy(String sourcePath, String destinationPath) {
        FileSystemNode sourceNode = rootDirectory.findNodeByPath(sourcePath);
        if (sourceNode == null) {
            System.out.println("Erro: Caminho de origem não encontrado.");
            return;
        }

        FileSystemNode destinationNode = rootDirectory.findNodeByPath(destinationPath);
        if (!(destinationNode instanceof Directory)) {
            System.out.println("Erro: Caminho de destino não é um diretório válido.");
            return;
        }

        Directory destinationDir = (Directory) destinationNode;
        if (destinationDir.findChildByName(sourceNode.getName()) != null) {
            System.out.println("Erro: Já existe um arquivo ou diretório com o mesmo nome no destino.");
            return;
        }

        destinationDir.addChild(sourceNode.cloneNode());
        journal.logOperation("Copy", "Source: " + sourcePath + ", Destination: " + destinationPath);
        System.out.println("Cópia realizada com sucesso de " + sourcePath + " para " + destinationPath);
    }


    public void rename(String oldPath, String newName) {
        FileSystemNode node = rootDirectory.findNodeByPath(oldPath);
        if (node == null) {
            System.out.println("Erro: Caminho não encontrado.");
            return;
        }

        node.setName(newName);
        journal.logOperation("Rename", "Old Path: " + oldPath + ", New Name: " + newName);
        System.out.println("Renomeado com sucesso para " + newName);
    }


    public void delete(String path) {
        FileSystemNode node = rootDirectory.findNodeByPath(path);
        if (node == null) {
            System.out.println("Erro: Caminho não encontrado.");
            return;
        }

        if (node instanceof Directory) {
            Directory dir = (Directory) node;
            if (!dir.getSubDirectories().isEmpty() || !dir.getFiles().isEmpty()) {
                System.out.println("Erro: O diretório não está vazio.");
                return;
            }
        }

        Directory parent = node.getParent();
        if (parent == null) {
            System.out.println("Erro: Não é possível excluir o diretório raiz.");
            return;
        }

        parent.removeChild(node);
        System.out.println("Exclusão realizada com sucesso: " + path);
    }


    // Display recent journal logs
    public void displayJournalLogs() {
        List<String> logs = journal.getLogs();
        if (logs.isEmpty()) {
            System.out.println("Nenhum log disponível.");
        } else {
            System.out.println("Logs do sistema:");
            int startIndex = Math.max(0, logs.size() - 10);
            for (int i = startIndex; i < logs.size(); i++) {
                System.out.println(logs.get(i));
            }
        }
    }
}
