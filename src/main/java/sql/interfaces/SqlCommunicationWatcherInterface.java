package sql.interfaces;

import scheduled.events.freelancer.api.elements.CommunicationThreadElement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlCommunicationWatcherInterface extends SqlGenericInterface{

    /**
     * Constructor creating SQL connections, preparing the class for use
     */
    public SqlCommunicationWatcherInterface() {
        super();
    }

    /**
     * Method which checks if the communication thread element already exists in the SQL table(s)
     * This searches for uniqueness by URL
     */
    public boolean threadElementExistsInDatabase(CommunicationThreadElement element) {

        //Build the SQL query
        String query = "SELECT thread_url FROM communication_threads WHERE thread_url = " + element.getThreadUrl();

        try {

            ResultSet result = this.executeSelectStatement(query);

            //Check if the ResultSet has an element. If so, the element exists!
            if(result.next())
                return true;
        } catch (SQLException e) {
            //TODO: Log this somehow.
        }

        //If the resultset never had an entry, the thread does not exist
        return false;
    }

    public CommunicationThreadElement getThreadElementByUrl(String url) {

        CommunicationThreadElement element = new CommunicationThreadElement();
        String query = "SELECT * FROM communication_threads WHERE thread_url = " + url;

        try {

            ResultSet result = this.executeSelectStatement(query);

            //Given that the URL is unique, there will only be 1 entry. Get that entry. Catch if it doesn't exist.
            result.next();

            //Get all of the values from the ResultSet and fill the CommunicationThreadElement with the correct data
            element.setThreadTitle(result.getString(1));
            element.setThreadUrl(result.getString(2));
            element.setReplyCount(result.getInt(3));
            element.setTimestamp(result.getTimestamp(4));

        } catch (SQLException ex) {

            //TODO: log this somehow

        }

        return element;
    }

    public void addThreadToDatabase(CommunicationThreadElement element) {

        try {
            PreparedStatement statement = this.connection.prepareStatement("INSERT INTO communication_threads (thread_title, thread_url, reply_count, last_seen) VALUES(?,?,?,?)");
            statement.setString(1, element.getThreadTitle());
            statement.setString(2, element.getThreadUrl());
            statement.setInt(3, element.getReplyCount());
            statement.setTimestamp(4, element.getTimestamp());
            statement.executeUpdate();
        } catch (SQLException ex) {

            //TODO: Add the error to the BotLogger whenever that's created
            System.err.println(ex.getMessage());
        }

    }


}
