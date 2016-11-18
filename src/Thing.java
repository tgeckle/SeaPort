
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Filename: Thing.java
 * Author: Theresa Geckle
 * Date: Nov 1, 2016
 * Purpose: Provides a parent class to the various classes in the project
 * providing basic functionality with regards to the name, index, and parent
 * fields which are required for all child classes.
 */
class Thing implements Comparable <Thing> {
   static java.util.Random rn = new java.util.Random ();
   static ArrayList <String> skillNames = null; // to read file only once
   
   String name = null;
   int index = 0;
   int parent = 0; // ID number of the parent Thing
   
   /*
   empty no-parameter constructor
   */
   public Thing() {
       
   } // end no-parameter constructor
   
   /*
   Constructor using input from file
   @param input A Scanner object that contains all of the basic data to 
   create a new Thing instance. Called from child class constructors to populate
   central fields name, index, and parent.
   */
   public Thing (Scanner input) {
       if (input.hasNext()) {
           name = input.next();
       }
       if (input.hasNextInt()) {
           index = input.nextInt();
       }
       if (input. hasNextInt()) {
           parent = input.nextInt();
       }
   } // end Thing Scanner constructor
   
   /*
   Method for reading file of skill names for use when generating random
   input files/
   */
   void readSkillsFile () {
      try {
         skillNames = new ArrayList <> ();
         Scanner sp = new Scanner (new File ("skillNames.txt"));
         while (sp.hasNext()) skillNames.add (sp.next());
         System.out.println ("Skills File size: " + skillNames.size());  
      } 
      catch (java.io.FileNotFoundException e) {System.out.println ("bad file");}
   } // end readWordsFile
   
   /*
   Returns the parent ID
   */
   public int getParent() {
       return parent;
   } // end getParent()
   
   /*
   Returns the index.
   */
   public int getIndex() {
       return index;
   } // end getIndex()
   
   /*
   Returns the name
   */
   public String getName() {
       return name;
   } // end getName()

   /*
   Creates an ArrayList of a Things
   */
   void toArray (List <Thing> mta) {
      mta.add (this);
   } // end method toList
   
   /*
   compareTo() method for implementing Comparable interface
   */
   public int compareTo (Thing m) {
      return index - m.index;
   } // end method compareTo > Comparable
   
   /*
   method for converting to String for purposes of creating an input file.
   */
   public String toFileString () {
      return "";
   } // default toFileString method in Thing
   
   /*
   Thing toString
   */
   public String toString() {
       return name + " " + index;
   }
} // end class Thing

