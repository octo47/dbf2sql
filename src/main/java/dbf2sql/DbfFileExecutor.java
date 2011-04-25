package dbf2sql;

import org.apache.log4j.Logger;
import dbf2sql.config.AbstractDBFReaderProvider;
import dbf2sql.config.InputFile;
import dbf2sql.config.impl.FileDBFReaderProvider;
import dbf2sql.core.DbfFieldInfoReader;
import dbf2sql.generator.pgsql.PgSqlDDLGenerator;
import dbf2sql.generator.typeinfo.TableName;
import dbf2sql.generator.typeinfo.FieldInfo;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.concurrent.Callable;
import java.util.Map;
import java.util.List;

/**
 * @author octo
 */
public class DbfFileExecutor implements Callable<ExecutionResult> {

    final static Logger logger = Logger.getLogger(DbfFileExecutor.class);

    private AbstractDBFReaderProvider provider;
    private String schemaName;
    private String tableName;
    private DataSource dataSource;
    private long batchSize = 500;

    private Map<String, String> mappings;

    public void setMappings(Map<String, String> mappings) {
        this.mappings = mappings;
    }

    public DbfFileExecutor(InputFile file, String schemaName, String tableName, DataSource dataSource) {
        this(new FileDBFReaderProvider(file), schemaName, tableName, dataSource);
    }

    public DbfFileExecutor(AbstractDBFReaderProvider provider, String schemaName, String tableName, DataSource dataSource) {
        this.provider = provider;
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.dataSource = dataSource;
    }

    public ExecutionResult call() throws Exception {

        Thread.currentThread().setName(provider.getFile().toString());
        if (logger.isDebugEnabled())
            logger.debug("Thread " + Thread.currentThread() + "file:" + provider.getFile());

        ExecutionResult result = new ExecutionResult(provider.getFile().getFileName().getName());
        final Connection connection = dataSource.getConnection();

        try {
            provider.getDBFReader().setTrimStrings(true);
            if (provider.getFile().getEncoding() != null)
                provider.getDBFReader().setCharactersetName(provider.getFile().getEncoding());

            PgSqlDDLGenerator pgSqlDDLGenerator = new PgSqlDDLGenerator(new TableName(schemaName, tableName));
            DbfFieldInfoReader fieldInfoReader = new DbfFieldInfoReader(provider.getDBFReader());
            List<FieldInfo> fieldInfos = fieldInfoReader.readFields();
            pgSqlDDLGenerator.addFields(fieldInfos);
            pgSqlDDLGenerator.setMappings(mappings);


            connection.setAutoCommit(false);
            // drop if allowed
            if (provider.getFile().isReplace()) {
                final Savepoint savepoint = connection.setSavepoint();
                try {
                    final String dropSql = pgSqlDDLGenerator.dropDDL();
                    if (logger.isDebugEnabled())
                        logger.debug(dropSql);
                    connection.createStatement().execute(dropSql);
                } catch (SQLException se) {
                    connection.rollback(savepoint);
                }
            }
            connection.commit();

            // Create table
            Savepoint savepoint = connection.setSavepoint();
            try {
                final String createSql = pgSqlDDLGenerator.createDDL();

                if (logger.isDebugEnabled())
                    logger.debug(createSql);
                connection.createStatement().execute(createSql);
            } catch (SQLException e) {
                connection.rollback(savepoint);
            }

            savepoint = connection.setSavepoint(this.getClass().getName());
            try {

                final String sql = pgSqlDDLGenerator.insertDDL();
                if (logger.isDebugEnabled())
                    logger.debug(sql);
                PreparedStatement stm = connection.prepareStatement(sql);

                Object[] values;
                int cnt = 0;
                while ((values = provider.getDBFReader().nextRecord()) != null) {
                    int columnIndex = 1;
                    for (int idx = 0; idx < values.length; idx++) {

                        // Если задан маппинг, но для этой колонки нет соответствия, то пропускаем ее
                        if(mappings!=null && mappings.get(fieldInfos.get(idx).getName().toUpperCase())==null)
                            continue;

                        Object value = values[idx];
                        if (value instanceof String) {
                            value = ((String) value).replace("\u0000", "");
                        }
                        stm.setObject(columnIndex++, value);
                    }
                    stm.addBatch();

                    if ((cnt % batchSize) == 0)
                        stm.executeBatch();

                    cnt++;
                    result.incProcessed();
                }
                stm.executeBatch();
            } catch (Exception ex) {
                connection.rollback(savepoint);
                throw ex;
            }

            connection.commit();
            result.setResult(ExecutionResult.Result.OK);
        } catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            connection.close();
        }
        return result;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public long getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(long batchSize) {
        this.batchSize = batchSize;
    }

    public AbstractDBFReaderProvider getProvider() {
        return provider;
    }
}
