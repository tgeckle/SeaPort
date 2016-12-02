// File: CreateSeaPortDataFile.java
// Date: Jul 8, 2016
// Author: Nicholas Duchon
// Purpose: CMSC 335 new project

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class CreateSeaPortDataFile {

    static int numPorts = 8; // number of ports
    static int numDocks = 15; // number of docks per port + 5 (>0)
    static int numPassenger = 20; // number of passenger ships per port
    static int numCargo = 20; // number of cargo ships per port
    static int numJobs = 5; // number of jobs per dock (>0)
    static int numPeople = 30; // number of persons per port + 5
    static String filename = "smoltestinput.txt";

    static String portSpec
            = "// port   name index parent(null)" + System.getProperty("line.separator")
            + "//    port   <string> <int> <int>" + System.getProperty("line.separator");
    static String dockSpec
            = "// dock   name index parent(port)" + System.getProperty("line.separator")
            + "//    dock   <string> <int> <int>" + System.getProperty("line.separator");
    static String shipSpec
            = "// ship   name index parent(dock/port) weight length width draft"
            + System.getProperty("line.separator")
            + "//    ship   <string> <int> <int> <double> <double> <double> <double>"
            + System.getProperty("line.separator");
    static String cshipSpec
            = "// cship  name index parent(dock/port) weight length width draft "
            + "cargoWeight cargoVolume cargoValue"
            + System.getProperty("line.separator")
            + "//    cship  <string> <int> <int> <double> <double> <double> <double> "
            + "<double> <double> <double>" + System.getProperty("line.separator");
    static String pshipSpec
            = "// pship  name index parent(dock/port) weight length width draft "
            + "numPassengers numRooms numOccupied"
            + System.getProperty("line.separator")
            + "//    pship  <string> <int> <int> <double> <double> <double> <double> "
            + "<int> <int> <int>" + System.getProperty("line.separator");
    static String personSpec
            = "// person name index parent skill" + System.getProperty("line.separator")
            + "//    person <string> <int> <int> <string>"
            + System.getProperty("line.separator");
    static String jobSpec
            = "// job    name index parent duration [skill]+ (one or more, matches "
            + "skill in person, may repeat)" + System.getProperty("line.separator")
            + "//    job    <string> <int> <int> <double> [<string>]"
            + System.getProperty("line.separator");

   public static void main (String [] args) {
      if (args.length > 0) numPorts = Integer.parseInt (args [0]);
      if (args.length > 1) numDocks = Integer.parseInt (args [1]);
      if (args.length > 2) numPassenger = Integer.parseInt (args [2]);
      if (args.length > 3) numCargo = Integer.parseInt (args [3]);
      if (args.length > 4) numJobs = Integer.parseInt (args [4]);
      if (args.length > 5) numPeople = Integer.parseInt (args [5]);
      World myWorld;
      myWorld = new World ();
      // number of port, docks per port, num pass ships, num cargo ships, jobs per dock, persons per port
      myWorld.createRandomPorts (numPorts, numDocks, numPassenger, numCargo, numJobs, numPeople); 
   //       System.out.println ("\n\n>>>>> Random world:\n" + myWorld.toFileString());
      
      ArrayList <Thing> otherWorld = new ArrayList <> ();
      myWorld.toArray (otherWorld); 
      java.util.Collections.sort(otherWorld);
   
      try {
         int type = 0;
         java.io.PrintStream pw = System.out; // comment out next line to send output to console
         pw = new java.io.PrintStream (new File (filename));
         pw.println ("// File: " + filename);
         pw.println ("// Data file for SeaPort projects");
         java.util.Date date = new java.util.Date ();
         pw.printf ("// Date: %tc" + System.getProperty("line.separator") , date);
         pw.printf ("// parameters: %d %d %d %d %d %d" + 
                 System.getProperty("line.separator") , numPorts, numDocks, numPassenger, numCargo, numJobs, numPeople);
         pw.printf ("//   ports, docks, pships, cships, jobs, persons" + 
                 System.getProperty("line.separator") );
         // shuffle within category
         int start = 0;
         int end = 0;
         ArrayList <Thing> sub = new ArrayList <> ();
         ArrayList <Thing> mta = new ArrayList <> ();
         for (int i = 1; i < 7; i++) {
            sub.clear ();
            while (start < otherWorld.size() && otherWorld.get (start).index/10000 < i) start ++;
            end = start;
            if (end >= otherWorld.size ()) 
               continue;
            while (end < otherWorld.size() && otherWorld.get (end).index/10000 == i) {
               sub.add (otherWorld.get(end)); 
               end ++;
            }
            java.util.Collections.shuffle (sub);
            mta.addAll (sub);
            System.out.printf ("index: %d Sub: %s" + 
                    System.getProperty("line.separator") , i, "sub?");
         } // end for each category
         otherWorld = mta;
         for (Thing item: otherWorld) {
            if (item.index/10000 != type) {
               pw.print (System.getProperty("line.separator"));
               switch (item.index/10000) {
                  case 1: pw.print (portSpec)  ; 
                     break;
                  case 2: pw.print (dockSpec)  ; 
                     break;
                  case 3: pw.print (pshipSpec) ; 
                     break;
                  case 4: pw.print (cshipSpec) ; 
                     break;
                  case 5: pw.print (personSpec); 
                     break;
                  case 6: pw.print (jobSpec)   ; 
                     break;
               } // end switch
               type = item.index/10000;
            } // end if changing type
            pw.print (item.toFileString());
         } // end writing data file
      } 
      catch (java.io.FileNotFoundException e) {
         System.out.println ("output file not found");
      }
      
      System.out.println ("done");
   } // end main
} // end class CreateSeaPortDataFile

