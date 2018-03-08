package commands.watcher;

import com.sun.deploy.util.ArrayUtil;
import commands.Command;
import commands.CommandModule;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import sql.interfaces.SqlUserInterface;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class RemoveKeywordCommand extends Command{

    public RemoveKeywordCommand(CommandModule module) {

        super("remove", module);
    }

    @Override
    protected void doCommand(MessageReceivedEvent event) {

        SqlUserInterface sql = new SqlUserInterface();

        //If the user doesn't exist, warn them, give them instructions, and exit
        if(!sql.userExists(event.getAuthor().getId())) {

            event.getChannel().sendMessage("Please tell me that you'd like to opt-in using `!register` first, please!").queue();
            return;
        }

        //Get all of the numeric values delimited by spaces, and put it into an array
        String[] removalValuesArray = event.getMessage().getContentRaw().trim().replaceAll("[^0-9.\\s]", "").split(" ");
        Vector<String> removalValuesList = new Vector<>(Arrays.asList(removalValuesArray));

        removalValuesList.removeElementAt(0);

        List<String> removalKeywords = new Vector<>();

        //Get all of the available keywords
        List<String> keywords = sql.getUserKeywords(event.getAuthor().getId());

        for(int i = 0; i < removalValuesList.size(); i++) {

            try {

                if (keywords.get(Integer.parseInt(removalValuesList.get(i))) != null) {

                    removalKeywords.add(keywords.get(Integer.parseInt(removalValuesList.get(i))));
                    continue;

                }

            } catch (ArrayIndexOutOfBoundsException ex) {

                //If we catch this, then we have gotten a number that doesn't exist. Ignore it.
                ex.printStackTrace();
                continue;
            }
        }


        //For all of the keywords to remove.. Remove them and add them to the output formatting builder
        StringBuilder builder = new StringBuilder();
        for(String keyword : removalKeywords) {

            sql.removeKeywordFromUser(event.getAuthor().getId(), keyword);
            builder.append("- ").append(keyword).append("\n");

        }

        //Print results to the user
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Keywords removed");
        eb.setColor(Color.RED);
        eb.setDescription("Removed " + removalKeywords.size() + " watch parameter(s)!");
        eb.addField("Removed keyphrases..", builder.toString(), false);

        event.getChannel().sendMessage(eb.build()).queue();



    }
}
