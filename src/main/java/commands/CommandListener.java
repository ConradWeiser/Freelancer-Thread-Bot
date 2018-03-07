package commands;

import core.BotConfigurationManager;
import core.enums.ConfigurationVariable;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Created by Conrad on 6/1/2017.
 */
public class CommandListener extends ListenerAdapter{

    private CommandManager manager = null;

    @Override public void onMessageReceived(MessageReceivedEvent event) {

        Message message = event.getMessage();
        String contents = message.getContentRaw();

        //If the message does not start with the command trigger set in the configuration file, ignore it
        if(!contents.startsWith(BotConfigurationManager.getInstance()
                .getPropertyValue(ConfigurationVariable.BOT_COMMAND_TRIGGER))) {

            return;

        }

        this.manager = CommandManager.getInstance();
        contents = contents.substring(1, contents.length());

        try {

            Command command = this.manager.getCommand(contents);
            command.execute(event);

        } catch (CommandNotFoundException ex) {

            //Handle if the command was not found
            System.out.println(String.format("[Command] User %s entered invalid command %s",
                    message.getAuthor().getName(), contents));
        }
    }
}
