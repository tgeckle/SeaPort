
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Filename: Ship.java
 * Author: Theresa Geckle
 * Date: Nov 1, 2016
 * Purpose: Extends the Thing class to provide additional fields of relevance to
 * * * a Ship. 
 */
public class Ship extends Thing {
   static ArrayList <String> words = null;

   double weight = 0;
   double length = 0;
   double width  = 0;
   double draft  = 0;
   Time arrivalTime = null;
   Time dockTime    = null;
   ArrayList <Job> jobs = new ArrayList <> ();
   boolean visited = false;
   
   /*
   empty no-parameter constructor
   */
   public Ship () {} // end no-parameter constructor
   
   /*
   Constructor using input from file
   @param input A Scanner object that contains all of the neccessary data to 
   create a new Ship instance.  
   */
   public Ship(Scanner input) {
       super(input);
       if (input.hasNextDouble()) {
           weight = input.nextDouble();
       }
       if (input.hasNextDouble()) {
           length = input.nextDouble();
       }
       if (input.hasNextDouble()) {
           width = input.nextDouble();
       }
       if (input.hasNextDouble()) {
           draft = input.nextDouble();
       }
   }
   
   // Adds a job to a Ship
   public void addJob(Job job) {
       jobs.add(job);
   }
   
   // Returns true if all jobs have been finished, to signal that the Ship should
   // leave the dock
   public boolean jobsFinished() {
       for (Job job : jobs) {
           if (!job.finished) {
               return false;
           }
       }
       return true;
   }
   
   /*
   Constructor for use with CreateSeaPortDataFile
   */
   public Ship (boolean t, int n) {
      if (words == null) readWordsFile ();
      name = words.get (rn.nextInt (words.size()));
      weight =  50 + rn.nextDouble () * 200;
      length = 100 + rn.nextDouble () * 400;
      width  =  30 + rn.nextDouble () * 100;
      draft  =  15 + rn.nextDouble () *  30;
   } // create a random ship
   
   /*
   Method for reading file of ship names for use when generating random
   input files/
   */
   void readWordsFile () {
      try {
         words = new ArrayList <> ();
         Scanner sp = new Scanner (new File ("shipNames.txt"));
         while (sp.hasNext()) words.add (sp.next());
         System.out.println ("Words File size: " + words.size());  
      } 
      catch (java.io.FileNotFoundException e) {System.out.println ("bad file");}
   } // end readWordsFile

   /*
   creates constituent ArrayLists when generating random input files 
   */
   void toArray (List <Thing> mta) {
      mta.add (this);
      for (Job mj: jobs) mj.toArray (mta);
   } // end method toList
   
   /*
   method for converting to String for purposes of creating an input file.
   */
   public String toFileString () {
      String st = "";
      st += String.format ("    ship %20s %d %d %.2f %.2f %.2f %.2f" 
              + System.getProperty("line.separator"), 
             name, index, parent, weight, length, width, draft);
      return st;
   } // end method toFileString
   
//   public int compareTo(Thing other) {
//       return super.compareTo(other);
//   } // Not sure if necessary.... 
   
   public int compareTo(Thing other) {
       Ship o;
       switch (Thing.sortCriterion) {
           case WEIGHT : {
               o = (Ship) other;
               return compareTo(o);
           }
           case LENGTH : {
               o = (Ship) other;
               return compareTo(o);
           }
           case WIDTH : {
               o = (Ship) other;
               return compareTo(o);
           }
           case DRAFT : {
               o = (Ship) other;
               return compareTo(o);
           }
           default : {
               return super.compareTo(other);
           }
       }
       
   }
   
   public int compareTo(Ship other) {
       switch (Thing.sortCriterion) {
           case WEIGHT : {
               return Double.compare(weight, other.weight);
           }
           case LENGTH : {
               return Double.compare(length, other.length);
           }
           case WIDTH : {
               return Double.compare(width, other.width);
           }
           case DRAFT : {
               return Double.compare(draft, other.draft);
           }
           default : {
               return super.compareTo(other);
           }
       }
   }
   
   /*
   Ship toString method
   */
   public String toString() {
       return super.toString() + "; Weight: " + weight + " Length: " + length +
               " Width: " + width + " Draft: " + draft;
   } // end Ship toString

} // end class Ship

