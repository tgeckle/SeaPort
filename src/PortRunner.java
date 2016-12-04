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
 Date: Dec 2, 2016
 Purpose: Extends the abstract SwingWorker class to implement a class that 
 * handles all the jobs within the supplied port.
 */
public class PortRunner extends SwingWorker<String, String>{
    SeaPort port;
    JTextArea textArea;
    JProgressBar progress;
    DockRunner runner;
    
    public PortRunner(SeaPort nport, JTextArea area, JProgressBar progressBar) {
        port = nport;
        textArea = area;
        progress = progressBar;
    }
    
    public void cancel() {
        if (!port.isDone()) {
            runner.cancel();
        } else {
            textArea.append(World.newLine + "ALL JOBS FINISHED, NOTHING TO CANCEL.");
        }
    }
    
    public void pause() {
        if (!port.isDone()) {
            runner.pause();
        } else {
            textArea.append(World.newLine + "ALL JOBS FINISHED, NOTHING TO SUSPEND.");
        }
    }
    
    public void unPause() {
        if (!port.isDone()) {
            runner.unPause();
        } else {
            textArea.append(World.newLine + "ALL JOBS FINISHED, NOTHING TO RESUME.");
        }
    }
    
    @Override
    public void process(List<String> chunks) {
        for (String chunk : chunks) {
            textArea.append(chunk);
        }
    }
    
    @Override
    public synchronized String doInBackground() throws InterruptedException{
        publish("Beginning jobs in Port of " + port.name + World.newLine
                + World.newLine);
        while (!port.isDone()) {
            for (Dock dock : port.docks) {
                
                if(!dock.ship.visited) {

                    publish("Undertaking jobs at " + dock.name + World.newLine
                            + World.newLine);
                    if (dock.ship.jobs.isEmpty()) {
                        publish("No jobs found on ship " + dock.ship.name + "."
                                + World.newLine);
                        dock.ship.visited = true;
                    } else {
                        runner = new DockRunner(dock, port, textArea, progress);

                        synchronized (runner) {
                            runner.execute();
                            runner.wait();
                        }
                    }

                    port.dispatchShips();
            }

            }
        }
        publish("All jobs finished at Port of " + port.name + World.newLine);
        
        return "";
    }
    
    @Override
    protected synchronized void done() {
        notifyAll();
    }
    
}
