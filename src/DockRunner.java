import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

/**
 * Filename: DockRunner.java
 Author: Theresa Geckle
 Date: Dec 1, 2016
 Purpose: 
 */
public class DockRunner extends SwingWorker<String, String>{
    Dock dock;
    SeaPort port;
    JTextArea textArea;
    
    public DockRunner(Dock ndock, SeaPort nPort, JTextArea area) {
        dock = ndock;
        port = nPort;
        textArea = area;
    }
    
    @Override
    public void process(List<String> chunks) {
        for (String chunk : chunks) {
            textArea.append(chunk);
        }
    }
    
    @Override
    public synchronized String doInBackground() throws InterruptedException{
        publish(dock.ship.name + " docked at " + dock.name + "."
                            + World.newLine);
        if (dock.ship.jobs.isEmpty()) {
            publish("No jobs found on ship " + dock.ship.name + "."
                    + World.newLine);
        } else {
            for (Job job : dock.ship.jobs) {
                publish("Beginning " + job.name + " on the ship "
                        + dock.ship.name + World.newLine);
                JobRunner jobbie = new JobRunner(job, textArea);
                synchronized (jobbie) {
                    jobbie.execute();
                    jobbie.wait();
                }
                publish(job.name + " finished." + World.newLine);

            }
            publish("All jobs finished on " + dock.ship.name + World.newLine
                    );
        }
        publish(dock.ship.name + " disembarking." + World.newLine
                    + World.newLine);
        if (port.queue.isEmpty()) {
            publish("No ships left in queue." + World.newLine + dock.name +
                    " finished all jobs." + World.newLine);
        }
        
        return "";
    }
    
    @Override
    protected synchronized void done() {
        notifyAll();
    }
    
    

}
