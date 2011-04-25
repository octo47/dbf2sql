package dbf2sql.generator.pgsql;

import org.apache.log4j.Logger;
import dbf2sql.generator.AbstractDDLGenerator;
import dbf2sql.generator.typeinfo.*;

import java.util.regex.Pattern;

/**
 * @author octo
 */
public class PgSqlDDLGenerator extends AbstractDDLGenerator {

    private static final Logger LOG = Logger.getLogger(PgSqlDDLGenerator.class);

    public PgSqlDDLGenerator(String schema, String plainName) {
        this(new TableName(schema, cleanIdentifier(plainName)));
    }

    public PgSqlDDLGenerator(TableName tableName) {
        super(tableName);
        this.addTypeInfo(new NoParamType("bit", "BOOLEAN"));
        LOG.debug("PostgreSQL does not support 'TINYINT' type, use SMALLINT instead.");
        this.addTypeInfo(new NoParamType("tinyint", "SMALLINT"));
        this.addTypeInfo(new NoParamType("smallint", "SMALLINT"));
        this.addTypeInfo(new NoParamType("integer", "INTEGER"));
        this.addTypeInfo(new NoParamType("int", "INTEGER"));
        this.addTypeInfo(new NoParamType("bigint", "BIGINT"));

        this.addTypeInfo(new NoParamType("float", "DOUBLE PRECISION"));
        this.addTypeInfo(new NoParamType("double", "DOUBLE PRECISION"));
        this.addTypeInfo(new NoParamType("real", "REAL"));
        this.addTypeInfo(new NumericType("numeric", "DECIMAL", TypeInfo.REQUIRED));
        this.addTypeInfo(new NumericType("decimal", "DECIMAL", TypeInfo.REQUIRED, TypeInfo.OPTIONAL));

        // character types
        this.addTypeInfo(new LengthType("char", "CHAR", TypeInfo.OPTIONAL));
        this.addTypeInfo(new LengthType("varchar", "VARCHAR", TypeInfo.OPTIONAL));
        LOG.debug("PostgreSQL does not support 'LONGVARCHAR' type, use VARCHAR instead.");
        this.addTypeInfo(new LengthType("longvarchar", "TEXT"));

        // date and time types
        this.addTypeInfo(new NoParamType("date", "DATE"));
        this.addTypeInfo(new LengthType("time", "TIME", TypeInfo.OPTIONAL));
        this.addTypeInfo(new LengthType("timestamp", "TIMESTAMP", TypeInfo.OPTIONAL));

        // other types
        LOG.debug("PostgreSQL does not support 'BINARY' type, use BYTEA instead.");
        this.addTypeInfo(new NoParamType("binary", "BYTEA"));
        LOG.debug("PostgreSQL does not support 'VARBINARY' type, use BYTEA instead.");
        this.addTypeInfo(new NoParamType("varbinary", "BYTEA"));
        LOG.debug("PostgreSQL does not support 'LONGVARBINARY' type, use BYTEA instead.");
        this.addTypeInfo(new NoParamType("longvarbinary", "BYTEA"));

        this.addTypeInfo(new NoParamType("other", "BYTEA"));
        this.addTypeInfo(new NoParamType("javaobject", "BYTEA"));
        this.addTypeInfo(new NoParamType("blob", "BYTEA"));
        this.addTypeInfo(new NoParamType("clob", "TEXT"));
    }

    private static Pattern patt = Pattern.compile("[\\W\\.\\-']");

    public static String cleanIdentifier(String name) {
        return patt.matcher(name).replaceAll("_").toLowerCase();
    }
}