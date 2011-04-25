package dbf2sql;

import org.apache.commons.cli.*;
import org.apache.log4j.Logger;
import dbf2sql.config.Config;
import dbf2sql.config.ConfigReader;
import dbf2sql.config.InputFile;

import java.io.File;

/**
 * @author octo
 */
public class MainCli {
    final static Logger logger = Logger.getLogger(MainCli.class);

    public static final int DEFAULT_PARALLEL = 4;

    @SuppressWarnings({"OverlyLongMethod"})
    private int run(String[] args) {

        Options options = new Options();

        options.addOption("h", false, "usage");
        options.addOption("n", true, "execution in parallel");

        CommandLineParser parser = new GnuParser();
        final MainExecutor mainExecutor = new MainExecutor();

        try {
            CommandLine line = parser.parse(options, args);
            final String[] fileNames = line.getArgs();
            if (fileNames.length == 0 || line.hasOption('h')) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("processDBF [options] config.xml [config2.xml]", options);
                return 1;
            }

            if (line.hasOption("n"))
                mainExecutor.setParallelTasks(Integer.parseInt(line.getOptionValue("n")));

            int errCnt = 0;
            ConfigReader configReader = new ConfigReader();
            for (String fileName : fileNames) {
                try {
                    final File configFileName = new File(fileName);
                    Config config = configReader.readConfig(configFileName);
                    if (!validateConfig(configFileName, config)) {
                        errCnt++;
                    } else {
                        mainExecutor.addConfig(config);
                    }
                } catch (Exception e) {
                    logger.error("Can't read config: " + fileName, e);
                    errCnt++;
                }
            }
            if (errCnt > 0) {
                logger.error("Fatal: here " + errCnt + " errors, aborting");
                return 1;
            }

        }
        catch (ParseException exp) {
            // oops, something went wrong
            logger.fatal("Parsing failed.  Reason: " + exp.getMessage());
        }

        mainExecutor.run();
        return 0;
    }

    private boolean validateConfig(File configPath, Config config) {
        boolean good = true;
        for (InputFile file : config.getFiles()) {
            String parentDir = configPath.getParent();
            if (parentDir != null)
                parentDir += File.separator;
            else
                parentDir = "";
            file.setFileName(new File(parentDir + file.getFileName()));
            if (!file.getFileName().exists()) {
                logger.error(configPath + ": No such file: " + file);
                good = false;
            } else if (logger.isDebugEnabled())
                logger.debug("...exists " + file);
        }
        if (config.getJdbcParams() == null) {
            logger.error(configPath + ": No jdbc params");
            good = false;
        }
        if (config.getName() == null)
            config.setName(configPath.getName());
        return good;
    }

    public static void main(String[] args) {
        final int i = new MainCli().run(args);
        if (i != 0)
            System.exit(i);
    }

}
