package dbf2sql.core;

import javadbf.DBFException;
import dbf2sql.driver.DriverException;

/**
 * @author octo
 */
public class DbfDriverException extends DriverException {

    public DbfDriverException() {
    }

    public DbfDriverException(String message) {
        super(message);
    }

    public DbfDriverException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbfDriverException(Throwable cause) {
        super(cause);
    }

    @Override
    public Throwable getNativeCause() {
        return findException(DBFException.class);
    }
}
