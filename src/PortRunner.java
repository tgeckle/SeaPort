import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ExecutionException;
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
    
    public PortRunner(SeaPort nport, JTextArea area) {
        port = nport;
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
        publish("Beginning jobs in Port of " + port.name + World.newLine
                + World.newLine);
        while (!port.done) {
            for (Dock dock : port.docks) {
                
                publish("Undertaking jobs at " + dock.name + World.newLine
                 + World.newLine);
                DockRunner runner = new DockRunner(dock, port, textArea);

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
