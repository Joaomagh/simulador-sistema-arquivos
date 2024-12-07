package filesystem;

import java.io.Serializable;

public class File extends FileSystemNode implements Serializable {
    private static final long serialVersionUID = 1L; // Para compatibilidade de serialização
    public File(String name) {
        super(name);
    }

    @Override
    public FileSystemNode cloneNode() {
        return new File(this.getName());
    }
}
