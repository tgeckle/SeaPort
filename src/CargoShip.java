import java.util.Scanner;

/**
 * Filename: CargoShip.java
 * Author: Theresa Geckle
 * Date: Nov 1, 2016
 * Purpose: Extends the Ship class to provide additional fields of relevance to
 * * * a cargo ship. 
 */
public class CargoShip extends Ship {
   static int indexNew = 40000;
   
   double cargoWeight = 0;
   double cargoVolume = 0;
   double cargoValue  = 0;
   
   /*
   Constructor using input from file
   @param input A Scanner object that contains all of the neccessary data to 
   create a new CargoShip instance.  
   */
   public CargoShip(Scanner input) {
       super(input);
       if (input.hasNextDouble()) {
           cargoWeight = input.nextDouble();
       }
       if (input.hasNextDouble()) {
           cargoVolume = input.nextDouble();
       }
       if (input.hasNextDouble()) {
           cargoValue = input.nextDouble();
       }
   } // end Scanner constructor
   
   /*
   Constructor for use with CreateSeaPortDataFile
   */
   public CargoShip (boolean f, int n) {
      super (f, n);
      index = indexNew++;
      cargoWeight =  20 + rn.nextDouble() *  200;
      cargoVolume = 100 + rn.nextDouble() *  100;
      cargoValue  =  10 + rn.nextDouble() * 1000;
      for (int i = 0; i < rn.nextInt (n); i++)
         jobs.add (new Job (this));
   } // end random ship constructor
   
   /*
   method for converting to String for purposes of creating an input file.
   */
   public String toFileString () {
      String st = "";
      st += String.format ("    cship %20s %d %d %.2f %.2f %.2f %.2f %.2f "
              + "%.2f %.2f" + System.getProperty("line.separator"), 
             name, index, parent, weight, length, width, draft,
             cargoWeight, cargoVolume, cargoValue);
      return st;
   } // end method toFileString
   
   /*
   toString for CargoShip class
   */
   public String toString() {
       String result = "Cargo Ship: " + super.toString();
       
       return result;
   } // end CargoShip toString
   
} // end class CargoShip

