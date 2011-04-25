package dbf2sql.generator.typeinfo;

/**
 * @author octo
 */
public class NumericType extends AbstractTypeInfo {

    Require requireDecimals = REQUIRED;
    Require requirePrecision = OPTIONAL;

    public NumericType(String jdbcType, String sqlType) {
        super(jdbcType, sqlType);
    }

    public NumericType(String jdbcType, String sqlType, Require requireDecimals) {
        super(jdbcType, sqlType);
        this.requireDecimals = requireDecimals;
    }

    public NumericType(String jdbcType, String sqlType,
                       Require requirePrecision,
                       Require requireDecimals) {
        super(jdbcType, sqlType);
        this.requirePrecision = requirePrecision;
        this.requireDecimals = requireDecimals;
    }

    public String toDDL(FieldInfo fieldInfo) {
        StringBuffer sb = new StringBuffer();
        final Integer precision = fieldInfo.getPrecision();
        final Integer decimals = fieldInfo.getDecimals();
        sb.append(getSqlType());
        if (precision == null) {
            if (requirePrecision.isRequired()) {
                throw new TypeInfoException("Type " + getJdbcType()
                        + " needs precision for task " + getEngineName());
            }
        } else {
            sb.append("(").append(precision);
            if (decimals == null) {
                if (requireDecimals.isRequired()) {
                    throw new TypeInfoException("Type " + getJdbcType()
                            + " needs decimals for task " + getEngineName());
                }
            } else {
                sb.append(",").append(decimals);
            }
            sb.append(")");
        }
        return sb.toString();
    }
}