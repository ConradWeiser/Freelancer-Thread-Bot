package scheduled.events.freelancer;

import core.BotCore;
import net.dv8tion.jda.core.JDA;
import scheduled.events.freelancer.api.elements.ForumThreadElement;
import scheduled.events.freelancer.api.elements.SqlUserElement;
import sql.interfaces.SqlFreelancerInterface;
import sql.interfaces.SqlUserInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class UserAlertHandler {

    private SqlUserInterface userInterface;
    private SqlFreelancerInterface freelancerInterface;
    private JDA currentDiscordInstance;

    public UserAlertHandler() {

        userInterface = new SqlUserInterface();
        freelancerInterface = new SqlFreelancerInterface();
        this.currentDiscordInstance = BotCore.getJDA();
    }

    public void alertUsers(List<ForumThreadElement> updatedThreads, List<ForumThreadElement> newThreads) {

        StringBuilder newThreadAnnouncement = new StringBuilder();
        StringBuilder updatedThreadAnnouncement = new StringBuilder();

        /**
         * A map formatted as the following: <DiscordID, List<All User Keywords>>
         */
        Map<String, List<String>> userKeywordMap = new HashMap<>();

        //Get all of the registered discord users
        List<SqlUserElement> users = userInterface.getAllDiscordUsers();

        //For each of the users, get all of their keywords
        for(SqlUserElement user : users) {

            List<String> keywords = userInterface.getUserKeywords(user.getDiscordId());

            //Fill a map entry
            userKeywordMap.put(user.getDiscordId(), keywords);

        }

        // --------------------------------------
        //Check to see if any of the NEW threads match something in the users
        sendUserDiscordPrivateAlert("420999545850429442", "Fuck meeee");

    }

    private void sendUserDiscordPrivateAlert(String discordPrivateChannelId, String message) {

        currentDiscordInstance.getPrivateChannelById(discordPrivateChannelId).sendMessage(message).queue();
        System.out.println("Queued");
    }


}
