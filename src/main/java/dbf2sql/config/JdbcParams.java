package dbf2sql.config;

import org.simpleframework.xml.Element;

/**
 * @author octo
 */
public class JdbcParams {

    @Element(required = true)
    private String driver;
    @Element(required = true)
    private String url;
    @Element()
    private String user;
    @Element()
    private String password;

    @Element()
    private String schema;

    public String getDriver() {
        return driver;
    }

    public JdbcParams setDriver(String driver) {
        this.driver = driver;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public JdbcParams setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getUser() {
        return user;
    }

    public JdbcParams setUser(String user) {
        this.user = user;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public JdbcParams setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getSchema() {
        return schema;
    }

    public JdbcParams setSchema(String schema) {
        this.schema = schema;
        return this;
    }

    @Override
    public String toString() {
        return "JdbcParams{" +
                "driver='" + driver + '\'' +
                ", url='" + url + '\'' +
                ", user='" + user + '\'' +
                ", password=******" +
                ", schema='" + schema + '\'' +
                '}';
    }
}
