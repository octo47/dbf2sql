package dbf2sql.generator.typeinfo;

/**
 * @author octo
 */
public class NoParamType extends AbstractTypeInfo {

    public NoParamType(String jdbcType, String sqlType) {
        super(jdbcType, sqlType);
    }

    public String toDDL(FieldInfo fieldInfo) {
        return getSqlType();
    }
}
