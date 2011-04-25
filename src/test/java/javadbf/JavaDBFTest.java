package javadbf;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class JavaDBFTest {

    Logger log = Logger.getLogger(JavaDBFTest.class);

    File dir = new File("target/tmp");

    @Before
    public void init() {
        if (dir.exists())
            dir.delete();
        dir.mkdirs();
    }

    @Test
    public void test1() throws Exception {

        log.debug("Creating an empty DBFWriter object... ");
        DBFWriter writer = new DBFWriter();
        assertNotNull(writer);
    }

    @Test
    public void test2() throws Exception {
        log.debug("Creating an empty DBFField object... ");
        DBFField field = new DBFField();
        assertNotNull(field);
    }

    @Test
    public void test3() throws Exception {

        log.debug("Writing a sample DBF file ... ");
        DBFField field = new DBFField();
        field.setName("F1");
        field.setDataType(DBFField.FIELD_TYPE_N);
        DBFWriter writer = new DBFWriter();
        writer.setFields(new DBFField[]{field});
        writer.addRecord(new Object[]{(double) 3});
        File f = new File(dir, "121212.dbf");
        FileOutputStream fos = new FileOutputStream(f);
        writer.write(fos);
        fos.flush();
        fos.close();
        assertTrue(f.exists());
    }

    @Test
    public void test4() throws Exception {

        log.debug("Reading the written file ...");
        File f = new File(dir, "121212.dbf");
        FileInputStream fis = new FileInputStream(f);
        DBFReader reader = new DBFReader(fis);
        log.debug("\tRecord count=" + reader.getRecordCount());
        fis.close();
        assertTrue(f.exists());
    }

    @Test
    public void checkDataType_N() throws Exception {

        File f = new File(dir, "121212.dbf");

        FileOutputStream fos = new FileOutputStream(f);
        DBFWriter writer = new DBFWriter();
        DBFField field = new DBFField();
        field.setName("F1");
        field.setDataType(DBFField.FIELD_TYPE_N);
        field.setFieldLength(15);
        field.setDecimalCount(0);

        writer.setFields(new DBFField[]{field});
        Double value = (double) 123456789012345L;
        writer.addRecord(new Object[]{value});
        log.debug(" written=" + value);
        writer.write(fos);
        fos.close();

        FileInputStream fis = new FileInputStream(f);
        DBFReader reader = new DBFReader(fis);

        Object[] values = reader.nextRecord();
        log.debug(" read=" + (Double) values[0]);
        log.debug(" written == read (" + (((Double) values[0]).equals(value)) + ")");
        fis.close();
        assertTrue(f.exists());
    }

    @Test
    @SuppressWarnings({"OverlyLongMethod"})
    public void checkRAFwriting() throws Exception {

        log.debug("Writing in RAF mode ... ");
        File file = new File(dir, "raf-1212.dbf");
        if (file.exists()) {

            file.delete();
        }
        DBFWriter writer = new DBFWriter(file);

        DBFField[] fields = new DBFField[2];

        fields[0] = new DBFField();
        fields[0].setName("F1");
        fields[0].setDataType(DBFField.FIELD_TYPE_C);
        fields[0].setFieldLength(10);

        fields[1] = new DBFField();
        fields[1].setName("F2");
        fields[1].setDataType(DBFField.FIELD_TYPE_N);
        fields[1].setFieldLength(2);

        writer.setFields(fields);

        Object[] record = new Object[2];
        record[0] = "Red";
        record[1] = (double) 10;

        writer.addRecord(record);

        record = new Object[2];
        record[0] = "Blue";
        record[1] = (double) 20;

        writer.addRecord(record);

        writer.write();
        log.debug("done.");

        log.debug("Appending to this file");

        writer = new DBFWriter(file);

        record = new Object[2];
        record[0] = "Green";
        record[1] = (double) 33;

        writer.addRecord(record);

        record = new Object[2];
        record[0] = "Yellow";
        record[1] = (double) 44;

        writer.addRecord(record);

        writer.write();
        log.debug("done.");
        assertTrue(true);
    }
}
