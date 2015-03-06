package model.calendar.day;

/*
 *  Application to print Jenyns Arms Diary Sheet
 * 
 * Copyright 2014 L Hughes
 * 
 * Abstract class: Day.java
 * day has value, ordinal string eg 2nd
 */

public abstract class Day {
	
	protected int value;
	protected String ordinalString;
	
	@Override
	public abstract String toString();
	
}