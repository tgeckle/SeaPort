import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;

/**
 * Filename: DockRunner.java
 Author: Theresa Geckle
 Date: Dec 1, 2016
 Purpose: 
 */
public class DockRunner extends SwingWorker{
    Dock dock;
    SeaPort port;
    
    public DockRunner(Dock ndock, SeaPort nport) {
        dock = ndock;
        port = nport;
    }
    
    @Override
    public synchronized String doInBackground() throws InterruptedException{
        do {
            JobRunner longest = null;
            double longDuration = 0.0;
            for (Job job : dock.ship.jobs) {
                JobRunner jobbie = new JobRunner(job, port);
                
                if (job.duration > longDuration) {
                    longest = jobbie;
                    longDuration = job.duration;
                    jobbie.setLast(true);
                } else {
                    jobbie.setLast(false);
                }
                
                jobbie.execute();
            }
//            System.out.println("");
//            System.out.println("Longest job: " + longest.job.name);
//            System.out.println("");
            if (longest != null) {
                while (longest.finishTime.plus(60, ChronoUnit.MILLIS).compareTo(Instant.now()) > 0) {

                }

            }
            port.dispatchShips();
            
//                    System.out.println("dock finished");
//                    System.out.println("");
            
//            if (longest != null) {
//                try {
//                    longest.get();
//                    System.out.println("dock finished");
//                    System.out.println("");
//                } catch (ExecutionException exc) {
//
//                }
//            }
        } while (!port.done);
        
        return "";
    }
    
    @Override
    protected synchronized void done() {
        
    }
    
    

}
