package dbf2sql.generator.typeinfo;

/**
 * @author octo
 */
public interface TypeInfo {

    String getJdbcType();

    String getSqlType();

    String toDDL(FieldInfo fieldInfo);

    String getEngineName();

    TypeInfo setEngineName(String engineName);

    static Require REQUIRED = new Require(false);
    static Require OPTIONAL = new Require(true);

    static class Require {
        boolean required;

        public boolean isRequired() {
            return required;
        }

        public Require(boolean required) {
            this.required = required;
        }
    }
}
