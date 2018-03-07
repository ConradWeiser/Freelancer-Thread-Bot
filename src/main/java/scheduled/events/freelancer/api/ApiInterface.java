package scheduled.events.freelancer.api;

import scheduled.events.freelancer.api.elements.CommunicationThreadElement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * Class designed to interface with http://discoverygc.com/forums/####### and retrieve data
 */
public class ApiInterface {


    /**
     * Method which gets all of the recent posts in https://discoverygc.com/forums/archive/index.php?forum-59.html
     * and stores them all fuck
     */
    public List<CommunicationThreadElement> getCommunicationChannelThreads() throws IOException {

        /**
         * Object containing the HTML response for the DiscoveryGC Communication(s) section
         */
        Document doc = Jsoup.connect("https://discoverygc.com/forums/archive/index.php?forum-59.html").get();

        List<CommunicationThreadElement> threadList = new Vector<>();

        //Get all of the thread data. This contains 2 elements. Pinned threads, and normal threads.
        Elements threads = doc.getElementsByClass("threadlist");

        for (Element sectionElement : threads) {

            //Get each individual thread for each individual section
            for(Element threadElement : sectionElement.getElementsByTag("li")) {

                CommunicationThreadElement communicationElement = new CommunicationThreadElement();

                //Get the URL for this comms message. There will only ever be one 'a', thus we pick the first
                String threadUrl = threadElement.select("a").first().attr("href");
                communicationElement.setThreadUrl(threadUrl);

                //Get the thread title
                String threadTitle = threadElement.select("a").first().text();
                communicationElement.setThreadTitle(threadTitle);

                //Get the reply count, use regex and delete all non-digits
                //TODO: Maybe consider replacing regex with a linear search for performance?
                String replyHtmlText = threadElement.getElementsByClass("replycount").text();
                communicationElement.setReplyCount(Integer.valueOf(replyHtmlText.replaceAll("\\D+","")));

                //Add the element into the list of elements
                threadList.add(communicationElement);

            }

        }

        return threadList;

    }

}
