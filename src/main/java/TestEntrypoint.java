import api.ApiInterface;

public class TestEntrypoint {

    public static void main(String[] args) {

        ApiInterface api = new ApiInterface();
        api.getCommunicationChannelThreads();

    }
}
