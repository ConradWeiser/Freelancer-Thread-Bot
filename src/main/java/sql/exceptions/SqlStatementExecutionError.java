package sql.exceptions;

public class SqlStatementExecutionError extends Throwable {

    public SqlStatementExecutionError(String error) {

        super(error);
    }
}
