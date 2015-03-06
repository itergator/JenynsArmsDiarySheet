package ui;

/*
 * Application to print Jenyns Arms Diary Sheet
 * 
 * Copyright 2014 L Hughes
 * 
 * UserInterface.java
 * 
 */

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import model.calendar.day.EnglishDay;
import model.calendar.month.EnglishMonth;

/*
 * class UserInterface
 */

public class UserInterface {
	
	/*
	 * public variables
	 */
	
	public JTextField yearTextField;
	
	public JLabel yearErrorLabel;
	public static JLabel detailLabel;
	
	public JButton yearOKButton;
	public JButton resetButton;
	public JButton printButton;
	
	public static JList<String> stDyJList = new JList<String>( EnglishDay.dayNames() );
	public static JList<String> stMnJList = new JList<String>( EnglishMonth.monthNames() );
	public static JList<String> stDateJList = new JList<String>( );
	public static JList<String> endMnJList = new JList<String>( EnglishMonth.monthNames() );
	public static JList<String> endDateJList = new JList<String>( );
	
	public JDialog printAlert = new JDialog();
	
	/*
	 * setup UserInterface
	 * 
	 * return null
	 */
	
	public UserInterface(){
		yearTextField = new JTextField("");
		yearErrorLabel = new JLabel("");
		yearOKButton = new JButton("OK");
		resetButton = new JButton("Reset");
		printButton = new JButton("Print");
		detailLabel = new JLabel();
	}
	
	/*
	 * create and setup Year Window
	 * 
	 * return null
	 */
	
	public JPanel yearWindow(){
		
		JPanel internalYearPanel = internalYearJPanel();
		JPanel content = mainYearPanel( internalYearPanel );
		
		return content;
	}
	
	/*
	 * create and setup main year panel
	 * 
	 * return JPanel content
	 */
	
	private JPanel mainYearPanel(JPanel internalYearPanel) {
		JPanel content = new JPanel();
		
		content.setLayout(new BorderLayout());
		content.add(internalYearPanel, BorderLayout.CENTER);
		content.add(yearOKButton, BorderLayout.SOUTH);
		
	    return content;
	}

	/*
	 * create and setup inner year panel
	 * 
	 * return JPanel yearPanel
	 */
	private JPanel internalYearJPanel(){
		
		JPanel yearPanel = new JPanel();
		JLabel yearLabel = new JLabel("Enter year:");

		yearPanel.setLayout( new BorderLayout() );
		yearPanel.setBorder(new EmptyBorder(10, 10, 10, 10) );
		
		yearLabel.setBorder( new EmptyBorder( 0, 0, 10, 0) );
		
		yearPanel.add(yearLabel, BorderLayout.NORTH);
		yearPanel.add(yearTextField, BorderLayout.CENTER);
		yearErrorLabel.setPreferredSize( new Dimension( 16, 16 ) );
		yearPanel.add(yearErrorLabel, BorderLayout.SOUTH);
		
		return yearPanel;
	}
	
	/*
	 * create and setup calendar window
	 * 
	 * add start date panel
	 * add end date panel
	 * add detail panel
	 * 
	 * return JPanel content
	 * 
	 */
	
	public JPanel calendarWindow(){
		// draws list boxes
		
		
		JPanel content = new JPanel();
		GridLayout dateSelectLayout = new GridLayout(3,0);
		
		JPanel dateSelectPanel = new JPanel();
		dateSelectPanel.setLayout( dateSelectLayout );
		dateSelectPanel.add( startDatePanel( ) );
		dateSelectPanel.add( endDatePanel( ) );
		dateSelectPanel.add( detailPanel( ) );
		
		printButton.setEnabled(false);
		
		content.setLayout( new BorderLayout() );
		content.add( resetButton, BorderLayout.NORTH );
		content.add( dateSelectPanel, BorderLayout.CENTER );
		content.add( printButton, BorderLayout.SOUTH );
		
		return content;
	}
	
	/*
	 * create and setup startDatePanel
	 * 
	 * return JPanel
	 */
	
private static JPanel startDatePanel(){
		
	JPanel daySelectionPanel = new JPanel();
	daySelectionPanel.setLayout( new GridBagLayout() );
	GridBagConstraints constraints = new GridBagConstraints();
	
	constraints.weightx = 0.5;
	constraints.weighty = 0.2;
	constraints.ipadx = 600;
	constraints.fill = GridBagConstraints.BOTH;
	
	constraints.gridx = 0;
	constraints.gridy = 0;
	constraints.gridwidth = 3;
	JLabel label = new JLabel("Start Date Information");
	label.setHorizontalAlignment(SwingConstants.CENTER);
	daySelectionPanel.add( label, constraints );
	
	constraints.gridx = 0;
	constraints.gridy = 1;
	constraints.gridwidth = 1;
	label = new JLabel("Start Day");
	label.setHorizontalAlignment(SwingConstants.CENTER);
	daySelectionPanel.add( label, constraints );
	
	constraints.gridx = 1;
	constraints.gridy = 1;
	label = new JLabel("Start Month");
	label.setHorizontalAlignment(SwingConstants.CENTER);
	daySelectionPanel.add( label, constraints );
	
	constraints.gridx = 2;
	constraints.gridy = 1;
	label = new JLabel("Start Date");
	label.setHorizontalAlignment(SwingConstants.CENTER);
	daySelectionPanel.add( label, constraints );
	
	constraints.weighty = 1;
	constraints.gridx = 0;
	constraints.gridy = 2;
	constraints.gridwidth= 1;
	daySelectionPanel.add(stDyJList, constraints);
	
	constraints.gridx = 1;
	constraints.gridy = 2;
	stMnJList.setEnabled(false);
	daySelectionPanel.add( new JScrollPane( stMnJList ), constraints );
	
	constraints.gridx = 2;
	constraints.gridy = 2;
	
	stDateJList.setEnabled(false);
	daySelectionPanel.add( new JScrollPane( stDateJList ), constraints );

	return daySelectionPanel;
	}

/*
 * create and setup endDatePanel
 * 
 * return JPanel
 */
	
	private static JPanel endDatePanel() {
		
		JPanel daySelectionPanel = new JPanel();
		daySelectionPanel.setLayout( new GridBagLayout() );
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.weightx = 0.5;
		constraints.weighty = 0.2;
		constraints.ipadx = 600;
		constraints.fill = GridBagConstraints.BOTH;
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		JLabel label = new JLabel("End Date Information");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		daySelectionPanel.add( label, constraints );
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		label = new JLabel("End Month");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		daySelectionPanel.add( label, constraints );
		
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		label = new JLabel("End Date");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		daySelectionPanel.add( label, constraints );
		
		constraints.gridx = 0;
		constraints.gridy = 2;
		endMnJList.setEnabled(false);
		
		constraints.ipady = 10;
		constraints.weighty = 10;
		daySelectionPanel.add( new JScrollPane( endMnJList ), constraints );
		
		constraints.gridx = 1;
		constraints.gridy = 2;
		endDateJList.setEnabled(false);
		daySelectionPanel.add( new JScrollPane( endDateJList ), constraints );
		
		return daySelectionPanel;
	}
	
	/*
	 * create and setup detail panel
	 * 
	 * return JPanel
	 */
	
	private static JPanel detailPanel( ){
		JPanel panel = new JPanel();
		
		panel.add(detailLabel);
		
		return panel;
	}
	
	/*
	 * reset
	 * 
	 * reset user interface
	 * 
	 * return null
	 */
	
	public void reset(){
		yearErrorLabel.setText(null);
		detailLabel.setText(null);
		
		if ( stDateJList.getModel().getSize() != 0){
			DefaultListModel<String> model = (DefaultListModel<String>) stDateJList.getModel();
			model.removeAllElements();
		}
		
		if ( endDateJList.getModel().getSize() != 0){
			DefaultListModel<String> model = (DefaultListModel<String>) endDateJList.getModel();
			model.removeAllElements();
		}
		
		stDyJList.clearSelection();
		stMnJList.clearSelection();
		endMnJList.clearSelection();
		
		stDyJList.setEnabled(true);
		stDateJList.setEnabled(false);
		stMnJList.setEnabled(false);
		
		endDateJList.setEnabled(false);
		endMnJList.setEnabled(false);
		
		detailLabel.setText( "" );
		
	}
	
	/*
	 * create option dialog
	 * 
	 * returns result from panel
	 */
	
	public static int createOptionDialog( String str ){
		Object[] options = { "OK", "CANCEL" };
    	int result = JOptionPane.showOptionDialog(null, str, "Check dates before continuing...",
    	JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
    	null, options, options[0]);
    	
    	return result;
	}
	
	/*
	 * create message dialog
	 * 
	 * returns null
	 */
	
	public static void createMessageDialog( String title, String msg ){
		JOptionPane.showMessageDialog(null, title, msg, JOptionPane.ERROR_MESSAGE);
	}
	
	/*
	 * get OK value from option dialog
	 * 
	 * return integer value for user interface of OK button on JOptionPane
	 */
	
	public static int getOKJOptionValue(){
		return JOptionPane.OK_OPTION;
	}
	
	/*
	 * setup dialog for print alert when data
	 * sent to printer
	 * 
	 * return null
	 */
	
	public void dataSent( String day, String month ){
		String msg = "Data sent to printer for " + day + " " + month;

		JOptionPane optionPane = new JOptionPane();
	    optionPane.setMessage(msg);
	    optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
	    printAlert = optionPane.createDialog( null, day + " " + month );
	    printAlert.setVisible(true);
	}
}