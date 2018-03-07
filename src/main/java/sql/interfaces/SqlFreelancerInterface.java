package sql.interfaces;

import scheduled.events.freelancer.api.elements.ForumThreadElement;
import scheduled.events.freelancer.api.enums.ThreadIdentifier;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlFreelancerInterface extends SqlGenericInterface{

    /**
     * Constructor creating SQL connections, preparing the class for use
     */
    public SqlFreelancerInterface() {
        super();
    }

    /**
     * Method which checks if the communication thread element already exists in the SQL table(s)
     * This searches for uniqueness by URL
     */
    public boolean threadElementExistsInDatabase(ForumThreadElement element) {

        //Build the SQL query
        String query = "SELECT thread_url FROM freelancer_threads WHERE thread_url = " + element.getThreadUrl();

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

    public ForumThreadElement getThreadElementByUrl(String url) {

        ForumThreadElement element = new ForumThreadElement();
        String query = "SELECT * FROM freelancer_threads WHERE thread_url = " + url;

        try {

            ResultSet result = this.executeSelectStatement(query);

            //Given that the URL is unique, there will only be 1 entry. Get that entry. Catch if it doesn't exist.
            result.next();

            //Get all of the values from the ResultSet and fill the ForumThreadElement with the correct data
            element.setThreadTitle(result.getString(1));
            element.setThreadUrl(result.getString(2));
            element.setReplyCount(result.getInt(3));
            element.setTimestamp(result.getTimestamp(4));

            //Get the thread identifier, and parse it into it's java equivalent enumeration
            switch(result.getString(5)) {

                case "communication": element.setThreadIdentifier(ThreadIdentifier.COMMUNICATION_THREADS);
                default: element.setThreadIdentifier(ThreadIdentifier.UNKNOWN);

            }

        } catch (SQLException ex) {

            //TODO: log this somehow

        }

        return element;
    }

    public void addThreadToDatabase(ForumThreadElement element) {

        try {
            PreparedStatement statement = this.connection.prepareStatement("INSERT INTO freelancer_threads (thread_title, thread_url, reply_count, last_seen, thread_type) VALUES(?,?,?,?,?)");
            statement.setString(1, element.getThreadTitle());
            statement.setString(2, element.getThreadUrl());
            statement.setInt(3, element.getReplyCount());
            statement.setTimestamp(4, element.getTimestamp());

            //Handle the enumeration stamp
            switch(element.getThreadIdentifier()) {

                case COMMUNICATION_THREADS: statement.setString(5, "communication");
                case UNKNOWN: statement.setString(5, "unknown");

            }

            System.out.println("Executing");


            statement.executeUpdate();

        } catch (SQLException ex) {

            //TODO: Add the error to the BotLogger whenever that's created
            System.err.println(ex.getMessage());
        }

    }


}
