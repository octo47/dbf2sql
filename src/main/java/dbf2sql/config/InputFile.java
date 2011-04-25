package dbf2sql.config;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.io.File;

/**
 * @author octo
 */
@Root(name = "file")
public class InputFile {
    @Attribute(required = true, name = "path")
    private File fileName;

    @Attribute(required = true)
    private String encoding;

    @Attribute(required = false)
    private Boolean replace = false;


    public File getFileName() {
        return fileName;
    }

    public InputFile setFileName(File fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getEncoding() {
        return encoding;
    }

    public InputFile setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public Boolean isReplace() {
        return replace;
    }

    public InputFile setReplace(Boolean replace) {
        this.replace = replace;
        return this;
    }

    @Override
    public String toString() {
        return "InputFile{" +
                "fileName=" + fileName +
                ", encoding='" + encoding + '\'' +
                ", replace=" + replace +
                '}';
    }


    public boolean isGzip() {
        return fileName.getName().endsWith(".gz");
    }

}
