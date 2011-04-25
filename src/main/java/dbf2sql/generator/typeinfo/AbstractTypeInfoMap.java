package dbf2sql.generator.typeinfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author octo
 */
public class AbstractTypeInfoMap implements TypeInfoMap {
    private Map<String, TypeInfo> typeInfoMap = new HashMap<String, TypeInfo>();

    protected void addTypeInfo(TypeInfo typeInfo) {
        typeInfoMap.put(typeInfo.getJdbcType(), typeInfo);
    }

    public TypeInfo getTypeInfo(String jdbcType) {
        final TypeInfo typeInfo = typeInfoMap.get(jdbcType);
        if (typeInfo == null)
            throw new IllegalArgumentException("No such type: " + jdbcType);
        return typeInfo;
    }

    protected Map<String, TypeInfo> getTypeInfoMap() {
        return typeInfoMap;
    }

}
