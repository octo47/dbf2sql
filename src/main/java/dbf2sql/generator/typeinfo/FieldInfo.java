package dbf2sql.generator.typeinfo;

/**
 * @author octo
 */
public class FieldInfo {

    private String name;
    private String jdbcTypeName;
    private Long length;
    private Integer decimals;
    private Integer precision;

    public FieldInfo(String name, String jdbcTypeName) {
        this.name = name;
        this.jdbcTypeName = jdbcTypeName;
    }

    public FieldInfo(String name, String jdbcTypeName, long length) {
        this.name = name;
        this.length = length;
        this.jdbcTypeName = jdbcTypeName;
    }

    public FieldInfo(String name, String jdbcTypeName, int precision, int decimals) {
        this.name = name;
        this.jdbcTypeName = jdbcTypeName;
        this.precision = precision;
        this.decimals = decimals;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJdbcTypeName() {
        return jdbcTypeName;
    }

    public void setJdbcTypeName(String jdbcTypeName) {
        this.jdbcTypeName = jdbcTypeName;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }

    public Integer getPrecision() {
        return precision;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }
}
