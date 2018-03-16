package model;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.Constant;

public class Crawler {
	
	private String LINK;
	private String TITLE;
	
	public Crawler(String LINK)
	{
		this.LINK = LINK;
	}
	
	public ArrayList<String> fetchImageURL()
	{
		Document doc = null;
		try {
			doc = Jsoup.connect(this.LINK).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Elements imageContainer = doc.select("span.mdCMN09Image");
		
		ArrayList<String> imageURL = new ArrayList<String>();
		for (Element src:imageContainer)
		{
			String styleValue = src.attr("style");
			String imageurl = extractLink(styleValue);
			imageURL.add(imageurl);
		}
		
		Element titleContainer = doc.select("h3.mdCMN08Ttl").first();
		this.TITLE = titleContainer.text();
		return imageURL;
	}
	
	public void fetchAndStoreImage(ArrayList<String> imageURL)
	{
		for(int i=0;i<imageURL.size();i++)
		{
			try {
				URL url = new URL(imageURL.get(i));
				BufferedImage image = ImageIO.read(url);
				save(image, Integer.toString(i), "png");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private void save(BufferedImage image, String fileName, String ext)
	{
		String dirPath = Constant.ASSET_PATH + "/" + this.TITLE;
		File directory = new File(dirPath);
		// if the directory does not exist, create it
		if (!directory.exists()) {
		    System.out.println("creating directory: " + directory.getName());
		    boolean result = false;

		    try{
		        directory.mkdirs();
		        result = true;
		    } 
		    catch(SecurityException se){
		        //handle it
		    }        
		    if(result) {    
		        System.out.println("DIR created");  
		    }
		}
		
		File imageFile = new File(dirPath + "/" + fileName + "." + ext);
		try {
			ImageIO.write(image, ext, imageFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String extractLink(String raw)
	{
		String pattern1 = "background-image:url(";
		String pattern2 = ";compress=true);";
		Pattern p = Pattern.compile(Pattern.quote(pattern1)+"(.*?)"+Pattern.quote(pattern2));
		Matcher m = p.matcher(raw);
		String result = "";
		while (m.find())
		{
			result = m.group(1);
		}
		return result;
	}
	
	/*uncomment for testing purpose*/
	
//	public static void main(String[] args)
//	{
//		Crawler crawler = new Crawler("");
//		ArrayList<String> imageList = crawler.fetchImageURL();
//		crawler.fetchAndStoreImage(imageList);
//		System.out.println("done");
//	}

}
