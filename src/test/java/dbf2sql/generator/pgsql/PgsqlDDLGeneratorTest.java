package dbf2sql.generator.pgsql;

import javadbf.DBFException;
import javadbf.DBFReader;
import org.junit.Before;
import org.junit.Test;
import dbf2sql.core.DbfFieldInfoReader;
import dbf2sql.generator.typeinfo.FieldInfo;
import dbf2sql.test.DbfFileFixture;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author octo
 */
public class PgsqlDDLGeneratorTest {

    DbfFileFixture fixture = new DbfFileFixture();

    @Before
    public void init() {
        fixture.init();
    }

    @Test
    public void simpleTest() throws IOException {
        PgSqlDDLGenerator pgSqlDDLGenerator = new PgSqlDDLGenerator(null, fixture.dbfFile1.getName());
        pgSqlDDLGenerator.addField(new FieldInfo("id", "bigint"));
        pgSqlDDLGenerator.addField(new FieldInfo("name", "varchar", 256));
        pgSqlDDLGenerator.addField(new FieldInfo("dt", "date"));

        // TODO: implement test with hsqld
        final String createDdl = pgSqlDDLGenerator.createDDL();
        System.out.println(createDdl);

        final String insertDdl = pgSqlDDLGenerator.insertDDL();
        System.out.println(insertDdl);

    }


    @Test
    public void dbfExamineTest() throws FileNotFoundException, DBFException {
        final DBFReader dbfReader = new DBFReader(new FileInputStream(fixture.dbfFile1));
        final DbfFieldInfoReader fieldInfoReader = new DbfFieldInfoReader(dbfReader);
        PgSqlDDLGenerator pgSqlDDLGenerator = new PgSqlDDLGenerator(null, fixture.dbfFile1.getName());
        pgSqlDDLGenerator.addFields(fieldInfoReader.readFields());

        // TODO: implement test with hsqld
        final String createDdl = pgSqlDDLGenerator.createDDL();
        System.out.println(createDdl);

        final String insertDdl = pgSqlDDLGenerator.insertDDL();
        System.out.println(insertDdl);

    }
}
