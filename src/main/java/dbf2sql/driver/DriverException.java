package dbf2sql.driver;

import dbf2sql.DDLGeneratorException;

/**
 * @author octo
 */
public class DriverException extends DDLGeneratorException {
    private static final long serialVersionUID = -6871183059102171104L;


    public DriverException() {
    }

    public DriverException(String message) {
        super(message);
    }

    public DriverException(String message, Throwable cause) {
        super(message, cause);
    }

    public DriverException(Throwable cause) {
        super(cause);
    }

    public Throwable getNativeCause() {
        return getCause();
    }

}
