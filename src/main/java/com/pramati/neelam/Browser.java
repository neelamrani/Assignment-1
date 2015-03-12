package com.pramati.neelam;

/**
 * A class to perform search on the given set of URLs.
 */


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Browser {
    
    private Set<String> pagesVisited = new LinkedHashSet<String>();
    private List<String> pagesToVisit = new LinkedList<String>();
    private List<String> emailLinks = new LinkedList<String>();
    
   // private enum Month {JANUARY=1, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER}
    
    
    /**
     * A function returning the next URL for parsing.
     * It also ensures that Duplicate URLs are not allowed.
     * @return
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
     * A function to perform searching/parsing of the web document.
     * @param url
     *         - The URL of the web page for parsing.
     * @param searchWord
     *         - The word to be searched.
     */
    
    public void search(String url, String searchWord)
    {
    	do
    	{
    		
    		String currentUrl;
    		Crawler pageCrawler = new Crawler();
    		if(this.pagesToVisit.isEmpty())
    		{
    			currentUrl = url;
    			
    		}
    		else
    		{
    			currentUrl = this.nextUrl();
    			searchWord = "date?";
    		}
    		if(currentUrl!= null){
    		pageCrawler.crawl(currentUrl, searchWord);
    		
    		
    		this.pagesToVisit.addAll(pageCrawler.getLinks());}
    		
    		
    	}while(!(this.pagesToVisit.isEmpty()));
    	
    																																																								
    	this.pagesToVisit.addAll(pagesVisited);
    	
    	
    	while(!pagesToVisit.isEmpty())
    	{
    		
    		Crawler mailCrawler = new Crawler();
    		String page = pagesToVisit.remove(0);
    		String path = makeDirectory(page);
    		mailCrawler.crawl(page, "@");
    		this.emailLinks.addAll(mailCrawler.getLinks());
    		writeEmails(path);
    	}
    }
    
    /**
     * Method to write all emails to separate files based on the links present on current HTML webpage. 
     * @param dirPath - The path of the directory where the file will be placed.
     */
    
    private void writeEmails(String dirPath)
    {
    	
    	int count = new File(dirPath).listFiles().length;
    	java.util.Properties properties = System.getProperties();
    	properties.list(System.out);
    	String separator = properties.get("file.separator").toString();
    	while(!this.emailLinks.isEmpty())
        {
    	  try{
    	    File emailText = new File(dirPath+separator+"email#"+count+".txt");
    	    emailText.createNewFile();
    	    if(emailText.isFile())
    	    {
    	    	
    	    	BufferedWriter writer = new BufferedWriter(new FileWriter(emailText));
    	    	writer.write(downloadPage(this.emailLinks.remove(0)));
    	    	writer.close();
    	    }
    	    count++;
    	   }
    	  catch(Exception e)
    	  {
    		System.out.println("IO Exception while creating File");
    		e.printStackTrace();
    	  }
    		
    	}
    	
	
    }
    
    /**
     * A method to make relevant directories organized Month wise.
     * @param pageName: Path for the Directory to write.
     * @return Absolute Path of the Directory.
     */
    
    private String makeDirectory(String pageName)
    {
    	java.util.Properties properties = System.getProperties();
    	properties.list(System.out);
    	String home = properties.get("user.home").toString();
    	String separator = properties.get("file.separator").toString();
    	String directoryName = "Neelam";
    	String subDirectory = null;
    	int begin = pageName.indexOf("2014");
    	int month = Integer.parseInt(pageName.substring(begin+4, begin+6));
    	switch(month)
    	{
    	   case 1:
    		   subDirectory = "January";
    		   break;
    	   case 2:
    		   subDirectory = "February";
    		   break;
    	   case 3:
    		   subDirectory = "March";
    		   break;
    	   case 4:
    		   subDirectory = "April";
    		   break;
    	   case 5:
    		   subDirectory = "May";
    		   break;
    	   case 6:
    		   subDirectory = "June";
    		   break;
    	   case 7:
    		   subDirectory = "July";
    		   break;
    	   case 8:
    		   subDirectory = "August";
    		   break;
    	   case 9:
    		   subDirectory = "September";
    		   break;
    	   case 10:
    		   subDirectory = "October";
    		   break;
    	   case 11:
    		   subDirectory = "November";
    		   break;
    	   case 12:
    		   subDirectory = "December";
    		   break;
    	  default:
    		  subDirectory = ""+month;
    		   
    	}
    	
    	File directory = new File(home+separator+directoryName+separator+subDirectory);
    	directory.mkdirs();
    	return directory.getAbsolutePath();
    }
    
    /**
     * Method to download the E-mail text 
     * @param url - url of email
     * @return the E-mail text.
     */
    
     private String downloadPage(String url)
     {
    	 try
    	 {
    		
    		 
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
    	 catch(Exception e)
    	 {
    		 System.out.println("Error while downloading");
    		 e.printStackTrace();
    		 
    	 }
    	 return null;
     }
    
    
    
    
    
}
