package com.demo.QuartzDemo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.csvreader.CsvWriter;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class Crawler4jDemo extends WebCrawler {

	private String MovieSubject = "https://m.douban.com/movie/subject";
	private String endStr = "?from=rec";

	private final File csv;
	private CsvWriter cw;

	public Crawler4jDemo() throws IOException {
		csv = new File("E:/豆瓣电影.csv");
		cw = new CsvWriter(new FileOutputStream(csv, true), ',', Charset.forName("GBK"));
		cw.write("片名");
		cw.write("评分");
		cw.endRecord();
		cw.close();
	}

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		if (href.startsWith(MovieSubject) && href.endsWith(endStr)) {
			return true;
		}
		return false;
	}

	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		Thread thread = Thread.currentThread();
		long id = thread.getId();
		String name = thread.getName();
		System.out.println(Controller.getCont() + "-->" + id + "-->" + name + "-->" + url);

		HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
		String html = htmlParseData.getHtml();
		Document doc = Jsoup.parse(html);
		Elements card = doc.select("div[class=card]");
		String title = card.select("h1").first().text();
		String score = card.select("section[class=subject-info]").first().select("div[class=left]").first().select("strong").text();

		try {
			cw = new CsvWriter(new FileOutputStream(csv, true), ',', Charset.forName("GBK"));
			cw.write(title);
			cw.write(score);
			cw.endRecord();
			cw.flush();
			cw.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

}
