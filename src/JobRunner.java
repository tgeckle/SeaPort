import java.time.Instant;
import java.time.temporal.ChronoUnit;
import javax.swing.SwingWorker;

/**
 * Filename: DockRunner.java
 Author: Theresa Geckle
 Date: Dec 1, 2016
 Purpose: 
 */
public class JobRunner extends SwingWorker{
    Job job;
    SeaPort port;
    boolean last = false;
    Instant startTime;
    Instant finishTime;
    static int modifier = 30;
    
    public JobRunner(Job njob, SeaPort nport) {
        job = njob;
        port = nport;
        startTime = Instant.now();
        finishTime = startTime.plus(job.duration * modifier, ChronoUnit.MILLIS);
    }
    
    public void setLast(boolean isLast) {
        last = isLast;
    }
    
    @Override
    public synchronized String doInBackground() throws InterruptedException{
        if (!job.finished) {
            System.out.println("Beginng " + job.getName());

            while (finishTime.compareTo(Instant.now()) > 0 ) {
                
            }
        }
        
        return "";
    }
    
    @Override
    protected synchronized void done() {
        System.out.println("Job finished.");
        job.finished = true;

        if (!port.queue.isEmpty()) {
            port.dispatchShips();

        }
    }
    
    

}
