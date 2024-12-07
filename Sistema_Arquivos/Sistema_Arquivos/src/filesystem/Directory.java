package filesystem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Directory extends FileSystemNode implements Serializable {
    private List<FileSystemNode> children;
    private static final long serialVersionUID = 1L;

    public Directory(String name) {
        super(name);
        this.children = new ArrayList<>();
    }

    public List<FileSystemNode> getChildren() {
        return children;
    }

    public void addChild(FileSystemNode child) {
        child.setParent(this);
        children.add(child);
    }

    public void removeChild(FileSystemNode child) {
        children.remove(child);
    }

    public FileSystemNode findNodeByPath(String path) {
        if (path == null || path.isEmpty()) {
            return null; // Caminho vazio
        }

        // Caminho absoluto, começa com "/"
        if (path.startsWith("/")) {
            path = path.substring(1); // Remove a barra inicial para facilitar a busca
            return this.findNodeByPathFromRoot(path); // Corrigido para chamar a instância atual
        } else {
            // Caminho relativo, começa do diretório atual
            return this.findNodeByPathFromCurrent(path); // Corrigido para chamar a instância atual
        }
    }

    private FileSystemNode findNodeByPathFromRoot(String path) {
        String[] parts = path.split("/");
        FileSystemNode current = this; // Começa pela instância atual, que é um diretório
        for (String part : parts) {
            if (part.isEmpty()) continue; // Ignora partes vazias (ex: //)
            current = ((Directory) current).findChildByName(part);
            if (current == null) {
                return null; // Caminho não encontrado
            }
        }
        return current;
    }

    private FileSystemNode findNodeByPathFromCurrent(String path) {
        // Busca dentro do diretório atual (implementação básica)
        String[] parts = path.split("/");
        FileSystemNode current = this; // Começa no diretório atual
        for (String part : parts) {
            if (part.isEmpty()) continue; // Ignora partes vazias (ex: //)
            current = ((Directory) current).findChildByName(part);
            if (current == null) {
                return null; // Caminho não encontrado
            }
        }
        return current;
    }


    public FileSystemNode findChildByName(String name) {
        for (FileSystemNode child : children) {
            if (child.getName().equals(name)) {
                return child;
            }
        }
        return null;
    }

    public List<Directory> getSubDirectories() {
        List<Directory> subDirs = new ArrayList<>();
        for (FileSystemNode child : children) {
            if (child instanceof Directory) {
                subDirs.add((Directory) child);
            }
        }
        return subDirs;
    }

    public List<File> getFiles() {
        List<File> files = new ArrayList<>();
        for (FileSystemNode child : children) {
            if (child instanceof File) {
                files.add((File) child);
            }
        }
        return files;
    }

    @Override
    public FileSystemNode cloneNode() {
        Directory clonedDirectory = new Directory(this.getName());
        for (FileSystemNode child : this.getChildren()) {
            clonedDirectory.addChild(child.cloneNode());
        }
        return clonedDirectory;
    }
}
