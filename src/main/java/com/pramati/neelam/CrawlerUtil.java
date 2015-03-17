package com.pramati.neelam;

/**
 * @author Neelam Rani
 * This is the helper class and contains the logic for crawling the given URL so that an HTTP request using Jsoup
 * can be made and consequently the relevant hyperlinks are obtained depending on the search string provided for 
 * links.
 */

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class CrawlerUtil implements Serializable{


	private List<String> links = new LinkedList<String>();

	private Document htmlDocument;

	/**
	 * This Method performs crawling operation i.e. performing HTTP request, 
	 * validating the response and parsing to collect relevant hyperlinks.
	 * @param url
	 *    - current URL being visited.
	 * @return
	 *    - result of crawl operation.
	 */

	public void crawl(String url, String word) throws Exception
	{
		//try
		//{

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

		//}
		/*catch(Exception e)
		{
			System.out.println("Error in HTTP Request "+e);
			e.printStackTrace();

		}*/
	}

	/**
	 * This method retrieves e-mail hyperlinks embeddded in the current HTML page being parsed. 
	 * @param url - URL of current HTML page being parsed.
	 */

	public void retrieveLinks(String url) throws Exception
	{

		//try{
		Document htmlDocument = Jsoup.connect(url).get();
		this.htmlDocument = htmlDocument;
		Elements linksOnPage;
		linksOnPage = this.htmlDocument.select("a[href*=@]");

		for(Element link: linksOnPage)
		{

			String str = link.absUrl("href");

			this.links.add(str);
		}
		//}
		/*	catch(Exception e)
		{
			System.out.println("Error while retrieving links");
			e.printStackTrace();
		}*/
	}

	/**
	 * Getter Method for the list containing hyperlinks.
	 * @return 
	 *  - returns the List of hyperlinks.
	 */
	public List<String> getLinks()
	{
		return this.links;
	}


}
