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
    JTextArea jobTextArea;
    JTextArea workerTextArea;
    JProgressBar progress;
    PortRunner runner;
    
    public WorldRunner(World world, JTextArea jArea, JTextArea wArea, 
            JProgressBar progressBar) {
        theWorld = world;
        jobTextArea = jArea;
        workerTextArea = wArea;
        progress = progressBar;
    }
    
    @Override
    public void process(List<String> chunks) {
        for (String chunk : chunks) {
            jobTextArea.append(chunk);
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
            runner = new PortRunner(port, jobTextArea, workerTextArea, progress);
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
