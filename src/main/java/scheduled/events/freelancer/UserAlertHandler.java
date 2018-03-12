package scheduled.events.freelancer;

import core.BotCore;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.PrivateChannel;
import scheduled.events.freelancer.api.elements.ForumThreadElement;
import scheduled.events.freelancer.api.elements.SqlUserElement;
import sql.interfaces.SqlFreelancerInterface;
import sql.interfaces.SqlUserInterface;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

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
        //Check each keyword for the user, and check if any of the new, or updated threads has content
        for(Map.Entry<String, List<String>> entry : userKeywordMap.entrySet()) {

            for(String keyword : entry.getValue()) {

                for(ForumThreadElement newThread : newThreads) {

                    if(newThread.getThreadTitle().toLowerCase().contains(keyword.toLowerCase())) {

                        //Hacky way to make sure duplicates aren't added into the resulting string
                        if(newThreadAnnouncement.toString().contains(newThread.getThreadTitle()))
                            continue;

                        newThreadAnnouncement.append(newThread.getThreadTitle()).append("\n");
                        continue;
                    }
                }

                for(ForumThreadElement updatedThread : updatedThreads) {

                    if(updatedThread.getThreadTitle().toLowerCase().contains(keyword.toLowerCase())) {

                        //Hacky way to make sure duplicates aren't added into the resulting string
                        System.out.println("Alerting user: " + entry.getKey() + " of updated thread " + updatedThread.getThreadTitle());
                        if(updatedThreadAnnouncement.toString().contains(updatedThread.getThreadTitle()))
                            continue;

                        updatedThreadAnnouncement.append(updatedThread.getThreadTitle()).append("\n");
                        continue;

                    }
                }
            }

            //If there was nothing new for the user, move on to the next user
            if(newThreadAnnouncement.toString().length() == 0 && updatedThreadAnnouncement.toString().length() == 0) {

                continue;

            }



            //Otherwise, there is content. Put it together for the user.
            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(Color.BLUE);
            builder.setTitle("Thread Alert");

            //Set the time this was observed at
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            builder.setDescription("Observed at: " + dateFormat.format(date));

            if(newThreadAnnouncement.toString().length() > 0) {

                builder.addField("New Thread(s)", newThreadAnnouncement.toString(), false);

            }

            if(updatedThreadAnnouncement.toString().length() > 0) {

                builder.addField("New Reply(s) on following thread(s)", updatedThreadAnnouncement.toString(), false);
            }

            //Send the message to the user
            sendUserDiscordPrivateAlert(entry.getKey(), builder.build());
        }




    }

    private void sendUserDiscordPrivateAlert(String userId, MessageEmbed message) {

        currentDiscordInstance.getUserById(userId).openPrivateChannel().queue((privateChannel ->
                privateChannel.sendMessage(message).queue()));
    }



}
