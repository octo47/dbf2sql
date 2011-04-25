package dbf2sql.generator.typeinfo;

import dbf2sql.DDLGeneratorException;

/**
 * @author octo
 */
public class TypeInfoException extends DDLGeneratorException {

    public TypeInfoException() {
    }

    public TypeInfoException(String message) {
        super(message);
    }

    public TypeInfoException(String message, Throwable cause) {
        super(message, cause);
    }

    public TypeInfoException(Throwable cause) {
        super(cause);
    }

    public Throwable getNativeCause() {
        return this;
    }
}
