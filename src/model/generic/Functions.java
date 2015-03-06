package model.generic;

/*
 * Application to print Jenyns Arms Diary Sheet
 * 
 * Copyright 2014 L Hughes
 * 
 * Functions.java
 * 
 * isIteger()
 * 	- parses integer for year
 * 
 */

public class Functions {
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    // only got here if we didn't return false
	    return true;
	}
}
