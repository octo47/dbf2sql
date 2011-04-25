package dbf2sql.generator.typeinfo;

/**
 * @author octo
 */
public class NotSupportedType extends AbstractTypeInfo {

    public NotSupportedType(String jdbcType) {
        super(jdbcType, null);
    }

    public String toDDL(FieldInfo fieldInfo) {
        throw new TypeInfoException("Type " + getJdbcType() + " not supported by " + getEngineName());
    }
}
