package commands;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Conrad on 6/1/2017.
 */
public abstract class Command {

    private String name;
    private CommandModule module;

    public Command(String name, CommandModule module) {
        this.name = name;
        this.module = module;
    }

    protected List<String> getArguments(Message message) {

        //Get the contents of the message after the initial command
        String contents = message.getContentRaw().substring(
                this.getPath().length() + 1);

        //Split the string on any comma characters
        List<String> list = Arrays.asList(contents.split(","));

        //Check each element for a leading whitespace character
        for(int i = 0; i < list.size(); i++) {

            //remove the first character if it is whitespace
            String temp = list.get(i);
            while(temp.startsWith(" ")) {
                temp = temp.substring(1);
            }

            //re-set the element in the list
            list.set(i, temp);
        }

        //return the list
        return list;

    }

    public String getName() {

        return this.name;
    }

    public String getPath() {

        return this.module.getCompleteName() + " " + this.getName();
    }

    protected abstract void doCommand(MessageReceivedEvent event);

    void execute(MessageReceivedEvent event) {

        //log the message
        String str = String.format("[Command] User %s called command %s",
                event.getAuthor().getName(), event.getMessage().getContentRaw().substring(1));
        System.out.println(str);

        //Execute the current command
        this.doCommand(event);
    }
}
