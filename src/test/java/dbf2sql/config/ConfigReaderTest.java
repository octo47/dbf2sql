package dbf2sql.config;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.io.StringReader;

/**
 * @author octo
 */
public class ConfigReaderTest {

    @Test
    public void testXmlReader() throws Exception {
        String xml = "<config name=\"config1\">" +
                "<file path=\"/var/tmp/file.txt\" encoding=\"cp866\"/>" +
                "<file path=\"/var/tmp/file2.txt\" encoding=\"cp866\"/>" +
                "<jdbcParams>" +
                " <schema>etl</schema>" +
                " <url>jdbc:fffurl</url>" +
                " <driver>org.postgresql.jdbc.Driver</driver>" +
                " <user>user</user>" +
                " <password>pwd</password>" +
                "</jdbcParams>" +
                "</config>";

        final Config config = new ConfigReader().readConfig(new StringReader(xml));
        assertEquals("file.txt", config.getFiles().get(0).getFileName().getName());
        assertEquals("file2.txt", config.getFiles().get(1).getFileName().getName());
        System.out.println(config);
    }
}
