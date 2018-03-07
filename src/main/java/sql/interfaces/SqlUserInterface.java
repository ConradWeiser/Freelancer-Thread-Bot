package sql.interfaces;

import sql.enums.UserAdministratorState;
import sql.exceptions.SqlStatementExecutionError;
import sql.exceptions.UserNotAdministratorException;
import sql.exceptions.UserNotExistsException;

import java.sql.ResultSet;
import java.sql.SQLException;

import static sql.enums.UserAdministratorState.*;

public class SqlUserInterface extends SqlGenericInterface {

    /**
     * Constructor which creates the SQL connections, and prepares the class for use
     */
    public SqlUserInterface() {
        super();
    }

    /**
     * Method which checks to see if a Discord User exists in the database already. If not, their user is created
     */
    public boolean userExists(String userId) {

        //Build the SQL query
        String query = "SELECT discord_name FROM discord_users WHERE snowflake_id = " + userId;

        try {

            ResultSet result = this.executeSelectStatement(query);

            //Check if the ResultSet has an element. If so, the user exists
            if(result.next()) {
                return true;
            }
        } catch (SQLException e) {
            //TODO: Set this up to work with the BotLogger
            System.err.println(e.getMessage());
        }

        //If the resultSet never had an entry, the user does not exist.
        return false;
    }

    /**
     * Method which checks if a registered user is an Administrator or not, given their unique discord ID
     * @param userId The unique snowflake ID referencing a discord user account
     */
    public boolean isUserAdmin(String userId)  {

        //Build the SQL query
        String query = "SELECT is_admin FROM discord_users WHERE snowflake_id = " + userId;

        try {

            ResultSet result = this.executeSelectStatement(query);

            //Check if the ResultSet is empty. If so, log an error and return false for safety
            if(!result.next()) {
                //TODO: Log an error to the Discord Logging System
                return false;
            }

            //Set the ResultSet back to it's beginning and extrapolate it's data
            return result.getBoolean(1);


        }  catch (SQLException e) {

            //Something went wrong with the ResultSet. You should never see this.
            //TODO: Change this to register with the Discord Bot Logger
            System.err.println("[Warning] SQL ResultSet object error in isUserAdmin function.");
        }

        //If for some reason we make it down here, we claim the user isn't an admin.
        return false;
    }

    /**
     * Method which sets a user with a given userID to either be an administrator, or not an administrator
     */
    public void setUserAdminStatus(String userId, UserAdministratorState desiredState)  {

        //Get whether the user should be set as an admin, or not an admin. (Defaulting to not)
        int desiredStateNumeric = 0;

        if(desiredState == UserAdministratorState.ADMIN)
            desiredStateNumeric = 1;

        //Create the SQL query
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("UPDATE discord_users SET is_admin = ");
        queryBuilder.append(desiredStateNumeric);
        queryBuilder.append(" WHERE snowflake_id = ");
        queryBuilder.append(userId);

        //Execute the query
        this.executeUpdateStatement(queryBuilder.toString());

    }

    /**
     * Method registering a user to the SQL database
     */
    public void registerDiscordUser(String userId, String discordName) {

        //Create the SQL query
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("INSERT INTO discord_users (snowflake_id, discord_name) VALUES (");
        queryBuilder.append(userId);
        queryBuilder.append(", \"");
        queryBuilder.append(discordName);
        queryBuilder.append("\")");

        System.out.println(queryBuilder.toString());

        //Execute the SQL statement
        this.executeInsertStatement(queryBuilder.toString());
    }


}
