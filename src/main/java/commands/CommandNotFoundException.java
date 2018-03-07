package commands;

/**
 * Created by SirensBell on 6/1/2017.
 */
@SuppressWarnings("serial")
public class CommandNotFoundException extends Exception {

    @Override public String getMessage() {
        return "The specified command was not found";

    }


}
