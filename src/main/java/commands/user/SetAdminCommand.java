package commands.user;

import commands.Command;
import commands.CommandModule;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import sql.enums.UserAdministratorState;
import sql.interfaces.SqlUserInterface;

public class SetAdminCommand extends Command {

    public SetAdminCommand(CommandModule module) {super("setadmin", module);}

    @Override
    protected void doCommand(MessageReceivedEvent event) {


        //First, reject this command from being used if the user calling it is not an administrator him/herself
        SqlUserInterface userInterface = new SqlUserInterface();
        if(!userInterface.isUserAdmin(event.getAuthor().getId())) {

            //Alert the user and exit
            event.getChannel().sendMessage("You must be an administrator yourself to change the status of other users.").queue();
            return;
        }

        //Verify that there were mentioned users to promote.
        if(event.getMessage().getMentionedMembers().size() == 0) {

            event.getChannel().sendMessage("Be sure to @mention the users you'd like to promote using this command! I'm not seeing any!").queue();
            return;
        }

        //If there are indeed users, promote them all to administrator
        int promotedUserCount = 0;

        for(Member memberToPromote : event.getMessage().getMentionedMembers()) {

            //Make sure that the user in question is currently registered as a member. If not, register them
            if(!userInterface.userExists(memberToPromote.getUser().getId())) {

                userInterface.registerDiscordUser(memberToPromote.getUser().getId(), memberToPromote.getUser().getName(), memberToPromote.getDefaultChannel().getId());
            }

            //Set the user as an administrator
            userInterface.setUserAdminStatus(memberToPromote.getUser().getId(), UserAdministratorState.ADMIN);
            promotedUserCount++;
        }

        //Alert the channel what just happened
        event.getChannel().sendMessage("Successively promoted " + promotedUserCount + " user(s) to administrator status.").queue();

    }
}
