package core.utility;

import java.io.File;

/**
 * A utility class hacked together, with the intention of giving a correct path to the directory containing the bot
 * settings depending on operating system
 */
public class FilePathManager {

    private static final String windowsPath = System.getenv("APPDATA") + "\\DiscordBot\\bot.conf";
    private static final String linuxPath = System.getProperty("user.home") + "/botconfig.cfg";

    public static File getBotConfigFile() {

        //Are we a windows operating system?
        if(System.getProperty("os.name").startsWith("Windows")) {

            //Return a windows file path
            return new File(windowsPath);
        }

        //Otherwise, we must be running on linux
        else {

            return new File(linuxPath);
        }


    }
}
