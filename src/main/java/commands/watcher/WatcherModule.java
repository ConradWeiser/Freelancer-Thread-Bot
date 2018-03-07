package commands.watcher;

import commands.CommandModule;

public class WatcherModule extends CommandModule {

    public WatcherModule() {
        super(null, "");

        //Add all of the available commands here
        this.addCommand(new AddKeywordCommand(this));
        this.addCommand(new ViewWatchlistCommand(this));
    }

    @Override
    protected String getCompleteName() {

        return "";
    }
}
