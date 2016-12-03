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
 Purpose: 
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
        runner.cancel();
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
        publish("Beginning jobs in Port of " + port.name + World.newLine
                + World.newLine);
        while (!port.done) {
            for (Dock dock : port.docks) {
                
                publish("Undertaking jobs at " + dock.name + World.newLine
                 + World.newLine);
                runner = new DockRunner(dock, port, textArea, progress);

                synchronized (runner) {
                    runner.execute();
                    runner.wait();
                }

                port.dispatchShips();

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
