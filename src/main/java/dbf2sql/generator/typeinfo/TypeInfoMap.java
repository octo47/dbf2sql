package dbf2sql.generator.typeinfo;

/**
 * @author octo
 */
public interface TypeInfoMap {
    TypeInfo getTypeInfo(String jdbcType);
}
