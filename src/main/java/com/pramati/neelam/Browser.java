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

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;




public class Browser {
    
    private Set<String> pagesVisited = new LinkedHashSet<String>();
    private List<String> pagesToVisit = new LinkedList<String>();
    private List<String> emailLinks = new LinkedList<String>();
    
   private enum Month {DUMMY, JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER}
   public static final java.util.Properties properties = System.getProperties();
	
	public static final String separator = properties.get("file.separator").toString();
	public static final String home = properties.get("user.home").toString();
 	
 	public static final String parentDirectory = home+separator+"Neelam";
 	
    
 	/**
 	 * Default constructor cleaning the resulting Parent Directory.
 	 */
 	
 	public Browser()
 	{
 		this.cleanDirectory();
 	}
 	
 	
 	/**
     * A Method returning the next URL for parsing.
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
 	 * Method to delete existing directory.
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
    			this.pagesVisited.add(url);
    		}
    		else
    		{
    			currentUrl = this.nextUrl();
    			if(currentUrl!=null)
    			{
    				Crawler mailCrawler = new Crawler();
    				String path = makeDirectory(currentUrl);
    				mailCrawler.retrieveLinks(currentUrl);
    				this.emailLinks.addAll(mailCrawler.getLinks());
    				writeEmails(path);
    			}
    		}
    		if(currentUrl!= null)
    		{
    			pageCrawler.crawl(currentUrl, searchWord);
    			this.pagesToVisit.addAll(pageCrawler.getLinks());
 		    }
      		
    	}while(!(this.pagesToVisit.isEmpty()));
    }
    
    /**
     * Method to write all emails to separate files based on the links present on current HTML webpage. 
     * @param dirPath - The path of the directory where the file will be placed.
     */
    
    private void writeEmails(String dirPath)
    {
    	
    	int count = new File(dirPath).listFiles().length;    	
    	while(!this.emailLinks.isEmpty())
        {
    		count++;
    		try{
    	    File emailText = new File(dirPath+separator+"email#"+count+".txt");
    	    emailText.createNewFile();
    	    if(emailText.isFile())
    	    {
    	    	
    	    	BufferedWriter writer = new BufferedWriter(new FileWriter(emailText));
    	    	writer.write(downloadPage(this.emailLinks.remove(0)));
    	    	writer.close();
    	    }
    	    
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
    	
    	String subDirectory = null;
    	int begin = pageName.indexOf("2014");
    	int month = Integer.parseInt(pageName.substring(begin+4, begin+6));
    	subDirectory = Month.values()[month].toString();
        File directory = new File(parentDirectory+separator+subDirectory);
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
