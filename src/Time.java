/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Filename: Time.java
 Author: Tea
 Date: Nov 1, 2016
 Purpose: 
 */
public class Time {
   int time = 0; // measured in seconds
   
   public Time (int t) {time = t;}
   
   public String toString () {
      return String.format ("%d %d:%d:%d", time/60/60/24, (time/60/60)%24, (time/60)%60, time%60);
   } // end method toString
} // end class Time
