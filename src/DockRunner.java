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
            for (Job job : dock.ship.jobs) {
                JobRunner jobbie = new JobRunner(job, port);
                jobbie.execute();
                while (jobbie.finishTime.plus(60, 
                        ChronoUnit.MILLIS).compareTo(Instant.now()) > 0) {
                }
                
            }
            
            port.dispatchShips();
        } while (!port.done);
        
        return "";
    }
    
    @Override
    protected synchronized void done() {
        
    }
    
    

}
