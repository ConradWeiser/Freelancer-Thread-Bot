package commands.watcher;

import commands.Command;
import commands.CommandModule;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import sql.interfaces.SqlUserInterface;

public class AddKeywordCommand extends Command{

    public AddKeywordCommand(CommandModule module) {
        super("watch", module);
    }

    @Override
    protected void doCommand(MessageReceivedEvent event) {

        SqlUserInterface sql = new SqlUserInterface();

        //If the user doesn't exist, warn them, give them instructions, and exit
        if(!sql.userExists(event.getAuthor().getId())) {

            event.getChannel().sendMessage("Please tell me that you'd like to opt-in using `!register` first, please!").queue();
            return;
        }

        //Parse out the keyword phrase
        String keywordPhrase = event.getMessage().getContentRaw().substring(7);

        //Insert the keyword phrase
        sql.addKeywordToUser(event.getAuthor().getId(), keywordPhrase);

        //Alert the user
        event.getChannel().sendMessage("Keyword: `" + keywordPhrase + "` has been added to your watchlist!").queue();

    }
}
