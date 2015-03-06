package controller;

/*
 * 
 * JenynsBookingSheet.java
 * controller
 * 
 * Application to print Jenyns Arms Diary Sheet
 * 
 * Copyright 2014 L Hughes
 * 
 */


/*
 * import java files
 */
import java.awt.List;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.event.*;

import model.calendar.day.EnglishDay;
import model.calendar.month.EnglishMonth;
import model.calendar.year.*;
import model.generic.Functions;
import model.printing.PrintDiarySheet;
import ui.*;

/*
 * public class JenynsBookingSheet
 */

public class JenynsBookingSheet {
	
	/*
	 * user interface variables
	 */

	private static JFrame window = new JFrame("Jenyns Booking Sheet");
	private static UserInterface ui = new UserInterface();
	
	/*
	 * indexes from user interface for use in model
	 */
	private static Year year;
	private static int startMonthIndex;
	private static int startDayIndex;
	private static int startDateIndex;
	private static int endMonthIndex;
	private static int endDateIndex;
	
	
	/*
	 * main
	 * entry point for application
	 * 
	 * disable window resizing
	 * add action listeners for UI Components
	 * start year panel
	 * 
	 * return null
	 * 
	 */
	public static void main(String[] args) {
		
		window.setResizable(false);
		
		addActionListeners();
		
		yearPanel();
		
	}
	
	/*
	 * add action listeners for
	 * 		- year OK button
	 * 		- reset button
	 * 		- print button
	 * 		- start month list
	 * 		- start day list
	 * 		- start date list
	 * 		- end month list
	 * 		- end date list
	 */
	
	private static void addActionListeners(){
		
		/*
		 * yearOKButton
		 * 
		 * validate user input
		 * reset UI
		 * create year
		 * set new size for window
		 * start calendar panel
		 * otherwise:
		 * retry user input
		 */
		
		ui.yearOKButton.addActionListener(new ActionListener() {
		    @Override
			public void actionPerformed(ActionEvent e) {
		        if ( Functions.isInteger( ui.yearTextField.getText() ) ){
		        	JPanel contentPane = (JPanel) window.getContentPane();
		        	contentPane.removeAll();
		        	contentPane.revalidate();
		        	contentPane.repaint();
		        	year = new EnglishYear( Integer.parseInt( ui.yearTextField.getText( ) ) );
		    		window.setSize( 600, 600);
		        	calendarPanel();
		        } else {
		        	ui.yearErrorLabel.setText("error in year, retry");
		        }
		    }
		} );
		
		/*
		 * resetButton
		 * 
		 * reset application
		 */
		
		ui.resetButton.addActionListener(new ActionListener() {
		    @Override
			public void actionPerformed(ActionEvent e) {
		    	
		    	reset();
	        	
		    }
		}
		);
		
		/*
		 * printButton
		 * 
		 * validate user input dates, if wrong:
		 * show error
		 * otherwise:
		 * create list of sheets to print
		 * print sheet one by one
		 */
		
		ui.printButton.addActionListener(new ActionListener() {
		    @Override
			public void actionPerformed(ActionEvent e) {
		    	
		    	if ( ( endMonthIndex < startMonthIndex ) || ( endMonthIndex == startMonthIndex && endDateIndex < startDateIndex ) ) {
		    		JOptionPane.showMessageDialog(null,
		    			    "Start date earlier than end date. Resetting...",
		    			    "Date error",
		    			    JOptionPane.ERROR_MESSAGE);
		    		reset();
		    		return;
		    	}
		    	
		    	List monthString = new List();
	        	List dayString = new List();
	        	int listSize = 0;
	        	
	        	int dayName = startDayIndex;
		    	
		    	for ( int currentMonth = startMonthIndex; currentMonth <= endMonthIndex; currentMonth++ ){
		    		
		    		int startDay = 0;
		    		if ( currentMonth == startMonthIndex ) startDay = startDateIndex;
		    		
		    		int endDay = 0;
		    		
		    		if ( currentMonth == endMonthIndex ){
		    			endDay = endDateIndex + 1;
		    		} else {
		    			endDay = year.month[ currentMonth ].day.length;
		    		}
		    		
		    		for ( int currentDay = startDay; currentDay < endDay; currentDay++ ){
		    			monthString.add( EnglishMonth.monthNames()[ currentMonth ] + " " + year.value );
		    			dayString.add( EnglishDay.dayNames()[ dayName ] + " " + year.month[ currentMonth ].day[ currentDay ].toString() );
		    			dayName++;
		    			listSize++;
			    		if ( dayName > 6 ) dayName = 0;
		    		}
		    		
		    	}
		    	
		    	String str = "Start Date: " + UserInterface.stDyJList.getSelectedValue() + ", " + UserInterface.stDateJList.getSelectedValue() + ", " + UserInterface.stMnJList.getSelectedValue() + "\n";
		    	str = str + "End Date: " + UserInterface.endDateJList.getSelectedValue() + " " + UserInterface.endMnJList.getSelectedValue() + "\n";
		    	
		    	int result = UserInterface.createOptionDialog(str);
		    	
		    	if ( result == UserInterface.getOKJOptionValue() ){
		    		
		    		for( int i = 0; i < listSize; i++){
						PrintDiarySheet sheet = new PrintDiarySheet( dayString.getItem( i ), monthString.getItem( i ) );
						int timeout_ms = 5000;//10 * 1000
						Timer timer = new Timer();
						timer.schedule(new CloseDialogTask( sheet ), timeout_ms);
						ui.dataSent( dayString.getItem( i ), monthString.getItem( i ) );
		    		}
		    		
		    	}
		    	
		    }
		}
		);
		
		/*
		 * start day list
		 * get selected index
		 * disable start day list
		 * enable start month list
		 * add detail
		 * 
		 */
		
		UserInterface.stDyJList.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if ( UserInterface.stDyJList.getValueIsAdjusting() ){
					setStartDayIndex( UserInterface.stDyJList.getSelectedIndex() );
					UserInterface.stDyJList.setEnabled(false);
					UserInterface.stMnJList.setEnabled(true);
					UserInterface.detailLabel.setText( UserInterface.detailLabel.getText() + " ||| Start Date: " + UserInterface.stDyJList.getSelectedValue() );
				}
			}
			
		});
		
		/*
		 * start month list
		 * get index, validate for reset
		 * get start month index
		 * set model for start date list depending on month chosen
		 * enable start date list
		 * add detail
		 * 
		 */
		
		UserInterface.stMnJList.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if ( UserInterface.stMnJList.getValueIsAdjusting() ){
					
					if ( UserInterface.stMnJList.getSelectedIndex() >= 0 ){
						setStartMonthIndex( UserInterface.stMnJList.getSelectedIndex() );
						UserInterface.stMnJList.setEnabled(false);
						
						int startMonthLength = year.month[ getStartMonthIndex() ].day.length;
						
						DefaultListModel<String> model = new DefaultListModel<String>();
						
						for ( int i = 0; i < startMonthLength; i++ ){
							model.addElement( year.month[ getStartMonthIndex() ].day[ i ].toString() );
						}
						
						UserInterface.stDateJList.setModel(model);
						
						UserInterface.detailLabel.setText( UserInterface.detailLabel.getText() + ", " + UserInterface.stMnJList.getSelectedValue() );
						
						UserInterface.stDateJList.setEnabled(true);
					}
					
				}
			}
			
		});
		
		/*
		 * get start date index
		 * disable start date index
		 * enable end month list
		 * add detail
		 */
		
		UserInterface.stDateJList.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if ( UserInterface.stDateJList.getValueIsAdjusting() ){
					setStartDateIndex( UserInterface.stDateJList.getSelectedIndex() );
					UserInterface.stDateJList.setEnabled(false);
					UserInterface.detailLabel.setText( UserInterface.detailLabel.getText() + " " + UserInterface.stDateJList.getSelectedValue() );
					UserInterface.endMnJList.setEnabled(true);
				}
			}
			
		});
		
		/*
		 * validate for reset
		 * set end month index
		 * set model for end date list
		 * enable end date list
		 * add detail
		 */
		
		UserInterface.endMnJList.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if ( UserInterface.endMnJList.getValueIsAdjusting() ){
					
					if ( UserInterface.endMnJList.getSelectedIndex() >= 0 ){
					
						setEndMonthIndex( UserInterface.endMnJList.getSelectedIndex() );
						UserInterface.endMnJList.setEnabled(false);
						
						int endMonthLength = year.month[ getEndMonthIndex() ].day.length;
						
						DefaultListModel<String> model = new DefaultListModel<String>();
						
						for ( int i = 0; i < endMonthLength; i++ ){
							model.addElement( year.month[ getEndMonthIndex() ].day[ i ].toString() );
						}
						
						UserInterface.endDateJList.setModel(model);
						
						UserInterface.endDateJList.setEnabled(true);
						
						UserInterface.detailLabel.setText( UserInterface.detailLabel.getText() + " ||| End Date: " + UserInterface.endMnJList.getSelectedValue() );
					
					}
				}
			}
			
		});
		
		/*
		 * set end date
		 * enable print button
		 */
		
		UserInterface.endDateJList.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if ( UserInterface.endDateJList.getValueIsAdjusting() ){
					setEndDateIndex( UserInterface.endDateJList.getSelectedIndex() );
					UserInterface.endDateJList.setEnabled(false);
					UserInterface.detailLabel.setText( UserInterface.detailLabel.getText() + ", " + UserInterface.endDateJList.getSelectedValue() );
					ui.printButton.setEnabled(true);
				}
			}
			
		});
	}
	
	/*
	 * 
	 * yearPanel()
	 * 
	 * setup year window
	 * 
	 * return null
	 * 
	 */
	
	private static void yearPanel(){
		
		JPanel content = ui.yearWindow();
	
	    window.setContentPane(content);
	    window.setSize(300, 150);
	    window.setLocation(100,100);
	    window.setVisible(true);
		
	}
	
	/*
	 * 
	 * calendarpanel()
	 * 
	 * setup date selection panel
	 * 
	 * return null
	 * 
	 */
	
	private static void calendarPanel(){
		
		// create method in UserInterface to
		JPanel content = ui.calendarWindow();
		
		UserInterface.detailLabel.setText( "Year: " + year.value );
		
	    window.setContentPane(content);
	    window.setVisible(true);
		
	}
	
	/*
	 * 
	 * reset()
	 * 
	 * reset components of application
	 * 
	 * return null
	 * 
	 */
	
	private static void reset(){
    	JPanel contentPane = (JPanel) window.getContentPane();
    	contentPane.removeAll();
    	window.setSize(300, 150);
    	contentPane.revalidate(); 
    	contentPane.repaint();
    	year = null;
    	ui.reset();
    	yearPanel();
	}
	
	/*
	 * setter and getters for application
	 * 
	 */
	
	public static int getStartMonthIndex() {
		return startMonthIndex;
	}

	public static void setStartMonthIndex(int startMonthIndex) {
		JenynsBookingSheet.startMonthIndex = startMonthIndex;
	}

	public static int getStartDayIndex() {
		return startDayIndex;
	}

	public static void setStartDayIndex(int startDayIndex) {
		JenynsBookingSheet.startDayIndex = startDayIndex;
	}

	public static int getStartDateIndex() {
		return startDateIndex;
	}

	public static void setStartDateIndex(int startDateIndex) {
		JenynsBookingSheet.startDateIndex = startDateIndex;
	}

	public static int getEndMonthIndex() {
		return endMonthIndex;
	}

	public static void setEndMonthIndex(int endMonthIndex) {
		JenynsBookingSheet.endMonthIndex = endMonthIndex;
	}

	public static int getEndDateIndex() {
		return endDateIndex;
	}

	public static void setEndDateIndex(int endDateIndex) {
		JenynsBookingSheet.endDateIndex = endDateIndex;
	}
	
	/*
	 * close dialog box class
	 * closes print confirmation after 5 seconds
	 * displays errors if appropriate
	 */
	private static class CloseDialogTask extends TimerTask {
		
		private boolean sent;
		private boolean complete;
		private boolean error;
		private boolean cancelled;
		
		private String day;
		private String month;
		
		public CloseDialogTask( PrintDiarySheet sheet) {
			// TODO Auto-generated constructor stub
			
			sent = sheet.sent;
			complete = sheet.complete;
			error = sheet.error;
			cancelled = sheet.cancelled;
			
			day = sheet.dayString;
			month = sheet.monthString;
			
			
		}
		
		  public void run() {
		    //close dialog code goes here
			  
			  if ( sent ) ui.printAlert.setVisible(false);
			  if ( complete ) UserInterface.createMessageDialog("alert", "print job completed for " + day + " " + month );
			  if ( error ) UserInterface.createMessageDialog("alert", "print job failed for " + day + " " + month );
			  if ( cancelled ) UserInterface.createMessageDialog("alert", "print job cancelled for " + day + " " + month );
		  }
		}
}
