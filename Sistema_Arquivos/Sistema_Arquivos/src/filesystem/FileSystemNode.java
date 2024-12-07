package filesystem;

import java.io.Serializable;

public abstract class FileSystemNode implements Serializable {
    private static final long serialVersionUID = 1L; // Defina o serialVersionUID
    private String name;
    private Directory parent;

    public FileSystemNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Directory getParent() {
        return parent;
    }

    public void setParent(Directory parent) {
        this.parent = parent;
    }

    public abstract FileSystemNode cloneNode();
}

