package dbf2sql;

/**
 * Base exception class fro loader subsystem
 *
 * @author octo
 */
public abstract class DDLGeneratorException extends RuntimeException {
    private static final long serialVersionUID = -9173042527857629048L;

    public DDLGeneratorException() {
    }

    public DDLGeneratorException(String message) {
        super(message);
    }

    public DDLGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public DDLGeneratorException(Throwable cause) {
        super(cause);
    }

    /**
     * Lookup for exception of the desired type.
     *
     * @param clz type to find
     * @return exception instance or null
     */
    public Throwable findException(Class clz) {
        for (Throwable e = getCause(); e != null; e = e.getCause()) {
            if (e.getClass().isAssignableFrom(clz)) {
                return e;
            }
        }
        return null;
    }

    /**
     * Lookup for native exception for exception producer.
     * This method is informational for user notification mostly.
     *
     * @return native cause
     */
    abstract public Throwable getNativeCause();

}
