package service;

import java.util.ArrayList;

import model.Crawler;

public class CrawlerService {
	
	public static String run(String url)
	{
		Crawler crawler = new Crawler(url);
		ArrayList<String> imageURL = crawler.fetchImageURL();
		return crawler.fetchAndStoreImage(imageURL);
	}

}
