package dbf2sql.core;

import javadbf.DBFException;
import javadbf.DBFField;
import javadbf.DBFReader;
import dbf2sql.generator.typeinfo.FieldInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: redesign
 *
 * @author octo
 */
public class DbfFieldInfoReader {

    private DBFReader reader;

    public DbfFieldInfoReader(DBFReader reader) {
        this.reader = reader;
    }

    @SuppressWarnings({"OverlyComplexMethod"})
    public List<FieldInfo> readFields() {
        try {
            final int count = reader.getFieldCount();
            final ArrayList<FieldInfo> fieldinfos = new ArrayList<FieldInfo>(count);
            for (int i = 0; i < count; i++) {
                final DBFField field = reader.getField(i);
                switch (field.getDataType()) {
                    case DBFField.FIELD_TYPE_C:
                        fieldinfos.add(new FieldInfo(
                                field.getName(),
                                "varchar",
                                field.getFieldLength()));
                        break;
                    case DBFField.FIELD_TYPE_D:
                        fieldinfos.add(new FieldInfo(field.getName(), "date"));
                        break;
                    case DBFField.FIELD_TYPE_N:
                    case DBFField.FIELD_TYPE_F:
                        fieldinfos.add(new FieldInfo(
                                field.getName(),
                                "numeric",
                                field.getFieldLength(),
                                field.getDecimalCount()
                        ));
                        break;
                    case DBFField.FIELD_TYPE_L:
                        fieldinfos.add(new FieldInfo(field.getName(), "bit"));
                        break;
                    case DBFField.FIELD_TYPE_M:
                        fieldinfos.add(new FieldInfo(field.getName(), "longnvarchar"));
                        break;
                    default:
                }
            }
            return fieldinfos;
        } catch (DBFException e) {
            throw new DbfDriverException(e);
        }


    }
}
