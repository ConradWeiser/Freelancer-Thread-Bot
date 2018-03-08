package scheduled.events.freelancer.api.elements;

public class SqlUserElement {

    int id;
    String discordId;
    String discordName;
    String privateChannelId;
    boolean isAdmin;

    public String getPrivateChannelId() {
        return privateChannelId;
    }

    public void setPrivateChannelId(String privateChannelId) {
        this.privateChannelId = privateChannelId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiscordId() {
        return discordId;
    }

    public void setDiscordId(String discordId) {
        this.discordId = discordId;
    }

    public String getDiscordName() {
        return discordName;
    }

    public void setDiscordName(String discordName) {
        this.discordName = discordName;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
