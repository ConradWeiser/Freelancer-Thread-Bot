package commands.user;

import commands.CommandModule;

public class UserModule extends CommandModule {

    public UserModule() {
        super(null, "");

        //Add all of the available commands
        this.addCommand(new RegisterUserCommand(this));
        this.addCommand(new SetAdminCommand(this));
    }

    @Override
    protected String getCompleteName() {

        return "";
    }


}
