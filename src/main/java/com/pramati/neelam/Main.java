package com.pramati.neelam;

import java.sql.Timestamp;

/**
 * A Testing Class containing the main method to parse the given URL
 * and calling the functions from main classes.
 */





public class Main {

	public static void main(String[] args) {
		
       try{
    	
    	 
    	   System.out.println("Web Crawler is started... "+new Timestamp(new java.util.Date().getTime()));
    	   
    	   Browser browser = new Browser();
    	 
    	   browser.search("http://mail-archives.apache.org/mod_mbox/maven-users/","2014");
    	 
    	   System.out.println("Web Crawler run complete... "+new Timestamp(new java.util.Date().getTime()));
    	   
       }
       catch(Exception e)
       { 	
    	 e.printStackTrace();   
       }
	}

}
