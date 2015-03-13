package com.pramati.neelam;

/**
 * A Helper class containing the functions to perform HTTP requests, 
 * parsing the web pages and collecting URLs.
 */

import java.util.LinkedList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Crawler {
	
	
	private List<String> links = new LinkedList<String>();
	
	private Document htmlDocument;
  
	/**
	 * The Method to perform crawling i.e. perform HTTP request, 
	 * validate the response and parse to collect link of valid web pages.
	 * @param url
	 *    - current URL being visited.
	 * @return
	 *    - result of crawl operation.
	 */
		
	public void crawl(String url, String word)
	{
		try
		{
			
			Document htmlDocument = Jsoup.connect(url).get();
			this.htmlDocument = htmlDocument;
			
			Elements linksOnPage;
			
			
			linksOnPage = this.htmlDocument.select("a[href*="+word+"]~a[href*=date]");
			
			for(Element link: linksOnPage)
			{
				
				String str = link.absUrl("href");
				
				if(!links.contains(str) && !str.contains("?0"))
				   this.links.add(str);
				
			}
			
		}
		catch(Exception e)
		{
			System.out.println("Error in HTTP Request "+e);
			e.printStackTrace();
			
		}
	}

	public void retrieveLinks(String url)
	{
		
		try{
			Document htmlDocument = Jsoup.connect(url).get();
			this.htmlDocument = htmlDocument;
			Elements linksOnPage;
			linksOnPage = this.htmlDocument.select("a[href*=@]");
		
			for(Element link: linksOnPage)
			{
			
				String str = link.absUrl("href");
		
				this.links.add(str);
			}
		}
		catch(Exception e)
		{
			System.out.println("Error while retrieving links");
			e.printStackTrace();
		}
	}
	
	/**
	 * Getter Method for the list containing links.
	 * @return 
	 *  - returns the List.
	 */
	public List<String> getLinks()
	{
		return this.links;
	}
	
	
}
