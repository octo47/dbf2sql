package dbf2sql.generator.typeinfo;

/**
 * @author octo
 */
public abstract class AbstractTypeInfo implements TypeInfo {

    private String engineName = "unknown";
    private String jdbcType;
    private String sqlType;

    public AbstractTypeInfo(String jdbcType, String sqlType) {
        this.jdbcType = jdbcType;
        this.sqlType = sqlType;
    }

    public String getEngineName() {
        return engineName;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public String getSqlType() {
        return sqlType;
    }

    public TypeInfo setEngineName(String engineName) {
        this.engineName = engineName;
        return this;
    }
}
