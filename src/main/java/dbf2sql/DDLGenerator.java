package dbf2sql;

import dbf2sql.generator.typeinfo.FieldInfo;

import java.util.List;

/**
 * @author octo
 */
public interface DDLGenerator {
    public void addFields(List<FieldInfo> fieldInfo);

    public void addField(FieldInfo fieldInfo);

    public String createDDL();

    public String insertDDL();
}
