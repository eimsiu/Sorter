package com.sorter.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

import com.google.gwt.user.client.Cookies;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */

public class Sorter implements EntryPoint {
	
	private static TextArea TAinput = new TextArea();
	private HorizontalPanel MainPanel = new HorizontalPanel();
	private VerticalPanel InputPanel = new VerticalPanel();
	private VerticalPanel OutputPanel = new VerticalPanel();
	private Button sortButton = new Button("Sort");
	private static FlexTable OutputTable = new FlexTable();
	  
	
	public void onModuleLoad() {
		
		//check if browser was closed or refreshed so we can reload data in TAinput again
		if (BrowserCloseDetector.get().wasClosed()) {
	        GWT.log("Browser was closed.");
	    }
	    else {
	        GWT.log("Refreshing or returning from another page.");
	        String test = Cookies.getCookie("detector");
	        TAinput.setText(test);
	    }
		
		//set-up TAinput
		TAinput.setCharacterWidth(80);
		TAinput.setVisibleLines(10);
		TAinput.getElement().setPropertyString("placeholder", "enter data text here");
		
		//set-up OutputTable
		OutputTable.setText(0, 0, "Nr");
		OutputTable.getRowFormatter().addStyleName(0, "OutputTableHeader");
		OutputTable.addStyleName("OutputTable");
		OutputTable.setVisible(false);
		
		//insert items into panel
		InputPanel.add(TAinput);
		InputPanel.add(sortButton);
		
		//insert items into panel
		OutputPanel.add(OutputTable);
		
		//Set-up Main Panel
		MainPanel.setWidth("100%");
		MainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	    MainPanel.add(InputPanel);
	    MainPanel.add(OutputPanel);
	    
	    //Insert into div sorter content(MainPanel)
	    RootPanel.get("sorter").add(MainPanel);
	    
	    // Listen for mouse events on the Sort button.
		sortButton.addClickHandler(new ClickHandler() {
	      public void onClick(ClickEvent event) {
	          sorter();
	      }
	    });
		
	}
	
	private void sorter() {
		//initial variables
		String content;
		Line[] lines = new Line[50];
        int n = 0;
        
        ClearTable();					//clears table of all content
		content = TAinput.getText(); 	//get data
		n = ReadData(content,lines,n);  //prepare data for sorting
		Sort(lines,n); 					//sort data
		OutputData(lines,n); //prepare data for printing
		OutputTable.setVisible(true);

	}
	
	public static void ClearTable() {
		int count = OutputTable.getRowCount();	//cause OutputTable.clear() seems to not be deleting the content properly
        for (int i = count-1; i > 0; i--) {
        	OutputTable.removeRow(i);
        }
	}
	 
	/**
	 * Reads data from content and prepares it for the sort method
	 * @param content - contains the whole string with data
	 * @param lines - keeps array of Line (which represents data of 1 line)
	 * @param n - the total amount of lines
	 * @return n - returns the amount of lines
	 */
	public static int ReadData(String content, Line[] lines, int n) {

		String[] words = new String[50];
        int counter = 0;
        
		String[] scanner = content.split("\n");
		for (String s: scanner) {
			counter = 0;
			words = new String[50];
		  String[] array = s.split("\\s");
      		for (String a: array) {
      			words[counter++] = a;
      		}
      		Line l = new Line(counter,words);
            lines[n++] = l;
		}
        return n;
    }


	
	/**
	 * Sorts data using the Bubble method
	 * @param lines - array of Line (Line represents 1 line of data)
	 * @param n - amount of lines
	 */
	public static void Sort(Line[] lines, int n){
        int sk;
        Line tmp;
        for (int i = 0; i < n-1; i++){
            for (int j = 0; j<n-i-1; j++){
                boolean same = true;	//bool keeps tracks if lines are the same
                
                sk = findSk(lines[j].getN(),lines[j+1].getN()) ;	//finds the line with less columns
                int k;
                for ( k = 0; k < sk; k++){
                    if (lines[j].getWord(k) == null ? lines[j+1].getWord(k) != null : !lines[j].getWord(k).equals(lines[j+1].getWord(k))){ //checks if both lines aren't empty and then checks if they are the same
                        same = false;
                        break;
                    }
                }
                if (sk == 0)    //workaround for empty lines
                    same = false;
                //if the lines are the same for k times
                if (same){
                    if (lines[j].getN() > lines[j+1].getN()){   //if the Line.n amount is different, changes are needed ("123" will be higher then ("123" "1") )												
                        tmp = lines[j];
                        lines[j] = lines[j+1];
                        lines[j+1] = tmp;
                    }
                }
                //lines are not the same
                //order goes => number,space,word
                else{
                    int prio1 = -1; //-1 - error, 0- number, 1- space, 2- word
                    int prio2 = -1;
                    boolean change = false;
                    
                    if(lines[j].getN() == 0)
                        prio1 = 1;  //in case empty line
                    else
                        prio1 = findPrio(lines[j].getWord(k));
                    
                    if(lines[j+1].getN() == 0)
                        prio2 = 1;  //in case empty line
                    else
                        prio2 = findPrio(lines[j+1].getWord(k));
                    
                    if (prio1 > prio2) //if priority already can be determined (cause numbers go before spaces, etc.)
                        change = true;
                        
                    if (prio1 == 0 && prio2 == 0){  //if both numbers, which is smaller?
                        if (findSmallerNumber(lines[j].getWord(k), lines[j+1].getWord(k)) )
                            change = true;
                    }
                    
                    if (prio1 == 2 && prio2 == 2){//if both words, which needs to go first
                        sk = findSk(lines[j].getWord(k).length(),lines[j+1].getWord(k).length()); //finds the length of the shorter word
                        for (int p = 0; p < sk; p++){
                            if(lines[j].getWord(k).charAt(p) > lines[j+1].getWord(k).charAt(p)){
                                change = true;
                                break;
                            }
                        }
                        if (!change && lines[j].getWord(k).length() > lines[j+1].getWord(k).length() ) //case: "word" vs "words". "word" must go first.
                            change = true;
                    }
                    
                    
                    if(change){
                        tmp = lines[j];
                        lines[j] = lines[j+1];
                        lines[j+1] = tmp;  
                    }
                    
                }
            }
        }
    }
    
	
    /**
     * Finds what kind of priority the string has
     * @param s- String of the line
     * @return priority number (0-number, 1-empty line, 2-word)
     */
    private static int findPrio(String s){
        if(isNumeric(s))
            return 0;
        if(s.isEmpty() || s == null)
            return 1;
        else
            return 2;
    }
    
    
    /**
     * Finds smaller number
     * @param n - first number
     * @param m - second number
     * @return smaller number
     */
    private static int findSk(int n, int m){
        if (n == m || n < m)
            return n;
        else
            return m;
    }
    
    /**
     * Returns true/false if the number in string s1 is smaller than in string s2 (we know they are already numbers because of their priority)
     * @param s1 - number in string
     * @param s2 - number in string
     * @return if the number is smaller
     */
    private static boolean findSmallerNumber(String s1, String s2){
        double a = Double.parseDouble(s1);
        double b = Double.parseDouble(s2);
        
        if (a > b)
            return true;
        else
            return false;
    }
    
    /**
     * Checks whether the string is a number
     * @param str - The string that will be attempted to be parsed
     * @return if the string can be a number
     */
    public static boolean isNumeric(String str)  
    {  
        try  
        {  
            double d = Double.parseDouble(str);  
        }  
        catch(NumberFormatException nfe)  
        {
            return false;
        }
            return true;
    }
    
    
    /**
     * Generates the FlexTable
     * @param lines - array of Line (Line represents 1 line of data)
	 * @param n - amount of lines
     */
    public static void OutputData(Line[] lines, int n) {
        for (int i=0; i<n; i++){
        	OutputTable.setText(i+1, 0, String.valueOf(i+1));
            for(int k=0; k<lines[i].getN(); k++){
                OutputTable.setText(i+1, k+1, lines[i].getWord(k));
                OutputTable.getCellFormatter().addStyleName(i+1, k+1, "OutputTableRow");
            }
        }
    }
    

    /* src: https://stackoverflow.com/questions/5203856/gwt-detect-browser-refresh-in-closehandler */
    public static class BrowserCloseDetector {
        private final String COOKIE = "detector";
        private static BrowserCloseDetector instance;

        private BrowserCloseDetector() {
            Window.addWindowClosingHandler(new Window.ClosingHandler() {
                public void onWindowClosing(Window.ClosingEvent closingEvent) {
                	String data = TAinput.getText();
                    Cookies.setCookie(COOKIE, data);
                }
            });
        }

        public static BrowserCloseDetector get() {
            return (instance == null) ? instance = new BrowserCloseDetector() : instance;
        }

        public boolean wasClosed() {
            return Cookies.getCookie(COOKIE) == null;
        }
    }
}
