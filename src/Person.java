
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Filename: Person.java
 * Author: Theresa Geckle
 * Date: Nov 1, 2016
 * Purpose: Extends the Thing class to provide additional fields of relevance to
 * * * a person. 
 */
public class Person extends Thing {
   static int indexNew = 50000;
   static ArrayList <String> words; 
   
   boolean isWorking = false;
  
   String skill = ""; // skill associated with a Person
   
   /*
   Constructor using input from file
   @param input A Scanner object that contains all of the neccessary data to 
   create a new Person instance.  
   */
   public Person(Scanner input) {
       super(input);
       if (input.hasNext()){
           skill = input.next();
       }
   } // end Scanner constructor
  
   /*
   Constructor for use with CreateSeaPortDataFile
   */
   public Person (SeaPort msp) {
      if (words == null) readNamesFile ();
      name = words.get (rn.nextInt (words.size()));
      index = indexNew++;
      parent = msp.index;
      if (skillNames == null) readSkillsFile ();
      skill = skillNames.get (rn.nextInt (skillNames.size()));
   } // create a random person
   
   /*
   Method for use in generating random Persons for purposes of creating an
   input file. 
   */
   void readNamesFile () {
      try {
         words = new ArrayList <> ();
         Scanner sp = new Scanner (new File ("personNames.txt"));
         while (sp.hasNext()) words.add (sp.next());
         System.out.println ("Names File size: " + words.size());  
      } 
      catch (java.io.FileNotFoundException e) {System.out.println ("bad file");}
   } // end readWordsFile
   
   public int compareTo (Thing other) {
       Person o;
       switch (Thing.sortCriterion) {
           case SKILL : {
               o = (Person) other;
               return compareTo(o);
           }
           default : {
               return super.compareTo(other);
           }
       }
       
   }
   
   public int compareTo(Person other) {
       switch (Thing.sortCriterion) {
           case SKILL : {
               return skill.compareTo(other.getSkill());
           }
           default : {
               return super.compareTo(other);
           }
       }
   }
   
   /*
   Returns a Person's skill
   */
   public String getSkill() {
       return skill;
   } // end getSkill()

   public String toString () {
      return "Person: " + super.toString() + " " + skill;
   } // end toString

  /*
   method for converting to String for purposes of creating an input file.
   */
   public String toFileString () {
      String st = "";
      st += String.format ("    person %20s %d %d %s" + 
              System.getProperty("line.separator"), 
             name, index, parent, skill);
      return st;
   } // end method toFileString
   
} // end Person

