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
public class WorldRunner extends SwingWorker<String, String>{
    World theWorld;
    JTextArea textArea;
    
    public WorldRunner(World world, JTextArea area) {
        theWorld = world;
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
        for (SeaPort port : theWorld.ports) {
            PortRunner runner = new PortRunner(port, textArea);
            synchronized (runner) {
                runner.execute();
                runner.wait();
            }

        }
        
        return "";
    }
    
    @Override
    protected synchronized void done() {
        notifyAll();
    }
    
}
