package dbf2sql;

import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import dbf2sql.config.InputFile;
import dbf2sql.test.DbfFileFixture;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author octo
 */
@ContextConfiguration
public class DbfFileExecutorTest extends AbstractJUnit4SpringContextTests {

    @Resource
    DataSource dataSource;

    DbfFileFixture fixture = new DbfFileFixture();

    @Before
    public void init() throws IOException {
        fixture.init();
    }

    @Test
    public void simpleTest() throws Exception {
        final DbfFileExecutor dbfFileExecutor = new DbfFileExecutor(new InputFile().setFileName(fixture.dbfFile1.getAbsoluteFile()).setEncoding("cp866").setReplace(true), "public", fixture.dbfFile1.getName(), dataSource);
        final ExecutionResult result = dbfFileExecutor.call();
        assertNotNull(result);

    }

    @Test
    public void simpleTestWithMapping() throws Exception {

        Map<String,String> map = new HashMap<String, String>();

        map.put("ID", "ID");
        map.put("N1", "N1");

        final DbfFileExecutor dbfFileExecutor = new DbfFileExecutor(new InputFile().setFileName(fixture.dbfFile1.getAbsoluteFile()).setEncoding("cp866").setReplace(true), "public", fixture.dbfFile1.getName(), dataSource);

        dbfFileExecutor.setMappings(map);
        
        final ExecutionResult result = dbfFileExecutor.call();
        assertNotNull(result);

    }

    @Test
    public void simpleGzTest() throws Exception {
        final DbfFileExecutor dbfFileExecutor = new DbfFileExecutor(new InputFile().setFileName(fixture.dbfFile1gz.getAbsoluteFile()).setEncoding("cp866").setReplace(true), "public", fixture.dbfFile1gz.getName(), dataSource);
        final ExecutionResult result = dbfFileExecutor.call();
        assertNotNull(result);

    }
}
