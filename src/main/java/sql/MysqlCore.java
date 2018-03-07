package sql;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import core.BotConfigurationManager;
import core.enums.ConfigurationVariable;

import java.sql.Connection;

public class MysqlCore {


    protected Connection connection;

    /**
     * Constructor which creates the SQL connection if an instance of this class doesn't already exist.
     */
    protected MysqlCore() {

       createSqlInstanceConnection();

    }

    /**
     * Method which checks a ResultSet to see if it's empty or not.
     */
    public boolean isResultSetEmpty(ResultSet set) {

        //Check the ResultSet. If it's empty, log an error to the logger and return false
        try {

            if(!set.next()) {

                //TODO: Log an error to the Discord Logging System
                return false;
            }

            //Otherwise, there are indeed results. Return true.
            return true;

        } catch (SQLException e) {

            //This is redundant and needs fixed. But log an error and return false here too.
            return false;
        }
    }

    /**
     * Method which creates an SQL connection, and stores the instance
     */
    private void createSqlInstanceConnection() {

        //Attempt to load the MYSQL driver
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {

            System.out.println("[Error] Where is your MySQL JDBC Driver?");
            e.printStackTrace();
            return;
        }


        //Attempt to connect to the database.
        try {

            BotConfigurationManager configManager = core.BotConfigurationManager.getInstance();

            this.connection = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/"
                                    + configManager.getPropertyValue(ConfigurationVariable.MYSQL_DATABASE_NAME)
                                    + "?useSSL=false"
                            , configManager.getPropertyValue(ConfigurationVariable.MYSQL_USERNAME)
                            , configManager.getPropertyValue(ConfigurationVariable.MYSQL_PASSWORD));
        }

        catch (SQLException e) {

            System.out.println("Connection Failed! Check console output for debugging");
            e.printStackTrace();
            return;
        }

        //If the connection is null for whatever reason, warn the user
        if(connection == null) {

            System.err.println("[Warning] Connection to SQL database failed for an unknown reason");
        }


    }
}




