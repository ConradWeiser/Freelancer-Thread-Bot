package core;

import java.io.*;
import java.util.Properties;

/**
 * Created by Conrad on 6/1/2017.
 */
public class BotConfigurationManager {

    String propertyFilePath = System.getenv("APPDATA") + "\\DiscordBot\\bot.conf";
    public Properties props = null;

    /**
     * Constructor which loads the configuration file and populates the instance with the data.
     * If one does not exist, it creates an empty config file and alerts the user to fill it out with the
     * required information.
     */
    public BotConfigurationManager() {

        //Load in the current properties file if it exists
        File configFile = new File(propertyFilePath);
        this.props = new Properties();

        try {

            FileReader reader = new FileReader(configFile);
            props.load(reader);

        } catch (FileNotFoundException e) {

            //If the file does not required, run the method to create an empty configuration template file
            System.err.println(String.format("Missing bot configuration file, please input details" +
                    " at the following path: %s", propertyFilePath));

            this.createEmptyConfigFile();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public static BotConfigurationManager instance = null;

    /**
     * Use singleton to only allow a single instance of the configuration manager to be active at a time
     */
    public static BotConfigurationManager getInstance() {
        if(BotConfigurationManager.instance == null) {
            BotConfigurationManager.instance = new BotConfigurationManager();
        }

        return BotConfigurationManager.instance;
    }

    /**
     * Method which looks into the configuration file, and retrieves a value from the Key/Value pair
     * @param propertyName the property key value stored in the configuraiton file
     * @return The value paired to the key parameter
     */
    public String getPropertyValue(String propertyName) {

        if(props.getProperty(propertyName) == null) {

            System.err.println("[Warning] Failed to read property value for: " + propertyName);
            return "";
        }

        else
            return props.getProperty(propertyName);
    }

    /**
     * Method which creates an empty configuration file
     */
    private void createEmptyConfigFile() {

        //Create the values whih the system looks for
        props.setProperty("DISCORD_BOT_API_KEY", "fillmein");

        //By default the bot responds to the '!' keyword before a phrase or command
        props.setProperty("BOT_COMMAND_TRIGGER", "!");

        //Save the values as a new file
        File configFile = new File(propertyFilePath);

        try {
            FileWriter writer = new FileWriter(configFile);
            props.store(writer, "Bot Settings");
            writer.close();

        } catch (IOException e) {
            //Thrown when the bot could not create the config file.
            System.err.println(String.format("Failed to write configuration at %s | Does the directory exist?",
                    propertyFilePath));
        }
    }
}
