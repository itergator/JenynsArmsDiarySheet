package model.calendar.month;

/*
 * Application to print Jenyns Arms Diary Sheet
 * 
 * Copyright 2014 L Hughes
 * 
 * EnglishMonth.java
 * extends Month.java
 * 
 * sets month index
 * creates Day array with number of days in month
 * 
 * class method monthNames to return array of month names
 * 
 */

import model.calendar.day.*;

public class EnglishMonth extends Month{
	
	public EnglishMonth( int i, boolean leapYear ){
		this.index = i;

		int[] daysInMonth = this.numDaysInMonth( leapYear );
		this.day = new EnglishDay[ daysInMonth[ this.index ] ];
		
		for( int ii = 0; ii < daysInMonth[ this.index ]; ii++ ){
			this.day[ ii ] = new EnglishDay( ii + 1 );
		}
	}
	
	private int[] numDaysInMonth( boolean leapYear ) {
		
		int numDaysWhenLeapYear = 28;
		
		if (leapYear) numDaysWhenLeapYear = 29;
		
		return new int[]{31, numDaysWhenLeapYear, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	}

	public static String[] monthNames() {
		return new String[]{ "January","February","March","April","May","June","July","August","September","October","November","December"};
	}
}