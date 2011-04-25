package dbf2sql;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import dbf2sql.config.Config;
import dbf2sql.config.InputFile;
import dbf2sql.config.JdbcParams;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author octo
 */
public class MainExecutor {
    final static Logger logger = Logger.getLogger(MainExecutor.class);

    public static final int DEFAULT_PARALLEL = 4;

    private int parallelTasks = DEFAULT_PARALLEL;
    private List<Config> configs = new LinkedList<Config>();

    public void addConfig(Config config) {
        configs.add(config);
    }

    public void run() {
        final ExecutorService executorService = Executors.newFixedThreadPool(parallelTasks);
        final List<Future<ExecutionResult>> futures = new LinkedList<Future<ExecutionResult>>();
        final List<DataSource> dataSources = new LinkedList<DataSource>();

        logger.info("Straring (parallel = " + parallelTasks + ")");

        for (Config config : configs) {
            logger.info("Creating datasource: " + config.getJdbcParams());
            final DataSource dataSource = setupDataSource(config.getJdbcParams(), parallelTasks + 2);
            dataSources.add(dataSource);
            for (InputFile inputFile : config.getFiles()) {
                logger.info("Submiting file: " + inputFile);
                futures.add(executorService.submit(new DbfFileExecutor(inputFile, config.getJdbcParams().getSchema(), inputFile.getFileName().getName(), dataSource)));
            }
        }

        try {
            while (futures.size() > 0) {
                boolean doSleep = true;
                for (Future<ExecutionResult> future : futures) {
                    if (future.isDone()) {
                        try {
                            final ExecutionResult result = future.get();
                            logger.info("done " + result);
                        } catch (ExecutionException e) {
                            logger.error(e.getCause());
                        }
                        futures.remove(future);
                        doSleep = false;
                        break;
                    }
                }
                if (doSleep)
                    Thread.sleep(1000);

                if (logger.isDebugEnabled())
                    logger.debug("now " + futures.size());
            }

        } catch (InterruptedException e) {
            logger.info("Interrupted: terminating", e);
        }
        for (DataSource dataSource : dataSources) {
            shutdownDataSource(dataSource);
        }
        executorService.shutdownNow();
    }

    public static DataSource setupDataSource(JdbcParams params, int maxActive) {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(params.getDriver());
        ds.setUsername(params.getUser());
        ds.setPassword(params.getPassword());
        ds.setUrl(params.getUrl());
        ds.setMaxActive(maxActive);
        ds.setMaxIdle(0);
	ds.setDefaultAutoCommit(false);
        return ds;
    }

    public static void shutdownDataSource(DataSource ds) {
        BasicDataSource bds = (BasicDataSource) ds;
        try {
            bds.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Config> getConfigs() {
        return Collections.unmodifiableList(configs);
    }

    public int getParallelTasks() {
        return parallelTasks;
    }

    public void setParallelTasks(int parallelTasks) {
        this.parallelTasks = parallelTasks;
    }

}
