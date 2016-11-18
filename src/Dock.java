import java.util.Scanner;

/**
 * Filename: Dock.java
 * Author: Theresa Geckle
 * Date: Nov 1, 2016
 * Purpose: Extends the Thing class to provide additional fields of relevance to
 * * * a dock. 
 */
public class Dock extends Thing {
   static int indexNew = 20000;
   Ship ship = null; // field for the Ship moored at the Dock
   
   /*
   Constructor using input from file
   @param input A Scanner object that contains all of the neccessary data to 
   create a new Dock instance.  
   */
   public Dock(Scanner input) {
       super(input);
   } // end Scanner constructor
   
   public Dock(String name, int parent) {
       super(name, parent);
   }

   /*
   Constructor for use with CreateSeaPortDataFile
   */
   public Dock (boolean f) {
      index = indexNew++;
      name = "Pier_"  + index%2000;
   } // end random ship constructor
   
   /*
   Adds a Ship to the Dock
   */
   public void addShip(Ship newShip) {
       ship = newShip;
   } // end addShip()

  /*
   method for converting to String for purposes of creating an input file.
   */
    public String toFileString () {
      String st = "";
      st += String.format ("  dock %s %d %d %d" + 
              System.getProperty("line.separator"), name, index, parent, 
              ship.index);
      return st;
   } // end method toFileString
   
   /*
    Dock toString
    */ 
   public String toString() {
       String result = super.toString() + System.getProperty("line.separator")
               +"      Ship: " + ship.toString();
       
       return result;       
   } //end Dock toString method

} // end class Dock

