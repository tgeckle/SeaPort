
import java.util.ArrayList;
import java.util.Scanner;
 

/**
 * Filename: Job.java
 Author: Tea
 Date: Nov 1, 2016
 Purpose: 
 */
public class Job extends Thing {
   static int indexNew = 60000;
   static ArrayList <String> words;
   public boolean finished = false;
  
   double duration = 0.0;
   ArrayList <String> requirements = new ArrayList <> ();
  // eg {"painter", "painter", "painter", "carpenter"};
   
   public Job(Scanner input) {
       super(input);
       if (input.hasNextDouble()) {
           duration = input.nextDouble();
       }
       while (input.hasNext()) {
           requirements.add(input.next());
       }
   }

   public Job (Ship ms) {
      name = String.format ("Job_%d_%d_%d", 
                rn.nextInt(90)+10, rn.nextInt(90)+10, rn.nextInt(90)+10);
      index = indexNew++;
      parent = ms.index;
      duration = rn.nextLong () * 100 + 20;
      if (skillNames == null) readSkillsFile ();
      for (int i = 0; i < rn.nextInt (5); i++)
         requirements.add (skillNames.get (rn.nextInt (skillNames.size())));
   } // create a random job
   
   public String toFileString () {
      String st = "";
      st += String.format ("    job %20s %d %d %.2f", 
             name, index, parent, duration);
      for (String sr: requirements) st += " " + sr;
      return st + System.getProperty("line.separator");
   } // end method toFileString
   
} // end Thing

