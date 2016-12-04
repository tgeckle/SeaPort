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
 * handles all the jobs at all the ports within the given World. 
 */
public class WorldRunner extends SwingWorker<String, String>{
    World theWorld;
    JTextArea textArea;
    JProgressBar progress;
    PortRunner runner;
    
    public WorldRunner(World world, JTextArea area, JProgressBar progressBar) {
        theWorld = world;
        textArea = area;
        progress = progressBar;
    }
    
    @Override
    public void process(List<String> chunks) {
        for (String chunk : chunks) {
            textArea.append(chunk);
        }
    }
    
    public void pause() {
        runner.pause();
    }
    
    public void unPause() {
        runner.unPause();
    }
    
    public void cancel() {
        runner.cancel();
    }
    
    @Override
    public synchronized String doInBackground() throws InterruptedException{
        for (SeaPort port : theWorld.ports) {
            runner = new PortRunner(port, textArea, progress);
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
