package core;

import core.enums.ConfigurationVariable;

import java.io.*;
import java.util.Properties;

public class BotConfigurationManager {

    private static BotConfigurationManager instance;

    //Only allow one instance of the ConfigurationManager to exist, to minimize resource use.
    public static BotConfigurationManager getInstance() {

        if(BotConfigurationManager.instance == null)
            BotConfigurationManager.instance = new BotConfigurationManager();

        return BotConfigurationManager.instance;

    }

    String propertyFilePath = System.getenv("APPDATA") + "\\DiscordBot\\bot.conf";
    Properties props = null;

    protected BotConfigurationManager() {

        File configFile = new File(propertyFilePath);
        this.props = new Properties();

        //Attempt to read the current configuration into the bot settings
        try {

            FileReader reader = new FileReader(configFile);
            props.load(reader);

        } catch (IOException ex) {

            System.err.println(String.format("[ERROR] Missing Bot configuration file contents, please input details"
            + "at the following path: %s", propertyFilePath));

            try {
 
                //Populate the configuration
                createEmptyConfigFile();

            } catch (IOException e) {

                System.err.println("[FATAL] An error has occurred trying to create the configuration directory and contents.");
            }
        }
    }

public String getPropertyValue(ConfigurationVariable requestedVariable) {

        switch(requestedVariable) {

            case DISCORD_BOT_API_KEY: return props.getProperty("Discord_Bot_API_Key");
            case MYSQL_USERNAME: return props.getProperty("MYSQL_Username");
            case MYSQL_PASSWORD: return props.getProperty("MYSQL_Password");
            case MYSQL_DATABASE_NAME: return props.getProperty("MYSQL_Database_Name");
            case BOT_COMMAND_TRIGGER: return props.getProperty("Bot_Command_Trigger");
            case LEAGUE_OF_LEGENDS_API_KEY: return props.getProperty("League_of_Legends_API_Key");
        }

        //It's impossible to get here. Return an empty string.
        return "";
}

    private void createEmptyConfigFile() throws IOException {

        //Create the values which the bot requires to run
        props.setProperty("MYSQL_Database_Name", "discordbot");
        props.setProperty("MYSQL_Username", "FillMeIn");
        props.setProperty("MYSQL_Password", "FillMeIn");
        props.setProperty("Discord_Bot_API_Key", "FillMeIn");
        props.setProperty("League_of_Legends_API_Key", "FillMeIn");
        props.setProperty("Bot_Command_Trigger", "!");

        File configFile = new File(propertyFilePath);
        FileWriter writer = new FileWriter(configFile);
        props.store(writer, "Bot Settings");

        writer.close();
    }
}
