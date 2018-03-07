package core;

import commands.CommandListener;
import core.enums.ConfigurationVariable;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;

/**
 * Created by Conrad on 6/1/2017.
 */
public class BotCore {

    private JDA jda;
    private BotConfigurationManager configurationManager = null;

    public BotCore() {

        //Create the Configuration mangaer and Bot Core instance.
        this.configurationManager = BotConfigurationManager.getInstance();
        this.createInstance();

    }

    /**
     * Method which creates the instance of the bot. Calling this function starts the functionality
     * of everything else within the bot framework
     */
    private void createInstance() {

        try {
            this.jda = new JDABuilder(AccountType.BOT)
                    .setToken(configurationManager.getPropertyValue(ConfigurationVariable.DISCORD_BOT_API_KEY))
                    .buildBlocking();

        } catch (InterruptedException e) {

            System.err.println("[Fatal] Interrupted while trying to connect to Discord");
            System.exit(1);

        } catch (LoginException e) {

            System.err.println("[Fatal] Unable to connect with the specified API token");
            System.exit(1);

        }
    }

    /**
     * Method which directs the Discord Bot to the appropriate message handlers.
     */
    private void attachCallbacks() {

        this.jda.addEventListener(new CommandListener());
    }

    /**
     * Shutdown hook making sure that when the shutdown command is called, cleanup operations are completed first
     */
    private void shutdown() {

        this.jda.shutdown();

    }

    /**
     * Entry point to the program.
     * Create the bot instance, and attach the shutdown hook for clean shutdowns.
     */
    public static void main(String[] args) {

        //Create the new instance
        BotCore bot = new BotCore();

        //Add the shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override public void run() {
                bot.shutdown();
            }
        });
    }

}
