package controller;

import service.CrawlerService;

public class ConsoleController {
	
	public static void main(String[] args)
	{
		String url = args[0];
		String filePath = CrawlerService.run(url);
	}

}
