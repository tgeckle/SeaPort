import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

/**
 * Filename: DockRunner.java
 Author: Theresa Geckle
 Date: Dec 1, 2016
 Purpose: 
 */
public class JobRunner extends SwingWorker<String, String>{
    Job job;
    boolean last = false;
    Instant startTime;
    Instant finishTime;
    int modifier = 10;
    JTextArea textArea;
    
    public JobRunner(Job njob, JTextArea area) {
        job = njob;
        startTime = Instant.now();
        finishTime = startTime.plus(job.duration * modifier, ChronoUnit.MILLIS);
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
        if (!job.finished) {

            while (finishTime.compareTo(Instant.now()) > 0 ) {
                
            }
        }
                
        return "";
    }
    
    @Override
    protected synchronized void done() {
        job.finished = true;
        notifyAll();
    }
    
    

}
