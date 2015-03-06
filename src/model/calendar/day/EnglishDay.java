package model.calendar.day;

/*
 * Application to print Jenyns Arms Diary Sheet
 * 
 * Copyright 2014 L Hughes
 * 
 * EnglishDay.java
 * extends Day.java
 * 
 * sets value
 * sets ordinal string
 * 
 * class method toString() writes day as String
 * class method dayNames() returns array with day names
 * 
 */

public class EnglishDay extends Day{

	public EnglishDay( int val ){
		this.value = val;
		this.ordinalString = englishOrdinalString();
	}
	
	private String englishOrdinalString() {
		int dateModulus = 0;
		String str;
		
		dateModulus = value % 10;
		
		switch ( dateModulus ) {
			case 1:
				
				if ( value == 11 ){
					str = "th";
				} else{
					str = "st";
				}
				break;
			case 2:
				if ( value == 12 ){
					str = "th";
				} else{
					str = "nd";
				}
				break;
			case 3:
				if ( value == 13 ){
					str = "th";
				} else{
					str = "rd";
				}
				break;
			default:
				str = "th";
		}
		
		return str;
	}

	@Override
	public String toString() {
		return Integer.toString( this.value ) + this.ordinalString;
	}
	
	public static String[] dayNames(){
		 return new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
	}
}