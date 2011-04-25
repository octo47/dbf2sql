package dbf2sql.generator.typeinfo;

/**
 * @author octo
 */
public class LengthType extends AbstractTypeInfo {

    Require req = REQUIRED;

    public LengthType(String jdbcType, String sqlType) {
        super(jdbcType, sqlType);
    }

    public LengthType(String jdbcType, String sqlType, Require require) {
        super(jdbcType, sqlType);
        req = require;
    }

    public String toDDL(FieldInfo fieldInfo) {
        StringBuffer sb = new StringBuffer();
        final Long length = fieldInfo.getLength();
        sb.append(getSqlType());
        if (length == null) {
            if (req.isRequired()) {
                throw new TypeInfoException("Type " + getJdbcType()
                        + " needs length for task " + getEngineName());
            }
        } else {
            sb.append("(").append(length).append(")");
        }
        return sb.toString();
    }
}
