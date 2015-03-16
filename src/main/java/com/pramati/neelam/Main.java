package com.pramati.neelam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;

/**
 * @author Neelam Rani
 * The testing class containing the main method to parse the given URL
 * and calling the functions from other classes.
 */





public class Main {
	static Browser browser;
    static File serialFile = new File("crawler.ser");
	public static void main(String[] args) {
		
       try{
    	    if(serialFile.exists())
    	    {
    	    	System.out.println("Last Run state is saved.\nPress Y to continue from last run?"+
    	          " or Press any other key to continue");
    	    	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    	    	String answer = reader.readLine();
    	    	if(answer.equalsIgnoreCase("Y"))
    	    	{
    	    		System.out.println("Restoring the state...");
    	    		FileInputStream fileStream = new FileInputStream(serialFile);
    	    		ObjectInputStream input = new ObjectInputStream(fileStream);
    	    		browser = (Browser)input.readObject();
    	    		input.close();
    	    		serialFile.delete();
    	    		restore();  		
    	    		return;
    	    	}
    	    	
    	    	
    	    }
        	    	serialFile.delete();
        	    	browser = new Browser();
        	    	run();
        	   
    	    
    	    
       }
       catch(Exception e)
       { 	
    	 
    	   System.out.println("Error! A problem occured while processing!!");
    	   try{
    	          
    		      System.out.println("Saving the state");
    		      FileOutputStream  fileStream = new FileOutputStream(serialFile);
    	          ObjectOutputStream output = new ObjectOutputStream(fileStream);
    	          output.writeObject(browser);
    	          output.close();
    	      }
    	   catch(Exception serialException)
    	   {
    		   System.out.println("Error while saving the state");
    		   serialException.printStackTrace();
    	   }
    	   e.printStackTrace();   
       }
	}
	
	
	public static void run() throws Exception
	{
		System.out.println("Web Crawler started... "+new Timestamp(new java.util.Date().getTime()));
   	 
 	    browser.search("http://mail-archives.apache.org/mod_mbox/maven-users/","2014", false);
 	 
 	   System.out.println("Web Crawler run complete... "+new Timestamp(new java.util.Date().getTime()));
	}

	public static void restore() throws Exception
	{
		System.out.println("Web Crawler restored... "+new Timestamp(new java.util.Date().getTime()));
	   	 
 	    browser.search("http://mail-archives.apache.org/mod_mbox/maven-users/","2014", browser.isCrawlingRequired());
 	 
 	   System.out.println("Web Crawler run complete... "+new Timestamp(new java.util.Date().getTime()));
	}
}
