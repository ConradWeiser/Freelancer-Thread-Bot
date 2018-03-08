package commands.user;

import commands.Command;
import commands.CommandModule;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import sql.interfaces.SqlUserInterface;

public class RegisterUserCommand extends Command {


    public RegisterUserCommand(CommandModule module) {
        super("register", module);
    }

    @Override
    protected void doCommand(MessageReceivedEvent event) {

        //Get the discordUserId and Username of the user requesting to be registered
        String discordUsername = event.getAuthor().getName();
        String discordId = event.getAuthor().getId();

        //Check to see if the user already exists
        SqlUserInterface userInterface = new SqlUserInterface();
        if(userInterface.userExists(discordId)) {

            //Send a message to the users DM inbox specifying such
            event.getChannel().sendMessage("You are already registered with DGC-Watcher!").queue();
            return;

        }

        //If the user does not exist, add them to the database
        userInterface.registerDiscordUser(discordId, discordUsername);
        event.getChannel().sendMessage("You've been registered with DGC-Watcher!\nCommands are now available for use.").queue();

    }
}
