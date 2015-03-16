package com.pramati.neelam;

/**
 * @author Neelam Rani
 * Browser is the main class for My Web Crawler which contains logic for how to fetch all relevant hyperlinks
 * and consequently store them in appropriate directory which is created as per native operating system
 * over which JAR is running.
 */


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;




public class Browser implements Serializable{
    
    private Set<String> pagesVisited = new LinkedHashSet<String>();
    private List<String> pagesToVisit = new LinkedList<String>();
    private List<String> emailLinks = new LinkedList<String>();
    private String currentUrl;
    private String path;
    
    
    
   private enum Month {DUMMY, JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER}
   public static final java.util.Properties properties = System.getProperties();
	
	public static final String separator = properties.get("file.separator").toString();
	public static final String home = properties.get("user.home").toString();
 	
 	public static final String parentDirectory = home+separator+"Neelam";
 	
    
 	/**
 	 * Default constructor cleaning the email directory created during previous run.
 	 */
 	
 	public Browser()
 	{
 		this.cleanDirectory();
 	}
 	
 	
 	/**
     * Method for returning the next URL for parsing.
     * It also ensures that Duplicate URLs are not allowed.
     * @return
     *   - next unvisited URL or null
     */
    
 	private String nextUrl()
    {
       String nextUrl;
       do
       {
    	   nextUrl = this.pagesToVisit.remove(0);
       }while(this.pagesVisited.contains(nextUrl) && !this.pagesToVisit.isEmpty());
      
       if(this.pagesVisited.add(nextUrl))
          return nextUrl;
          
       return null;
    }
    
 	
 	/**
 	 * Method for cleaning existing directory structure.
 	 */
 	
    private void cleanDirectory()
    {
    	try{
        File file = new File(parentDirectory);
        if(file.exists())
    	FileUtils.deleteDirectory(file);
    	}
    	catch (Exception e)
    	{
    		System.out.println("Cannot delete directory");
    		e.printStackTrace();
    	}
    }
   
    
    /**
     * Method for performing searching/parsing of the HTML page.   
     * @param url
     * - The URL of the HTML page for parsing.
     * @param searchWord
     * - Search String for parsing.
     * @param isRestore
     * - is restored from last run
     * @throws Exception
     */
    
    public void search(String url, String searchWord, boolean isRestore) throws Exception
    {
    	
    	do
    	{
    		//String currentUrl;
    		
    		Crawler pageCrawler = new Crawler();
    		
    		if(this.pagesToVisit.isEmpty())
    		{
      			this.currentUrl = url;
    			this.pagesVisited.add(url);
    		}
    		else
    		{
    			if(!isRestore){
    			  this.currentUrl = this.nextUrl();
    			}
    			else
    			{
    				isRestore = false;
    			}
    			if(this.currentUrl!=null)
    			{
    				Crawler mailCrawler = new Crawler();
    				this.path = makeDirectory(this.currentUrl);
    				mailCrawler.retrieveLinks(this.currentUrl); //point 1
    				append(this.emailLinks, mailCrawler.getLinks());
    				writeEmails(this.path); //point 2
    			}
    		}
    		if(currentUrl!= null)
    		{
    			pageCrawler.crawl(currentUrl, searchWord); //point 3
    			append(this.pagesToVisit, pageCrawler.getLinks());
 		    }
      		
    	}while(!(this.pagesToVisit.isEmpty()));
    }
    
    /**
     * 
     * @param list
     * @param hyperLinks
     */
    public void append(List<String> list, List<String> hyperLinks)
    {
    	for(String link : hyperLinks)
    	{
    		if(list.isEmpty() || !list.contains(link))
    			list.add(link);
    	}
    }
    
    /**
     * 
     * @return
     * @throws Exception
     */
    public boolean isCrawlingRequired() throws Exception
    {
    	if(!this.emailLinks.isEmpty())
    	{
    		writeEmails(path);
    	    return false;
    	}
    	return true;
    }
    
    /**
     * Method to write all emails to separate files based on the links present on current HTML webpage. 
     * @param dirPath - The path of the directory where the file will be located.
     */
    
    private void writeEmails(String dirPath) throws Exception
    {
    	
    	int count = new File(dirPath).listFiles().length + 1;  
    	File emailPath = new File(dirPath+separator+"email#"+count+".txt");
    	while(!this.emailLinks.isEmpty())
        {
    		
    		try{
    	    
    	    if(!emailPath.exists())
    	    {
    	     emailPath.createNewFile();
    	     if(emailPath.isFile())
    	     {
    	    	
    	    	BufferedWriter writer = new BufferedWriter(new FileWriter(emailPath));
    	    	writer.write(downloadPage(this.emailLinks.get(0)));//point 4
    	    	this.emailLinks.remove(0);
    	    	writer.close();
    	     } 
    	    }
    	    else
    	    {
    	   	this.emailLinks.remove(0);
    	     }
    	    count++;
    	    emailPath = new File(dirPath+separator+"email#"+count+".txt");
    	 }
    	 catch(Exception e)
    	  {
    		emailPath.delete();
    		throw e;
    		 //System.out.println("IO Exception while creating File");
    		//e.printStackTrace();
    	  }
    		
    	}	
    }
    
    /**
     * A method for creating relevant directories organized month-wise.
     * @param pageName: String containing the URL from which month details are retrieved.
     * @return Absolute Path of the created Directory.
     */
    
    private String makeDirectory(String pageName)
    {
    	
    	String subDirectory = null;
    	int begin = pageName.indexOf("2014");
    	int month = Integer.parseInt(pageName.substring(begin+4, begin+6));
    	subDirectory = Month.values()[month].toString();
        File directory = new File(parentDirectory+separator+subDirectory);
    	directory.mkdirs();
    	return directory.getAbsolutePath();
    }
    
    /**
     * Method for downloading the E-mail text 
     * @param url - String containing the hyperlink for the email HTML page.
     * @return returns the email text.
     */
    
     private String downloadPage(String url) throws Exception
     {
    	// try
    	// {	 
    		 Document email = Jsoup.connect(url).get();
    		 Elements preText = email.select("pre");
    		 StringBuffer pageBuffer = new StringBuffer(); 
    		 for (Element html : preText)
    		 {
    			 String text = html.text();
    		
    			 pageBuffer.append(text);
    		 }
    		 return pageBuffer.toString();
    		 
    		 
    	 }
    	/* catch(Exception e)
    	 {
    		 System.out.println("Error while downloading");
    		 e.printStackTrace();
    		 
    		 
    	 }
    	 return null;
     }*/
    
}
