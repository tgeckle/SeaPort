// File: CreateSeaPortDataFile.java
// Date: Jul 8, 2016
// Author: Nicholas Duchon
// Purpose: CMSC 335 new project

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class CreateSeaPortDataFile {

    static int nt = 8; // number of ports
    static int nd = 15; // number of docks per port + 5 (>0)
    static int np = 20; // number of passenger ships per port
    static int nc = 20; // number of cargo ships per port
    static int nj = 5; // number of jobs per dock (>0)
    static int nn = 30; // number of persons per port + 5
    static String filename = "testinput.txt";

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
      if (args.length > 0) nt = Integer.parseInt (args [0]);
      if (args.length > 1) nd = Integer.parseInt (args [1]);
      if (args.length > 2) np = Integer.parseInt (args [2]);
      if (args.length > 3) nc = Integer.parseInt (args [3]);
      if (args.length > 4) nj = Integer.parseInt (args [4]);
      if (args.length > 5) nn = Integer.parseInt (args [5]);
      World mw;
      mw = new World ();
      // number of port, docks per port, num pass ships, num cargo ships, jobs per dock, persons per port
      mw.createRandomPorts (nt, nd, np, nc, nj, nn); 
   //       System.out.println ("\n\n>>>>> Random world:\n" + mw.toFileString());
      
      ArrayList <Thing> mwa = new ArrayList <> ();
      mw.toArray (mwa); 
      java.util.Collections.sort(mwa);
   
      try {
         int type = 0;
         java.io.PrintStream pw = System.out; // comment out next line to send output to console
         pw = new java.io.PrintStream (new File (filename));
         pw.println ("// File: " + filename);
         pw.println ("// Data file for SeaPort projects");
         java.util.Date date = new java.util.Date ();
         pw.printf ("// Date: %tc" + System.getProperty("line.separator") , date);
         pw.printf ("// parameters: %d %d %d %d %d %d" + 
                 System.getProperty("line.separator") , nt, nd, np, nc, nj, nn);
         pw.printf ("//   ports, docks, pships, cships, jobs, persons" + 
                 System.getProperty("line.separator") );
         // shuffle within category
         int start = 0;
         int end = 0;
         ArrayList <Thing> sub = new ArrayList <> ();
         ArrayList <Thing> mta = new ArrayList <> ();
         for (int i = 1; i < 7; i++) {
            sub.clear ();
            while (start < mwa.size() && mwa.get (start).index/10000 < i) start ++;
            end = start;
            if (end >= mwa.size ()) 
               continue;
            while (end < mwa.size() && mwa.get (end).index/10000 == i) {
               sub.add (mwa.get(end)); 
               end ++;
            }
            java.util.Collections.shuffle (sub);
            mta.addAll (sub);
            System.out.printf ("index: %d Sub: %s" + 
                    System.getProperty("line.separator") , i, "sub?");
         } // end for each category
         mwa = mta;
         for (Thing mt: mwa) {
            if (mt.index/10000 != type) {
               pw.print (System.getProperty("line.separator"));
               switch (mt.index/10000) {
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
               type = mt.index/10000;
            } // end if changing type
            pw.print (mt.toFileString());
         } // end writing data file
      } 
      catch (java.io.FileNotFoundException e) {
         System.out.println ("output file not found");
      }
      
      System.out.println ("done");
   } // end main
} // end class CreateSeaPortDataFile

