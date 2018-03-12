package scheduled.events.freelancer.threads;

import scheduled.events.freelancer.UserAlertHandler;
import scheduled.events.freelancer.api.ApiInterface;
import scheduled.events.freelancer.api.elements.ForumThreadElement;
import sql.interfaces.SqlFreelancerInterface;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class ThreadScannerRunnable implements Runnable{

    /**
     * URL to the discoverygc section which this scanner is responsible of parsing data for
     */
    private String threadUrl;

    private ApiInterface freelancerApi;

    SqlFreelancerInterface sql;

    /**
     * A list which is populated whenever there is a 'new' thread. (Viewer count is 0, and it hasn't been discovered
     * before)
     */
    List<ForumThreadElement> newThreads;

    List<ForumThreadElement> updatedThreads;

    /**
     * The instance of the user handler managing this threads messages
     */
    UserAlertHandler userHandler;

    public ThreadScannerRunnable(String threadUrl, UserAlertHandler handler) {

        this.threadUrl = threadUrl;
        this.freelancerApi = new ApiInterface();
        this.sql = new SqlFreelancerInterface();
        this.newThreads = new Vector<>();
        this.updatedThreads = new Vector<>();
        this.userHandler = handler;

    }


    //TODO: Consider doing more operations on the SQL database instead of doing it here.
    @Override
    public void run() {

        List<ForumThreadElement> discoveredElements;

        //Get all of the new threads given the supplied threadUrl
        try {
            discoveredElements = freelancerApi.getSectionThreads(this.threadUrl);

        } catch (IOException ex) {

            //TODO: Add a bot error logger call
            //This catches when there was a failure communicating to the discoverygc website. Exit early.
            return;
        }

        // -------------------------------------------------------
        //Compare each of the available elements to the SQL database. If there are any 'new' entries, sort it as such
        for(Iterator<ForumThreadElement> iter = discoveredElements.listIterator(); iter.hasNext(); )  {

            ForumThreadElement currentIteration = iter.next();

            //If the thread doesn't exist, add it to the database, and move it to the sorted list
            if(!sql.threadElementExistsInDatabase(currentIteration)) {

                this.newThreads.add(currentIteration);
                iter.remove();
                continue;

            }

            //Otherwise, get the existing element for comparison
            ForumThreadElement existingElement = sql.getThreadElementByUrl(currentIteration.getThreadUrl());

            //Verify if it's new, or old, by comparing the view counts
            if(currentIteration.getReplyCount() > existingElement.getReplyCount()) {

                this.updatedThreads.add(currentIteration);
                //Update the database view count
                sql.updateExistingThread(currentIteration);
                iter.remove();
                continue;

            }

        }

        // ---------------------------------------------
        //Add all of the new threads to the database
        addNewThreadsToDatabase();

        //Update all of the seen threads in the database
        updateSeenThreadsInDatabase();

        //Alert everyone relevent that there was a change
        userHandler.alertUsers(updatedThreads, newThreads);

        //Clear the two lists now that we've used them both
        updatedThreads.clear();
        newThreads.clear();

    }

    private void updateSeenThreadsInDatabase() {

        for(ForumThreadElement element : updatedThreads) {

            System.out.println("Updating thread: " + element.getThreadTitle());
            sql.updateExistingThread(element);
        }
    }

    private void addNewThreadsToDatabase() {

        for(ForumThreadElement element : newThreads) {

            System.out.println("Adding new thread: " + element.getThreadTitle());
            sql.addThreadToDatabase(element);
        }
    }
}
