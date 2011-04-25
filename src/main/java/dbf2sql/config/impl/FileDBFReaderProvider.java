package dbf2sql.config.impl;

import javadbf.DBFReader;
import dbf2sql.config.AbstractDBFReaderProvider;
import dbf2sql.config.InputFile;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * Reader provider
 */
public class FileDBFReaderProvider extends AbstractDBFReaderProvider {
    private DBFReader dbfReader = null;

    public FileDBFReaderProvider(InputFile file) {
        super(file);
    }

    @Override
    public DBFReader getDBFReader() throws IOException {
        synchronized (this) {
            if (dbfReader == null) {
                InputStream inputStream = new FileInputStream(getFile().getFileName());

                if (getFile().isGzip())
                    dbfReader = new DBFReader(new BufferedInputStream(new GZIPInputStream(inputStream)));
                else
                    dbfReader = new DBFReader(inputStream);
            }
            return dbfReader;
        }
    }
}
