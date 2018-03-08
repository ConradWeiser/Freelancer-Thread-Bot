package scheduled.events.freelancer.api.elements;

import scheduled.events.freelancer.api.enums.ThreadIdentifier;

import java.sql.Date;
import java.sql.Timestamp;

public class ForumThreadElement {

    public ForumThreadElement() {

        //When the element is created, set the timestamp to now
        this.timestamp = new Timestamp(System.currentTimeMillis());

    }

    String threadUrl;
    String threadTitle;
    int replyCount;
    Timestamp timestamp;
    ThreadIdentifier threadIdentifier;

    public void setTimestampToNow() {

        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public ThreadIdentifier getThreadIdentifier() {
        return threadIdentifier;
    }

    public void setThreadIdentifier(ThreadIdentifier threadIdentifier) {
        this.threadIdentifier = threadIdentifier;
    }

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
