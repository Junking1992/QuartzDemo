package com.demo.QuartzDemo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.csvreader.CsvWriter;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class Crawler4MO extends WebCrawler {

	private String webPath = "https://avmo.pw/cn/";
	private String MovieSubject = "https://avmo.pw/cn/movie/";

	private final File csv;
	private CsvWriter cw;

	public Crawler4MO() throws IOException {
		csv = ControllerMain.getFile();
	}

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		if (href.startsWith(webPath)) {
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
		System.out.println(ControllerMain.getCont() + "-->" + id + "-->" + name + "-->" + url);
		if (url.startsWith(MovieSubject)) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String html = htmlParseData.getHtml();
			Document doc = Jsoup.parse(html);

			try {
				cw = new CsvWriter(new FileOutputStream(csv, true), ',', Charset.forName("GBK"));
				
				String title = doc.select("a.bigImage").first().attr("title");
				cw.write(title);
				Element msgDiv = doc.select("div.col-md-3.info").first();
				Elements ps = msgDiv.select("p");
				for (int i = 0; i < ps.size(); i++) {
					String[] arr = ps.get(i).text().split(":");
					if (arr.length > 1) {
						cw.write(arr[1]);
					} else {
						cw.write(ps.get(i + 1).text());
						i++;
					}
				}
				cw.endRecord();
				cw.flush();
				cw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
