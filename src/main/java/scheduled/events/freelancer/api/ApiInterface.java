package scheduled.events.freelancer.api;

import scheduled.events.freelancer.api.elements.ForumThreadElement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import scheduled.events.freelancer.api.enums.ThreadIdentifier;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * Class designed to interface with http://discoverygc.com/forums/####### and retrieve data
 */
public class ApiInterface {


    /**
     * Method which gets all of the recent posts in https://discoverygc.com/forums/archive/index.php?forum-59.html
     */
    public List<ForumThreadElement> getSectionThreads(String url) throws IOException {

        /**
         * Object containing the HTML response for the DiscoveryGC thread list section
         */
        Document doc = Jsoup.connect(url).get();

        List<ForumThreadElement> threadList = new Vector<>();

        //Get all of the thread data. This contains 2 elements. Pinned threads, and normal threads.
        Elements threads = doc.getElementsByClass("threadlist");

        for (Element sectionElement : threads) {

            //Get each individual thread for each individual section
            for(Element currentThread : sectionElement.getElementsByTag("li")) {

                ForumThreadElement threadElement = new ForumThreadElement();

                //Get the URL for this thread. There will only ever be one 'a', thus we pick the first
                String threadUrl = currentThread.select("a").first().attr("href");
                threadElement.setThreadUrl(threadUrl);

                //Get the thread title
                String threadTitle = currentThread.select("a").first().text();
                threadElement.setThreadTitle(threadTitle);

                //Get the reply count, use regex and delete all non-digits
                //TODO: Maybe consider replacing regex with a linear search for performance?
                String replyHtmlText = currentThread.getElementsByClass("replycount").text();
                threadElement.setReplyCount(Integer.valueOf(replyHtmlText.replaceAll("\\D+","")));

                //Set the enum thread identifier, if one exists for the supplied URL
                ThreadIdentifier td = getCorrectIdentifierFromUrl(url);
                threadElement.setThreadIdentifier(td);

                //Add the element into the list of elements
                threadList.add(threadElement);

            }

        }

        return threadList;

    }

    private ThreadIdentifier getCorrectIdentifierFromUrl(String url) {

        //Loop over all possible enum values
        for(ThreadIdentifier identifier : ThreadIdentifier.values()) {

            //If the value of the enum matches the url, we've found the correct value
            if(getUrlFromThreadIdentifierEnum(identifier).equals(url)) {

                return identifier;
            }

        }

        //Otherwise, one doesn't exist. Return the unknown type
        return ThreadIdentifier.UNKNOWN;
    }

    /**
     * Static method which converts a thread identifier enum to that thread location URL
     * @param identifier
     * @return a resolvable URL
     */
    static public String getUrlFromThreadIdentifierEnum(ThreadIdentifier identifier) {

        switch(identifier) {

            case COMMUNICATION_THREADS: return "https://discoverygc.com/forums/archive/index.php?forum-59.html";
            default: return "";
        }
    }

}
