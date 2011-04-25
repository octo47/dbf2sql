package dbf2sql;

import javadbf.DBFException;
import javadbf.DBFField;
import javadbf.DBFReader;
import javadbf.DBFWriter;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;

/**
 * @author astepachev
 */
public class CutDbfMain {

    final static Logger logger = LoggerFactory.getLogger(CutDbfMain.class);

    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    public int run(String[] args) {
        Options options = new Options();

        options.addOption("h", false, "usage");
        options.addOption("a", "append", false, "append mode");
        options.addOption("o", "overwrite", false, "overwrite mode");
        options.addOption("f", "first", true, "start row");
        options.addOption("l", "last", true, "last row");
        options.addOption("fl", "filter-long", true, "filed_num:value1,value2,value3 (all values casts to long)");

        CommandLineParser parser = new GnuParser();
        try {
            CommandLine line = parser.parse(options, args);
            final String[] fileNames = line.getArgs();
            if (fileNames.length != 1 && fileNames.length != 2 || line.hasOption('h')) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("cutdbf.sh [options] input [output]", options);
                return 1;
            }

            long firstRow;
            long lastRow;
            Integer filterFieldId = null;
            Set<Long> filterValues = new HashSet<Long>();

            final File inputFile = new File(fileNames[0]);
            final File outputFile = new File((fileNames.length == 2) ? fileNames[1] : fileNames[0] + "out");
            final DBFReader dbfReader = new DBFReader(
                    ((inputFile.getName().endsWith(".gz") ?
                            new GZIPInputStream(new FileInputStream(inputFile), 4096)
                            : new FileInputStream(inputFile))));

            final Converter conv;
            String filter = line.getOptionValue("fl");
            if (filter == null) {
                firstRow = Integer.parseInt(line.getOptionValue('f', "1"));
                lastRow = Integer.parseInt(line.getOptionValue('l', "1"));
                conv = null;
            } else {
                final String[] idvalue = filter.split(":");
                if (idvalue.length != 2)
                    throw new IllegalArgumentException("filter should have form field_num:value[,value]");
                final String[] values = idvalue[1].split(",");
                for (String value : values) {
                    filterValues.add(Long.parseLong(value));
                }
                filterFieldId = Integer.parseInt(idvalue[0]);
                firstRow = 1;
                lastRow = Long.MAX_VALUE;
                final DBFField field = dbfReader.getField(filterFieldId);
                if (field.getDataType() == DBFField.FIELD_TYPE_C)
                    conv = new Converter() {
                        @Override
                        public Long toLong(Object o) {
                            return Long.parseLong((String) o);
                        }
                    };
                else if (field.getDataType() == DBFField.FIELD_TYPE_F ||
                        field.getDataType() == DBFField.FIELD_TYPE_N ||
                        field.getDataType() == DBFField.FIELD_TYPE_D) {
                    conv = new Converter() {
                        @Override
                        public Long toLong(Object o) {
                            return ((Number) o).longValue();
                        }
                    };
                } else {
                    throw new IllegalArgumentException(String.format("doesn't support type %d for filtering", field.getDataType()));
                }
            }

            final List<Object[]> rows = new ArrayList<Object[]>();

            Object[] nr;
            long currRow = 0;
            while ((nr = dbfReader.nextRecord()) != null) {
                currRow++;
                if (currRow >= firstRow && currRow <= lastRow) {
                    boolean write = false;
                    if (filterFieldId == null)
                        write = true;
                    else if (filterFieldId != null && nr[filterFieldId] != null) {
                        if (filterValues.contains(conv.toLong(nr[filterFieldId]))) {
                            write = true;
                        }
                    }
                    if (write)
                        rows.add(nr);
                }
                if (currRow > lastRow)
                    break;
            }
            if (rows.size() > 0) {
                boolean addFields = true;
                if (outputFile.exists()) {
                    if (line.hasOption("overwrite")) {
                        outputFile.delete();
                    } else if (line.hasOption("append")) {
                        addFields = false;
                    } else {
                        System.err.printf("file %s exists", outputFile);
                        System.exit(3);
                    }
                }
                
                final DBFWriter writer = new DBFWriter(outputFile);
                if (addFields) {
                    final List<DBFField> fields = new ArrayList<DBFField>();
                    for (int i = 0; i < dbfReader.getFieldCount(); i++) {
                        fields.add(dbfReader.getField(i));
                    }
                    writer.setFields(fields.toArray(new DBFField[fields.size()]));
                }

                for (Object[] row : rows) {
                    writer.addRecord(row);
                }
                writer.write();
                System.out.println("Wrote into " + outputFile);
            }
        }
        catch (ParseException exp) {
            // oops, something went wrong
            logger.error("Parsing failed", exp);
        } catch (DBFException e) {
            logger.error("Processing failed", e);
        } catch (FileNotFoundException e) {
            logger.error("Parsing failed.", e);
        } catch (IOException e) {
            logger.error("Loading failed.", e);
        }
        return 0;

    }

    public static void main(String[] args) {
        final int i = new CutDbfMain().run(args);
        if (i != 0)
            System.exit(i);
    }

    public interface Converter {
        Long toLong(Object o);
    }
}
