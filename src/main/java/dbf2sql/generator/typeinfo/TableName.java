package dbf2sql.generator.typeinfo;

/**
 * @author octo
 */
public class TableName {
    private String schema;
    private String tableName;

    public TableName(String tableName) {
        this.tableName = tableName;
    }

    public TableName(String schema, String tableName) {
        this.schema = schema;
        this.tableName = tableName;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @SuppressWarnings({"RedundantIfStatement"})
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TableName tableName1 = (TableName) o;

        if (schema != null ? !schema.equals(tableName1.schema) : tableName1.schema != null) return false;
        if (tableName != null ? !tableName.equals(tableName1.tableName) : tableName1.tableName != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (schema != null ? schema.hashCode() : 0);
        result = 31 * result + (tableName != null ? tableName.hashCode() : 0);
        return result;
    }

    public boolean hasSchema() {
        return schema != null && schema.length() > 0;
    }

    public String getFullyQualifiedName(String quoteIdentifier, String shemaSeparator) {
        final StringBuilder fqon = new StringBuilder();
        if (this.hasSchema())
            fqon.append(quoteIdentifier).append(schema).append(quoteIdentifier).append(shemaSeparator);
        fqon.append(quoteIdentifier).append(tableName).append(quoteIdentifier);
        return fqon.toString();
    }
}
