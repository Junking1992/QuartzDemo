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

public class ControllerMain {

	public static int count = 0;

	public static File csv;
	
	public final static String TITLE = "标题";
	public final static String CODE = "识别码";
	public final static String DATE = "发行时间";
	public final static String LENGTH = "长度";
	public final static String DIRECTOR = "导演";
	public final static String MAKER = "制作商";
	public final static String PUBLISHER = "发行商";
	public final static String SERIAL = "系列";
	public final static String TYPE = "类别";

	public synchronized static int getCont() {
		return ++count;
	}
	
	public synchronized static File getFile() throws IOException{
		if(csv == null){
			csv = new File("E:/mo.csv");
			CsvWriter cw = new CsvWriter(new FileOutputStream(csv, true), ',', Charset.forName("GBK"));
			cw.write("TITLE");
			cw.write("CODE");
			cw.write("DATE");
			cw.write("LENGTH");
			cw.write("DIRECTOR");
			cw.write("MAKER");
			cw.write("PUBLISHER");
			cw.write("SERIAL");
			cw.write("TYPE");
			cw.endRecord();
			cw.close();
		}
		return csv;
	}

	public static void main(String[] args) throws Exception {
		String crawlStorageFolder = "E:/B";
		int numberOfCrawlers = 5;

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setMaxDepthOfCrawling(-1);

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
		controller.addSeed("https://avmo.pw/cn/page/1");

		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.start(Crawler4MO.class, numberOfCrawlers);
	}
}
