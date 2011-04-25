package dbf2sql.config;

import javadbf.DBFReader;

import java.io.IOException;

/**
 * Base class, providing readers
 */
public abstract class AbstractDBFReaderProvider {

    private InputFile file;

    protected AbstractDBFReaderProvider(InputFile file) {
        this.file = file;
    }

    public InputFile getFile() {
        return file;
    }

    public abstract DBFReader getDBFReader() throws IOException;
}
