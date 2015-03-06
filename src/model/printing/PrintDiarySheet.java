package model.printing;

/*
 * Application to print Jenyns Arms Diary Sheet
 * 
 * Copyright 2014 L Hughes
 * 
 * PrintDiarySheet.java
 * 
 */

import java.awt.*;
import java.awt.print.*;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;
import javax.swing.JOptionPane;


/*
 * class PrintDiarySheet implements
 * 		Printable to print
 * 		PrintJobListener for feedback from printer
 */
public class PrintDiarySheet implements Printable, PrintJobListener {
	
	/*
	 * public feedback from printer
	 */
	public boolean sent = false;
	public boolean complete = false;
	public boolean error = false;
	public boolean cancelled = false;
	
	public String dayString;
	public String monthString;
	
	/*
	 * private constants for printing
	 */
	private static final int ZERO = 0;
	private static final float TITLESIZE = 24.0f;
	private static final float ROOMSIZE = 18.0f;
	private static final float TABLESIZE = 16.0f;
	private static final int PAGEMARGIN = 5;
	private static final int TITLETEXTMARGIN = 10;
	private static final int ROOMTEXTMARGIN = 8;
	private static final int TABLETEXTMARGIN = 8;
	private static final int CHALETOFFSET = 20;
	private static final String SUNDAY = "Sunday";
	
	/*
	 * PrintDiarySheet
	 * 
	 * setup for printing sheet including
	 * 		- page margins
	 * 		- A4 size
	 * 		- lookup services linked to computer
	 * 		- chooses preferred printer
	 * 
	 * prints page
	 */
	public PrintDiarySheet( String currentDay, String currentMonth) {
		// TODO Auto-generated constructor stub
		dayString = currentDay;
		monthString = currentMonth;
		
		/* Construct the print request specification.
         * The print data is a Printable object.
         * the request additonally specifies a job name, 2 copies, and
         * landscape orientation of the media.
         */
        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        
        pras.add(MediaSizeName.ISO_A4);
        int x = 10; //left and right margin
        int y = 10; //top and bottom margin. Note that bottom margin cannot be less than 15 mm
        int w = 190; //Width
        int h = 277; //Height
        int units = MediaPrintableArea.MM;
        pras.add(new MediaPrintableArea(x, y, w, h, units));

        /* locate a print service that can handle the request */
        PrintService services =
                PrintServiceLookup.lookupDefaultPrintService();

        if (services != null){
                /* create a print job for the chosen service */
                DocPrintJob pj = services.createPrintJob();

                try {
                        /* 
                        * Create a Doc object to hold the print data.
                        */
                        Doc doc = new SimpleDoc(this, flavor, null);

                        pj.addPrintJobListener(this);
                        
                        /* print the doc as specified */
                        pj.print(doc, pras);

                        /*
                        * Do not explicitly call System.exit() when print returns.
                        * Printing can be asynchronous so may be executing in a
                        * separate thread.
                        * If you want to explicitly exit the VM, use a print job 
                        * listener to be notified when it is safe to do so.
                        */

                } catch (PrintException e) { 
                        System.err.println(e);
                }
        } 
		
	}

	/*
	 * print
	 * setup page graphics prints
	 */
	
	@Override
	public int print(Graphics g, PageFormat pf, int page)
			throws PrinterException {
		// TODO Auto-generated method stub
		
		if (page > 0) { /* We have only one page, and 'page' is zero-based */
            return NO_SUCH_PAGE;
        }
 
        /* User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         */
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
 
        /* Now we perform our rendering */
        /* draw frame */
        g.drawRect( ZERO, ZERO, ( int )pf.getImageableWidth(), ( int )pf.getImageableHeight() );
        g.drawLine( ( int ) pf.getImageableWidth() / 2, ZERO, ( int ) pf.getImageableWidth() / 2, ( int ) pf.getImageableHeight() );
        
        int currentY = ZERO;
        
        /* draw title */
        currentY = ( int )TITLESIZE;
        g.setFont( g.getFont().deriveFont( TITLESIZE ) );
        g.setFont( g.getFont().deriveFont( Font.BOLD ) );
        g.drawString( monthString, PAGEMARGIN, currentY );
        
        /* calculate offset for right aligned text */
        FontMetrics fm = g.getFontMetrics( g.getFont() );
        int textWidth = fm.stringWidth( dayString);
        
        int dateTextPointX = ( int )pf.getImageableWidth() - PAGEMARGIN - textWidth;
        g.drawString(dayString, dateTextPointX, currentY );
        
        currentY = currentY + TITLETEXTMARGIN;
        
        g.drawLine( ZERO, currentY, ( int )pf.getImageableWidth(), currentY );
        
        currentY = currentY + ( int )ROOMSIZE;
		
        String str = dayString.substring(0, 6);
        
        if ( str.equals( SUNDAY ) ){
        	g = completeSunday( g, pf, currentY );
        } else {
        	g = completeDay( g, pf, currentY );
        }
		
		return PAGE_EXISTS;
	}
	
	/*
	 * CompleteSunday
	 * Sunday diary sheet different to rest of week
	 */
	
	private static Graphics completeSunday( Graphics g, PageFormat pf, int currentY ){
		
		int halfPageX = ( ( int ) pf.getImageableWidth() / 2 ) + PAGEMARGIN;
		
		g.setFont( g.getFont().deriveFont( ROOMSIZE ) );
        g.drawString( "Restaurant 12:00pm", PAGEMARGIN, currentY );
        g.drawString( "Restaurant 6:00pm", halfPageX, currentY );
        
        currentY = currentY + ROOMTEXTMARGIN;
        
        g.drawLine(ZERO, currentY, ( int )pf.getImageableWidth(), currentY );
        
        g.setFont( g.getFont().deriveFont( TABLESIZE ) );
        
        for ( int i = 1; i <= 7; i++ ){
        	currentY = currentY + ( int )TABLESIZE + TABLETEXTMARGIN;
        	
        	g.drawString( Integer.toString( i ), PAGEMARGIN, currentY );
            g.drawString( Integer.toString( i ), halfPageX, currentY );
            
            currentY = currentY + TABLETEXTMARGIN;
            
            g.drawLine( ZERO, currentY, ( int )pf.getImageableWidth(), currentY );
        }
        
        currentY = currentY + ( int )ROOMSIZE;
        
        g.setFont( g.getFont().deriveFont( ROOMSIZE ) );
        g.drawString( "Restaurant 2:00pm", PAGEMARGIN, currentY );
        g.drawString( "Restaurant 8:00pm", halfPageX, currentY );
        
        currentY = currentY + ROOMTEXTMARGIN;
        
        g.drawLine(ZERO, currentY, ( int )pf.getImageableWidth(), currentY );
        
        g.setFont( g.getFont().deriveFont( TABLESIZE ) );
        
        for ( int i = 1; i <= 7; i++ ){
        	currentY = currentY + ( int )TABLESIZE + TABLETEXTMARGIN;
        	
        	g.drawString( Integer.toString( i ), PAGEMARGIN, currentY );
            g.drawString( Integer.toString( i ), halfPageX, currentY );
            
            currentY = currentY + TABLETEXTMARGIN;
            
            g.drawLine( ZERO, currentY, ( int )pf.getImageableWidth(), currentY );
        }
        
        currentY = currentY + ( int )ROOMSIZE;
        
        g.setFont( g.getFont().deriveFont( ROOMSIZE ) );
        g.drawString( "Restaurant 4:00pm", PAGEMARGIN, currentY );
        
        currentY = currentY + ROOMTEXTMARGIN;
        
        g.drawLine(ZERO, currentY, ( int )pf.getImageableWidth(), currentY );
        
        g.setFont( g.getFont().deriveFont( TABLESIZE ) );
        
        for ( int i = 1; i <= 7; i++ ){
        	currentY = currentY + ( int )TABLESIZE + TABLETEXTMARGIN;
        	
        	g.drawString( Integer.toString( i ), PAGEMARGIN, currentY );
        	if ( i > 3 ) g.drawString( "Chalet "+ Integer.toString( i - 3 ), halfPageX, currentY );
            
            currentY = currentY + TABLETEXTMARGIN;
            
            g.drawLine( ZERO, currentY, ( int )pf.getImageableWidth(), currentY );
        }
        
        return g;
	}
	
	/*
	 * CompleteDay
	 * diary sheet different to rest of week
	 */
	
	private static Graphics completeDay(Graphics g, PageFormat pf, int currentY ){
		
		int halfPageX = ( ( int ) pf.getImageableWidth() / 2 ) + PAGEMARGIN;
		
		g.setFont( g.getFont().deriveFont( ROOMSIZE ) );
        g.drawString( "Restaurant a.m.", PAGEMARGIN, currentY );
        g.drawString( "Restaurant p.m.", halfPageX, currentY );
        
        currentY = currentY + ROOMTEXTMARGIN;
        
        g.drawLine(ZERO, currentY, ( int )pf.getImageableWidth(), currentY );
        
        g.setFont( g.getFont().deriveFont( TABLESIZE ) );
        
        for ( int i = 1; i <= 7; i++ ){
        	currentY = currentY + ( int )TABLESIZE + TABLETEXTMARGIN;
        	
        	g.drawString( Integer.toString( i ), PAGEMARGIN, currentY );
            g.drawString( Integer.toString( i ), halfPageX, currentY );
            
            currentY = currentY + TABLETEXTMARGIN;
            
            g.drawLine( ZERO, currentY, ( int )pf.getImageableWidth(), currentY );
        }
        
        currentY = currentY + ( int )ROOMSIZE; 
        
        g.setFont( g.getFont().deriveFont( ROOMSIZE ) );
        g.drawString( "Next Door a.m.", PAGEMARGIN, currentY );
        g.drawString( "Next Door p.m. ", halfPageX, currentY );
        
        currentY = currentY + ROOMTEXTMARGIN;
        
        g.drawLine(ZERO, currentY, ( int )pf.getImageableWidth(), currentY );
        
        g.setFont( g.getFont().deriveFont( TABLESIZE ) );
        
        for ( int i = 1; i <= 12; i++ ){
        	currentY = currentY + ( int )TABLESIZE + TABLETEXTMARGIN;
        	
        	g.drawString( Integer.toString( i ), PAGEMARGIN, currentY );
            g.drawString( Integer.toString( i ), halfPageX, currentY );
            
            currentY = currentY + TABLETEXTMARGIN;
            
            g.drawLine( ZERO, currentY, ( int )pf.getImageableWidth(), currentY );
        }
        
        currentY = currentY + CHALETOFFSET;
        
        g.drawLine(ZERO, currentY, ( int )pf.getImageableWidth(), currentY );
        
        currentY = currentY + ( int )TITLESIZE;
        
        g.drawString( "Chalet 1", PAGEMARGIN, currentY );
        g.drawString( "Chalet 2", halfPageX, currentY );
        
        currentY = currentY + TITLETEXTMARGIN;
        
        g.drawLine(ZERO, currentY, ( int )pf.getImageableWidth(), currentY );
        
        currentY = currentY + ( int )TITLESIZE;
        
        g.drawString( "Chalet 3", PAGEMARGIN, currentY );
        g.drawString( "Chalet 4", halfPageX, currentY );
        
        return g;
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.print.event.PrintJobListener#printDataTransferCompleted(javax.print.event.PrintJobEvent)
	 * 
	 * printJobListener methods
	 */

	@Override
	public void printDataTransferCompleted(PrintJobEvent pje) {
		// TODO Auto-generated method stub
		sent = true;
	}

	@Override
	public void printJobCompleted(PrintJobEvent pje) {
		// TODO Auto-generated method stub
		complete = true;
	}

	@Override
	public void printJobFailed(PrintJobEvent pje) {
		// TODO Auto-generated method stub
		error = true;
	}

	@Override
	public void printJobCanceled(PrintJobEvent pje) {
		// TODO Auto-generated method stub
		cancelled = true;
	}

	@Override
	public void printJobNoMoreEvents(PrintJobEvent pje) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printJobRequiresAttention(PrintJobEvent pje) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null, "alert", "printer requires attention...", JOptionPane.ERROR_MESSAGE);
		error = true;
	}

}
