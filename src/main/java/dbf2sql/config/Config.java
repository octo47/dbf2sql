package dbf2sql.config;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * @author octo
 */
@Element(name = "config")
@Root
public class Config {
    @Attribute(required = true)
    private String name;

    @ElementList(inline = true)
    private List<InputFile> files;

    @Element
    private JdbcParams jdbcParams;

    public String getName() {
        return name;
    }

    public Config setName(String name) {
        this.name = name;
        return this;
    }

    public List<InputFile> getFiles() {
        return files;
    }

    public Config setFiles(List<InputFile> files) {
        this.files = files;
        return this;
    }

    public JdbcParams getJdbcParams() {
        return jdbcParams;
    }

    public Config setJdbcParams(JdbcParams jdbcParams) {
        this.jdbcParams = jdbcParams;
        return this;
    }

    @Override
    public String toString() {
        return "Config{" +
                "files=" + files +
                ", jdbcParams=" + jdbcParams +
                '}';
    }
}
