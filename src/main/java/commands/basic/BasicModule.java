package commands.basic;

import commands.CommandModule;

/**
 * Created by SirensBell on 6/1/2017.
 */
public class BasicModule extends CommandModule {

    public BasicModule() {

        super(null, "");

        //Add commands here
        this.addCommand(new ShutdownCommand(this));
    }

    @Override protected String getCompleteName() {

        return "";
    }
}
