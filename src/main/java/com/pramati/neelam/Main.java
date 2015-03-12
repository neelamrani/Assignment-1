package com.pramati.neelam;

/**
 * A Testing Class containing the main method to parse the given URL
 * and calling the functions from main classes.
 */





public class Main {

	public static void main(String[] args) {
		
       try{
    	
    	   System.out.println("Web Crawler is started...");
    	   
    	   Browser browser = new Browser();
    	   browser.search("http://mail-archives.apache.org/mod_mbox/maven-users/","date");
    	   
    	   System.out.println("Web Crawler run complete");
    	   
       }
       catch(Exception e)
       { 	
    	 e.printStackTrace();   
       }
	}

}
