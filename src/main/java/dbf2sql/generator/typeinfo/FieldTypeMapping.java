package dbf2sql.generator.typeinfo;

/**
 * @author octo
 */
public class FieldTypeMapping {
    FieldInfo field;
    TypeInfo typeInfo;

    public FieldTypeMapping(FieldInfo field, TypeInfo typeInfo) {
        this.field = field;
        this.typeInfo = typeInfo;
    }

    public String getName() {
        return field.getName();
    }

    public String getDDL() {
        return typeInfo.toDDL(field);
    }
}
