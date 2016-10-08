package com.demo.QuartzDemo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import com.csvreader.CsvWriter;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {

	public static int count = 0;

	public static File csv;

	public synchronized static int getCont() {
		return ++count;
	}
	
	public synchronized static File getFile() throws IOException{
		if(csv == null){
			csv = new File("E:/豆瓣电影.csv");
			CsvWriter cw = new CsvWriter(new FileOutputStream(csv, true), ',', Charset.forName("GBK"));
			cw.write("片名");
			cw.write("评分");
			cw.endRecord();
			cw.close();
		}
		return csv;
	}

	public static void main(String[] args) throws Exception {
		String crawlStorageFolder = "E:/B";
		int numberOfCrawlers = 3;

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setMaxDepthOfCrawling(6);

		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

		/*
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 */
		controller.addSeed("https://m.douban.com/movie/subject/1292052/?from=rec");

		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.start(Crawler4jDemo.class, numberOfCrawlers);
	}
}
