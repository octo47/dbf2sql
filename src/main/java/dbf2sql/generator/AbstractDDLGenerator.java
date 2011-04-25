package dbf2sql.generator;

import org.apache.log4j.Logger;
import dbf2sql.DDLGenerator;
import dbf2sql.generator.typeinfo.FieldInfo;
import dbf2sql.generator.typeinfo.FieldMapping;
import dbf2sql.generator.typeinfo.TableName;
import dbf2sql.generator.typeinfo.TypeInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author octo
 */
public abstract class AbstractDDLGenerator implements DDLGenerator {

    private static final Logger LOG = Logger.getLogger(AbstractDDLGenerator.class);

    private Map<String, TypeInfo> typeInfoMap = new HashMap<String, TypeInfo>();

    private List<FieldMapping> mapping = new ArrayList<FieldMapping>();

    private Map<String, String> fieldMapping;

    private TableName targetTableName;
    private String quoteIdentifier = "\"";
    private String schemaSeparator = ".";

    protected AbstractDDLGenerator(TableName tableName) {
        this.targetTableName = tableName;
    }

    public void addFields(List<FieldInfo> fieldInfo) {
        for (FieldInfo info : fieldInfo) {
            addField(info);
        }
    }

    public void setMappings(Map<String, String> m) {
        this.fieldMapping = m;
    }

    public void addField(FieldInfo fieldInfo) {
        final String sqlType = fieldInfo.getJdbcTypeName();
        final TypeInfo typeInfo = typeInfoMap.get(sqlType);
        if (typeInfo == null)
            throw new IllegalArgumentException("No such type: " + sqlType);
        mapping.add(new FieldMapping(fieldInfo, typeInfo));
        if (LOG.isDebugEnabled()) {
            LOG.debug("Mapped " + fieldInfo + " to " + sqlType);
        }
    }

    public String createDDL() {
        StringBuilder sql = new StringBuilder();
        sql.append("create table ");
        sql.append(getTargetTableName().getFullyQualifiedName(quoteIdentifier, schemaSeparator));
        sql.append(" (");
        String sep = "";
        for (FieldMapping fieldMapping : this.getMapping()) {
            sql.append(sep);
            sql.append(fieldMapping.getName());
            sql.append(" ");
            sql.append(fieldMapping.getDDL());
            sep = " ,";
        }
        sql.append(");");
        return sql.toString();
    }

    public String insertDDL() {
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ");
        sql.append(getTargetTableName().getFullyQualifiedName(quoteIdentifier, schemaSeparator));
        sql.append(" (");
        String sep = "";
        StringBuilder params = new StringBuilder();
        for (FieldMapping fm : this.getMapping()) {

            String name = fm.getName();

            if(fieldMapping!=null)
                name = fieldMapping.get(name.toUpperCase());

            if(name==null)
                continue;
            
            sql.append(sep).append(name);
            params.append(sep).append("?");
            sep = " ,";
        }
        sql.append(") values (").append(params).append(")");

        return sql.toString();
    }

    public String dropDDL() {
        StringBuilder sql = new StringBuilder();
        sql.append("drop table ");
        sql.append(getTargetTableName().getFullyQualifiedName(quoteIdentifier, schemaSeparator));
        sql.append(";");
        return sql.toString();
    }


    protected void addTypeInfo(TypeInfo typeInfo) {
        typeInfoMap.put(typeInfo.getJdbcType(), typeInfo);
    }

    protected TypeInfo getTypeInfo(String jdbcType) {
        return typeInfoMap.get(jdbcType);
    }

    public Map<String, TypeInfo> getTypeInfoMap() {
        return typeInfoMap;
    }

    public List<FieldMapping> getMapping() {
        return mapping;
    }

    public TableName getTargetTableName() {
        return targetTableName;
    }

    public String getQuoteIdentifier() {
        return quoteIdentifier;
    }

    public void setQuoteIdentifier(String quoteIdentifier) {
        this.quoteIdentifier = quoteIdentifier;
    }

    public String getSchemaSeparator() {
        return schemaSeparator;
    }

    public void setSchemaSeparator(String schemaSeparator) {
        this.schemaSeparator = schemaSeparator;
    }
}
