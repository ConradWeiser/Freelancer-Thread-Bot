package commands;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Conrad on 6/1/2017.
 */
public class CommandModule {

    private CommandModule parent = null;
    private String name = null;

    protected List<CommandModule> submodules = new ArrayList<CommandModule>();
    protected List<Command> commands = new ArrayList<Command>();

    public CommandModule(CommandModule parent, String name) {

        if(parent == null)
            this.parent = null;

        else this.parent = parent;

        this.name = name;
    }

    protected void addCommand(Command command) {

        this.commands.add(command);
    }

    protected void addSubmodule(CommandModule submodule) {

        this.submodules.add(submodule);
    }

    Command getCommand(String string) throws CommandNotFoundException {

        for(Command command : this.commands) {

            String name = this.getCompleteName() + command.getName();
            if(string.startsWith(name)) {

                return command;
            }
        }

        for(CommandModule module : this.submodules) {

            try {

                Command command = module.getCommand(string);
                return command;
            } catch (CommandNotFoundException ex) {

                //No command, keep looking
                continue;
            }
        }

        throw new CommandNotFoundException();
    }

    protected String getCompleteName() {

        String name = (this.parent == null) ? "" : this.parent.getCompleteName();
        name = name + this.name + " ";
        return name;
    }

    
}
