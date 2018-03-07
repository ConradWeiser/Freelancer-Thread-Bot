package commands;

import commands.basic.BasicModule;
import commands.user.UserModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Conrad on 6/1/2017.
 */
public class CommandManager {

    private List<CommandModule> modules;

    private CommandManager() {

        this.modules = new ArrayList<>();

        //Insert the modules here
        this.modules.add(new BasicModule());
        this.modules.add(new UserModule());

    }

    private static CommandManager instance = null;

    public static CommandManager getInstance() {
        if (CommandManager.instance == null) {
            CommandManager.instance = new CommandManager();
        }
        return CommandManager.instance;
    }

    public Command getCommand(String message) throws CommandNotFoundException {

        for(CommandModule module : this.modules) {

            try {
                Command command = module.getCommand(message);
                return command;
            } catch (CommandNotFoundException ex) {
                //Go to the next module
            }
        }

        throw new CommandNotFoundException();


    }
}
