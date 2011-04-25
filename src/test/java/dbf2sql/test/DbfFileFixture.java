package dbf2sql.test;

import javadbf.DBFField;
import javadbf.DBFWriter;

import java.io.*;
import java.sql.Date;
import java.util.zip.GZIPOutputStream;

/**
 * @author octo
 */
public class DbfFileFixture {
    File dbfDir = new File("target/tmp");
    public File dbfFile1 = new File(dbfDir, "test1.dbf");
    public File dbfFile1gz = new File(dbfDir, "test1.dbf.gz");
    public File dbfFile2 = new File(dbfDir, "test2.dbf");
    public File dbfFile2gz = new File(dbfDir, "test2.dbf.gz");

    public void init() {
        if (!dbfDir.exists())
            dbfDir.mkdirs();
        if (dbfFile1.exists())
            dbfFile1.delete();
        prepareDbf(dbfFile1, false);
        prepareDbf(dbfFile2, false);
        prepareDbf(dbfFile1gz, true);
        prepareDbf(dbfFile2gz, true);
    }

    private void prepareDbf(File dbfFile, boolean doGz) {
        try {

            OutputStream fos = new FileOutputStream(dbfFile);
            if (doGz) {
                fos = new GZIPOutputStream(fos);
            }
            DBFWriter writer = new DBFWriter();
            DBFField fields[] = new DBFField[6];
            fields[0] = new DBFField();
            fields[0].setName("ID");
            fields[0].setDataType(DBFField.FIELD_TYPE_N);
            fields[0].setFieldLength(8);

            fields[1] = new DBFField();
            fields[1].setName("N1");
            fields[1].setDataType(DBFField.FIELD_TYPE_N);
            fields[1].setFieldLength(15);
            fields[1].setDecimalCount(0);

            fields[2] = new DBFField();
            fields[2].setName("C1");
            fields[2].setDataType(DBFField.FIELD_TYPE_C);
            fields[2].setFieldLength(45);

            fields[3] = new DBFField();
            fields[3].setName("F1");
            fields[3].setDataType(DBFField.FIELD_TYPE_F);
            fields[3].setFieldLength(20);
            fields[3].setDecimalCount(2);

            fields[4] = new DBFField();
            fields[4].setName("L1");
            fields[4].setDataType(DBFField.FIELD_TYPE_L);

            fields[5] = new DBFField();
            fields[5].setName("D1");
            fields[5].setDataType(DBFField.FIELD_TYPE_D);

            writer.setFields(fields);

            for (int i = 0; i < 1200; i++) {
                writer.addRecord(new Object[]{(double) i, ((double) 123456789012345L) + 0.1 * i, makeString(i), i * 1.33, i % 10 == 0, new Date(new java.util.Date().getTime())});
            }

            writer.write(fos);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String makeString(int i) throws UnsupportedEncodingException {
        byte[] bytes = ("проверкаhello" + i).getBytes("utf-8");
        bytes[bytes.length / 2] = 0;
        return new String(bytes, "utf-8");
    }

}
