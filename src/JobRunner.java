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
public class JobRunner extends SwingWorker<String, Integer>{
    Job job;
    boolean paused = false;
    Instant startTime;
    Instant finishTime;
    int modifier = 1000;
    JTextArea textArea;
    
    int length;
    
    public JobRunner(Job njob, JTextArea area) {
        job = njob;
        startTime = Instant.now();
        length = (int)job.duration * modifier;
        finishTime = startTime.plus(length, ChronoUnit.MILLIS);
        textArea = area;
    }
    
    public void pause() {
        textArea.append(World.newLine + "WORKFLOW PAUSED. CLICK RESUME TO "
                + "UNPAUSE" + World.newLine);
        paused = true;
    }
    
    public void unPause(){
        textArea.append("Resuming workflow." + World.newLine + World.newLine);
        paused = false;
    }
    
    @Override
    public void process(List<Integer> chunks) {
        for (Integer chunk : chunks) {
//            System.out.println("" + elapsed);
//            setProgress((int)(100 *(elapsed / (job.duration * modifier))));
        }
    }
    
    
    @Override
    public synchronized String doInBackground() throws InterruptedException{
        if (!job.finished) {

            while (finishTime.isAfter(Instant.now()) & !isCancelled()) {
                if (paused) {
                    Thread.sleep(300);
                    finishTime = finishTime.plusMillis(300);
                }
                else {
                    long dif = Instant.now().until(finishTime, ChronoUnit.MILLIS);
                    double percent = (100 * ((length-dif)/(double)length));
                    setProgress((int)percent);
                }
            }
        }
                
        return "";
    }
    
    @Override
    protected synchronized void done() {
        if (isCancelled()) {
            textArea.append("Job cancelled." + World.newLine);
        }
        job.finished = true;
        notifyAll();
    }
    
    

}
