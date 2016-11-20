import java.util.Scanner;

/**
 * Filename: PassengerShip.java
 * Author: Theresa Geckle
 * Date: Nov 1, 2016
 * Purpose: Extends the Ship class to provide additional fields of relevance to
 * * * a passenger ship. 
 */
public class PassengerShip extends Ship {
   static int indexNew = 30000;
   
   int numberOfPassengers    = 0;
   int numberOfRooms         = 0;
   int numberOfOccupiedRooms = 0;
   
   /*
   Constructor using input from file
   @param input A Scanner object that contains all of the neccessary data to 
   create a new PassengerShip instance.  
   */
   public PassengerShip(Scanner input) {
       super(input);
       if(input.hasNextInt()){
           numberOfPassengers = input.nextInt();
       }
       if(input.hasNextInt()){
           numberOfRooms = input.nextInt();
       }
       if(input.hasNextInt()){
           numberOfOccupiedRooms = input.nextInt();
       }
   }

   /*
   Constructor for use with CreateSeaPortDataFile
   */
   public PassengerShip (boolean f, int n) {
      super (f, n);
      index = indexNew++;
      numberOfRooms         = 100 + rn.nextInt (1000);
      numberOfPassengers    = Math.round(numberOfRooms * rn.nextFloat() * 4);
      numberOfOccupiedRooms = Math.min (numberOfRooms, numberOfPassengers/2);
      for (int i = 0; i < rn.nextInt (n); i++)
         jobs.add (new Job (this));
   } // end random ship constructor

   /*
   method for converting to String for purposes of creating an input file.
   */
   public String toFileString () {
      return String.format ("    pship %20s %d %d %.2f %.2f %.2f %.2f %d %d %d" 
              + System.getProperty("line.separator"), 
             name, index, parent, weight, length, width, draft,
             numberOfPassengers, numberOfRooms, numberOfOccupiedRooms);
   } // end method toFileString
   
   /*
   PassengerShip toString
   */
   public String toString() {
       String result = "Passenger Ship: " + super.toString();
       
       return result;
   } // end PassengerShip toString
   
} // end class PassengerShip

