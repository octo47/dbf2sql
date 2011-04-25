package dbf2sql.config;

import org.simpleframework.xml.load.Persister;

import java.io.File;
import java.io.Reader;

/**
 * @author octo
 */
public class ConfigReader {

    public Config readConfig(File file) throws Exception {
        return new Persister().read(Config.class, file);
    }

    public Config readConfig(Reader reader) throws Exception {
        return new Persister().read(Config.class, reader);
    }
}
