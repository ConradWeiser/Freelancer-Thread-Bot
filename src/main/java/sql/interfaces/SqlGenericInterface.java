package sql.interfaces;

import sql.MysqlCore;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlGenericInterface extends MysqlCore {

    public SqlGenericInterface() {

        //Init the SqlCore parent class
        super();

    }

    /**
     * Method creating a statement which returns a response from the SQL database from a string
     */
    public ResultSet executeSelectStatement(String query)  {

        ResultSet resultSet = null;

        try {

            Statement stmt = this.connection.createStatement();
            resultSet = stmt.executeQuery(query);

            //Check to see if the ResultSet is null. If so, display a warning
            //TODO: Set this up with the bot logger
            if(resultSet == null)
                System.err.println("[Warning] ResultSet from SQL transaction came up null");

        } catch (SQLException e) {

            System.err.println(e.getMessage());

        }

        return resultSet;
    }

    /**
     * Method executing an update statement, not requiring a response
     */
    public void executeUpdateStatement(String query) {

        try {

            Statement stmt = this.connection.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            //TODO: Set this up with the bot logger
            System.err.println("[Warning] Failed to execute SQL update statement");
            e.printStackTrace();
        }
    }

    /**
     * A stubbed method executing an insert statement, not requiring a response
     */
    public void executeInsertStatement(String query) {
        this.executeUpdateStatement(query);
    }

}
