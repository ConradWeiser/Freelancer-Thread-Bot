package commands.watcher;

import commands.Command;
import commands.CommandModule;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import sql.interfaces.SqlUserInterface;

import java.awt.*;
import java.util.List;

public class ViewWatchlistCommand extends Command {

    public ViewWatchlistCommand(CommandModule module) {
        super("keywords", module);
    }

    @Override
    protected void doCommand(MessageReceivedEvent event) {

        SqlUserInterface sql = new SqlUserInterface();

        //If the user doesn't exist, warn them, give them instructions, and exit
        if(!sql.userExists(event.getAuthor().getId())) {

            event.getChannel().sendMessage("Please tell me that you'd like to opt-in using `!register` first, please!").queue();
            return;
        }

        //Get all of the available keywords and format them
        List<String> keywords = sql.getUserKeywords(event.getAuthor().getId());

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Registered watchlist keywords");
        eb.setColor(Color.GREEN);
        eb.setDescription("I'm watching out for these keywords! You can add more using the '!watch <phrase>' command!");

        StringBuilder keywordList = new StringBuilder();

        int keywordCounter = 1;

        for(String keyword : keywords) {

            keywordList.append(keywordCounter + "- ");
            keywordList.append(keyword).append("\n");
            keywordCounter++;
        }

        eb.addField("Keywords", keywordList.toString(), false);

        event.getChannel().sendMessage(eb.build()).queue();

    }
}
