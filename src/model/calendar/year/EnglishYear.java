package model.calendar.year;

/*
 * Application to print Jenyns Arms Diary Sheet
 * 
 * Copyright 2014 L Hughes
 * 
 * EnglishYear.java
 * extends Year.java
 * 
 * setup English year with respect to leap year in English calendar
 * 
 */

import model.calendar.month.*;

public class EnglishYear extends Year {
	
	public final boolean leapYear;
	
	public EnglishYear( int yearValue ){
		
		int numMonthsInYear = 12;
		
		value = yearValue;

		leapYear = isLeapYear();
		
		month = new EnglishMonth[ numMonthsInYear ];
		
		for ( int i = 0; i < numMonthsInYear; i++ ){
			month[ i ] = new EnglishMonth( i, leapYear );
		}
		
	}
	
	private boolean isLeapYear(){
		
		if ( value % 4 == 0){
			return true;
		}
		
		return false;
		
	}
	
}
