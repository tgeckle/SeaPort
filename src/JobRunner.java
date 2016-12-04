import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

/**
 * Filename: DockRunner.java
 Author: Theresa Geckle
 Date: Dec 1, 2016
 Purpose: Extends the abstract SwingWorker class to implement a class that 
 * handles a single job. Also contains the core logic for what happens if the
 * workflow is paused or unpaused. 
 */
public class JobRunner extends SwingWorker<String, Integer>{
    Job job;
    boolean paused = false;
    Instant startTime;
    Instant finishTime;
    final int MODIFIER = 1000;
    JTextArea textArea;
    
    int length;
    
    public JobRunner(Job njob, JTextArea area) {
        job = njob;
        startTime = Instant.now();
        length = (int)job.duration * MODIFIER;
        finishTime = startTime.plus(length, ChronoUnit.MILLIS);
        textArea = area;
    }
    
    public void pause() {
        if (!paused) {
            textArea.append(World.newLine + "WORKFLOW PAUSED. CLICK 'RESUME JOB' "
                    + "TO UNPAUSE" + World.newLine);
            paused = true;
        }
        else {
            textArea.append("WORKFLOW ALREADY PAUSED. CLICK 'RESUME JOB' TO "
                    + "UNPAUSE." + World.newLine + World.newLine);
        }
    }
    
    public void unPause(){
        if (paused) {
        textArea.append("RESUMING WORKFLOW." + World.newLine + World.newLine);
        paused = false;
        } else {
            textArea.append("JOB MUST BE PAUSED TO RESUME." + World.newLine 
                    + World.newLine);
        }
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
            textArea.append("JOB CANCELLED." + World.newLine + World.newLine);
        }
        job.finished = true;
        setProgress(100);
        notifyAll();
    }
    
    

}
