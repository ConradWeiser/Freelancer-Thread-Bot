package commands.basic;

import commands.Command;
import commands.CommandModule;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Created by SirensBell on 6/1/2017.
 */
public class ShutdownCommand extends Command {

    public ShutdownCommand(CommandModule module) {

        super("shutdown", module);
    }

    @Override protected void doCommand(MessageReceivedEvent event) {

        //TODO: Do some sort of administration checking. Don't just let anyone shut down the bot. NOT IMPLEMENTED

        System.exit(0);
    }
}
