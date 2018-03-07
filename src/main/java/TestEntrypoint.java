import scheduled.events.freelancer.api.ApiInterface;
import scheduled.events.freelancer.api.elements.ForumThreadElement;
import scheduled.events.freelancer.api.enums.ThreadIdentifier;
import sql.interfaces.SqlFreelancerInterface;

import java.io.IOException;
import java.util.List;

public class TestEntrypoint {

    public static void main(String[] args) {

        List<ForumThreadElement> elements;
        ApiInterface api = new ApiInterface();

        try {
            elements = api.getSectionThreads(ApiInterface.getUrlFromThreadIdentifierEnum(ThreadIdentifier.COMMUNICATION_THREADS));

            SqlFreelancerInterface sql = new SqlFreelancerInterface();

            for(ForumThreadElement element : elements) {

                sql.addThreadToDatabase(element);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
