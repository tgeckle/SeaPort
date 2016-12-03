import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JProgressBar;
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
    JProgressBar progress;
    JobRunner runner;
    
    public DockRunner(Dock ndock, SeaPort nPort, JTextArea area, 
            JProgressBar progressBar) {
        dock = ndock;
        port = nPort;
        textArea = area;
        progress = progressBar;
    }
    
    public void cancel() {
        runner.cancel(true);
    }
    
    public void pause() {
        runner.pause();
    }
    
    public void unPause() {
        runner.unPause();
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
                runner = new JobRunner(job, textArea);
                progress.setValue(0);
                progress.setStringPainted(true);
                runner.addPropertyChangeListener(new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent evt) {
                        if ("progress".equals(evt.getPropertyName()) &!job.finished) {
                            progress.setValue((Integer) evt.getNewValue());
                        }
                        else if ("progress".equals(evt.getPropertyName()) &job.finished) {
                            progress.setValue(100);
                        }
                    }
                });
                synchronized (runner) {
                    runner.execute();
                    runner.wait();
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
                    " finished all jobs." + World.newLine + World.newLine);
        }
        
        return "";
    }
    
    @Override
    protected synchronized void done() {
        notifyAll();
    }
    
    

}
