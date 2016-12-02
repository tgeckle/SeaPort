
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.util.LinkedHashMap;
import java.util.HashMap;

/**
 * Filename: CargoShip.java
 * Author: Theresa Geckle
 * Date: Nov 1, 2016
 * Purpose: Extends the Thing class to provide additional fields of relevance to
 * * * a Seaport. Holds ArrayLists of all Docks, Ships, and Persons within the
 * * * port.  
 */
class SeaPort extends Thing {
   static int indexNew = 10000;
   static ArrayList <String> portNames = null;
   
   double latitude  = 0;
   double longitude = 0;
   ArrayList <Dock>   docks   = new ArrayList <> (); 
   ArrayList <Ship>   queue     = new ArrayList <> ();
   ArrayList <Ship>   ships   = new ArrayList <> ();
   ArrayList <Person> persons = new ArrayList <> ();
   
   boolean done = false;
   
   /*
   Constructor using input from file
   @param input A Scanner object that contains all of the neccessary data to 
   create a new SeaPort instance.  
   */
   public SeaPort(Scanner input) {
       super(input);
   }
   
   /*
   Constructor for use with CreateSeaPortDataFile
   */
   public SeaPort (int numDocks, int numPass, int numCargo, int numJobs, 
           int numPersons) {
      if (portNames == null) readPortsFile ();
      name = portNames.get (rn.nextInt (portNames.size()));
      index = indexNew++;
      parent = 0;
      int remainingDocks = rn.nextInt (numDocks) + 5;
      while (remainingDocks > 0 && numPass > 0) {
         PassengerShip mps = new PassengerShip (true, numJobs); // random ship
         ships.add (mps);
         Dock md = new Dock (true); // random dock
         docks.add (md);
         md.ship = mps;
         md.parent = index;
         mps.parent = md.index;
         numPass --;
         remainingDocks--;
      } // end adding passenger ships first
      while (numPass > 0) {
         PassengerShip mps = new PassengerShip (true, numJobs); // random ship
         ships.add (mps);
         queue.add (mps);
         mps.parent = index;
         numPass --;
      } // end remaining passenger ships
      while (remainingDocks > 0 && numCargo > 0) {
         CargoShip mpc = new CargoShip (true, numJobs); // random ship
         ships.add (mpc);
         Dock md = new Dock (true); // random dock
         docks.add (md);
         md.ship = mpc;
         md.parent = index;
         mpc.parent = md.index;
         numCargo --;
         remainingDocks--;
      } // end adding passenger ships first
      while (numCargo > 0) {
         CargoShip mpc = new CargoShip (true, numJobs); // random ship
         ships.add (mpc);
         queue.add (mpc);
         mpc.parent = index;
         numCargo --;
      } // end remaining passenger ships
      
      for (int i = 0; i < rn.nextInt (numPersons) + 5; i++) {
         persons.add (new Person (this));
      }
   } // end list of port names constructor - creates a random port
   
   /*
   Dispatches current assigned Ship from a given Dock and takes on the next Ship
   in the queue. Before being called, it should be checked that there are still 
   Ships in the queue.
     */
   
   public synchronized void dispatchShips() {
       for (Dock dock : docks) {
           if (dock.ship.jobsFinished()) {
               if (!queue.isEmpty()) {
                   dock.ship = queue.remove(0);
               } else {
                   done = true;
               }
           }
       }
   }
   
   /*
   Adds a Dock to the ArrayList of Docks within the seaport.
   */
   public void addDock(Dock dock) {
       docks.add(dock);
   } // end addDock()
   
   /*
   Adds a ship to the list of all ships within the SeaPort and also adds it to 
   its parent Dock or to the queue of Ships waiting to dock.
   */
   public void addShip(Ship ship) {
       ships.add(ship);
       if (ship.getParent() == index) {
           queue.add(ship);
       }
       
       else {
           getDockByIndex(ship.getParent()).addShip(ship);
       }
   } // end addShip
   
   /*
   Adds a Person to the list of Persons in the SeaPort.
   */
   public void addPerson(Person dude) {
       persons.add(dude);
   } // end addPerson()
   
   /*
   Returns the Dock object with a given @param index.
   */
   public Dock getDockByIndex(int index) {
       for (Dock dock : docks) {
           if (dock.getIndex() == index) {
               return dock;
           }
       }
       return null;
   } // end getDockByIndex method
   
   /*
   Returns the ArrayList of Docks at the SeaPort
   */
   public ArrayList<Dock> getDocks() {
       return docks;
   } // end getDocks()
   
   /*
   Returns the ArrayList of People at the SeaPort
   */
   public ArrayList<Person> getPeople() {
       return persons;
   } // end getPeople()
   
   /*
   Returns the ArrayList of Ships at the SeaPort
   */
   public ArrayList<Ship> getShips() {
       return ships;
   } // end getShips()
   
   /*
   reads list of port names for use in creating random input files
   */
   void readPortsFile () {
      try {
         portNames = new ArrayList <> ();
         Scanner sp = new Scanner (new File ("portNames.txt"));
         while (sp.hasNext()) portNames.add (sp.next());
         System.out.println ("Ports file size: " + portNames.size());  
      } 
      catch (java.io.FileNotFoundException e) {System.out.println ("bad file");}
   } // end method readPortsFile
   
   /*
   creates constituent ArrayLists when generating random input files 
   */
   void toArray (List <Thing> ma) {
      ma.add (this);
      for (Dock   md: docks  ) md.toArray (ma);
      for (Ship   ms: ships  ) ms.toArray (ma);
      for (Person mp: persons) mp.toArray (ma);
   } // end toArray in SeaPort

   /*
   method for converting to String for purposes of creating an input file.
   */
   public String toFileString () {
      return String.format ("port %s %d %d" 
              + System.getProperty("line.separator"), name, index, 0);
   } // end method toFileString
   
   /*
   SeaPort toString
   */
   public String toString() {
       String result = super.toString() + System.getProperty("line.separator")
                + System.getProperty("line.separator");
       
       for (Dock dock : docks) {
           result += "    Dock: " + dock.toString() + 
                   System.getProperty("line.separator") +
                   System.getProperty("line.separator");
       } // end Dock for loop
       
       result += "--- List of all ships in queue: " + 
               System.getProperty("line.separator");
       
       for (Ship ship : queue) {
           result += " > " + ship.toString() + 
                   System.getProperty("line.separator");
       } // end Ship queue for loop
       
       result += System.getProperty("line.separator") + 
               "--- List of all ships: " + System.getProperty("line.separator");
       
       for (Ship ship : ships) {
           result += " > " + ship.toString() + 
                   System.getProperty("line.separator");
       } // end Ship for loop
       
       result += System.getProperty("line.separator") + 
               "--- List of all people: "+ System.getProperty("line.separator");
       
       for (Person dude : persons) {
           result += " > Person: " + dude.toString() + 
                   System.getProperty("line.separator");
       } // end Person for loop
       
       return result;
   } // end toString

} // end class SeaPort

