package scheduled.events.freelancer;

import scheduled.events.freelancer.threads.ThreadScannerRunnable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class CommunicationThreadScanner {

    private final String communicationSectionUrl = "https://discoverygc.com/forums/archive/index.php?forum-59.html";

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private  UserAlertHandler userHandler;

    public CommunicationThreadScanner() {

        this.startThreadScannerService();
    }

    private void startThreadScannerService() {

        this.userHandler = new UserAlertHandler();

        final Runnable reporter = new ThreadScannerRunnable(communicationSectionUrl, userHandler);

        //A handler variable we can use to access the reporter
        //TODO: Implement control methods
        final ScheduledFuture<?> reportHandler = scheduler.scheduleAtFixedRate(reporter, 3, 10, TimeUnit.SECONDS);
    }
}
