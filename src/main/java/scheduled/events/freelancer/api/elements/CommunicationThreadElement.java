package scheduled.events.freelancer.api.elements;

import java.sql.Date;
import java.sql.Timestamp;

public class CommunicationThreadElement {

    public CommunicationThreadElement() {

        //When the element is created, set the timestamp to now
        this.timestamp = new Timestamp(System.currentTimeMillis());

    }

    String threadUrl;
    String threadTitle;
    int replyCount;
    Timestamp timestamp;


    public String getThreadUrl() {
        return threadUrl;
    }

    public void setThreadUrl(String threadUrl) {
        this.threadUrl = threadUrl;
    }

    public String getThreadTitle() {
        return threadTitle;
    }

    public void setThreadTitle(String threadTitle) {
        this.threadTitle = threadTitle;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
