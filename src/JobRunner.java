import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
public class JobRunner extends SwingWorker<String, String>{
    SeaPort port;
    Job job;
    boolean paused = false;
    Instant startTime;
    Instant finishTime;
    final int MODIFIER = 300;
    JTextArea jobTextArea;
    JTextArea workerTextArea;
    ArrayList<Person> workers;
    
    int length;
    
    public JobRunner(Job njob, SeaPort nport, JTextArea jArea, JTextArea wArea) {
        job = njob;
        port = nport;
        startTime = Instant.now();
        length = (int)job.duration * MODIFIER;
        finishTime = startTime.plus(length, ChronoUnit.MILLIS);
        jobTextArea = jArea;
        workerTextArea = wArea;
        workers = new ArrayList<>();
    }
    
    public void pause() {
        if (!paused) {
            jobTextArea.append(World.newLine + "WORKFLOW PAUSED. CLICK 'RESUME JOB' "
                    + "TO UNPAUSE" + World.newLine);
            paused = true;
        }
        else {
            jobTextArea.append("WORKFLOW ALREADY PAUSED. CLICK 'RESUME JOB' TO "
                    + "UNPAUSE." + World.newLine + World.newLine);
        }
    }
    
    public void unPause(){
        if (paused) {
            jobTextArea.append("RESUMING WORKFLOW." + World.newLine + World.newLine);
            paused = false;
        } else {
            jobTextArea.append("JOB MUST BE PAUSED TO RESUME." + World.newLine 
                    + World.newLine);
        }
    }
    
    @Override
    public void process(List<String> chunks) {
        for (String chunk : chunks) {
            jobTextArea.append(chunk);
        }
    }
    
    protected synchronized void assignWorkers() {
        boolean skillAssigned = false;
        for (String skill : job.requirements) {
            for (Person dude : port.persons) {
                if (!dude.isWorking && dude.skill.equals(skill)) {
                    workers.add(dude);
                    dude.isWorking = true;
                    skillAssigned = true;
                    port.freeWorkers.remove(dude);
                    break;
                }
            }
            if (!skillAssigned) {
                publish("Insufficient workers found. No worker found with "
                        + "'" + skill + "' skill." + World.newLine);
                cancel(true);
            }
            
            setWorkerText();
        }
    }
    
    private synchronized void setWorkerText() {
        workerTextArea.setText("Port of " + port.name + ": " + World.newLine);
        workerTextArea.append("Workers engaged: " + World.newLine);
        for (Person dude : workers) {
            workerTextArea.append("- " + dude.name + " (" + dude.skill + ")"
            + World.newLine);
        }
        workerTextArea.append(World.newLine + "Workers available: " 
                + World.newLine);
        for (Person dude : port.freeWorkers) {
            workerTextArea.append("- " + dude.name + " (" + dude.skill + ")"
            + World.newLine);
        }
    }
    
    
    @Override
    public synchronized String doInBackground() throws InterruptedException{
        if (!job.finished) {
            assignWorkers();

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
            jobTextArea.append("JOB CANCELLED." + World.newLine + World.newLine);
        } else {
            jobTextArea.append(job.name + " finished by ");
        }
        job.finished = true;
        setProgress(100);
        for (Person dude : workers) {
            jobTextArea.append(dude.name + " (" + dude.skill + "); ");
            port.freeWorkers.add(dude);
            dude.isWorking = false;
        }
        workers.removeAll(workers);
        jobTextArea.append(World.newLine);
        setWorkerText();
        notifyAll();
    }
    
    

}
