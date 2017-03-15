package com.demo.QuartzDemo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest
{
	@Test
	public void demo_1() throws Exception{
		Demo_1 demo = new Demo_1();
		demo.run();
	}
	
	@Test
	public void TestUrl(){
		getHtmlByUrl("https://avmo.pw/cn");
	}
	
	public static String getHtmlByUrl(String http){
		StringBuffer sb = new StringBuffer();
		try {
            URL url = new URL(http);
            InputStream in =url.openStream();
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader bufr = new BufferedReader(isr);
            String buffer;
            while ((buffer = bufr.readLine()) != null) {
            	sb.append(buffer + "\r\n");
            }
            bufr.close();
            isr.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return sb.toString();
	}
	
	public static void main(String[] args) {
		String html = getHtmlByUrl("https://avmo.pw/cn/movie/5ws8");
		Map<String, String> data = new HashMap<String, String>();
		List<String> tagList = new ArrayList<String>();
		List<Map<String, String>> starList = new ArrayList<Map<String, String>>();
		Document doc = Jsoup.parse(html);
		Element msgDiv = doc.select("div.col-md-3.info").first();
		Elements ps = msgDiv.select("p");
		
		String title = doc.select("a.bigImage").first().attr("title");
		data.put("title", title);
		for (int i = 0; i < ps.size(); i++) {
			String p = ps.get(i).text();
			if(p.indexOf(":") > -1 && !"类别:".equals(p)){//有:无类别的
				String[] arr = p.split(":");
				if(arr.length > 1){//两个String
					data.put(arr[0].trim(), arr[1].trim());
				}else{//一个String
					if(i < (ps.size()-1)){//有下文
						String p2 = ps.get(i+1).text();
						if(p2.indexOf(":") > -1){//下文有:
							data.put(arr[0].trim(), "");
						}else{//无:
							data.put(arr[0].trim(), p2);
						}
						i++;
					}else{//无下文
						data.put(arr[0].trim(), "");
					}
				}
			}else if(p.indexOf(":") > -1 && "类别:".equals(p)){//有:有类别的
				if(i < (ps.size()-1)){//有下文
					Element e = ps.get(i+1);
					Elements spans = e.select("span");
					if(spans.size() > 0){
						for(Element span : spans){
							tagList.add(span.text().trim());
						}
					}
					i++;
				}
			}else{
				data.put("其他" + i, p);
			}
		}
		
		Elements stars = doc.select("a.avatar-box");
		data.put("STARS", stars.size()+"");
		Map<String, String> starMap = null;
		for(Element star : stars){
			starMap = new HashMap<String, String>();
			starMap.put("URL", star.attr("href"));
			starMap.put("NAME", star.text());
			starList.add(starMap);
		}
		
		System.out.println(data.toString());
		System.out.println(tagList.toString());
		System.out.println(starList.toString());
//		String html = "<div class='col-md-3 info'><p><span class='header'>识别码:</span> <span style='color:#CC0000;'>AP-409</span></p><p><span class='header'>发行时间:</span> 2017-03-17</p><p><span class='header'>长度:</span> 125分钟</p><p><span class='header'>导演:</span> <a href='https://avmo.pw/cn/director/7e'>クニオカ</a></p><p class='header'>制作商: </p><p><a href='https://avmo.pw/cn/studio/yg'>アパッチ</a></p><p class='header'>发行商: </p><p><a href='https://avmo.pw/cn/label/68b'>アパッチ（HHHグループ）</a></p><p class='header'>系列:</p><p><a href='https://avmo.pw/cn/series/lit'>母娘同時○○痴漢</a></p><p class='header'>类别:</p><p><span class='genre'><a href='https://avmo.pw/cn/genre/g'>DMM独家</a></span><span class='genre'><a href='https://avmo.pw/cn/genre/n'>颜射</a></span><span class='genre'><a href='https://avmo.pw/cn/genre/u'>吞精</a></span><span class='genre'><a href='https://avmo.pw/cn/genre/19'>放尿</a></span><span class='genre'><a href='https://avmo.pw/cn/genre/3r'>粗暴</a></span></p></div>";
//        Document doc = Jsoup.parse(html);
//		Element msgDiv = doc.select("div.col-md-3.info").first();
//		Elements ps = msgDiv.select("p");
//		for (int i = 0; i < ps.size(); i++) {
//			String[] arr = ps.get(i).text().split(":");
//			if(arr.length > 1){
//				System.out.print(arr[0] + ">");
//				System.out.println(arr[1]);
//			}else{
//				System.out.print(arr[0] + ">");
//				System.out.println(ps.get(i+1).text());
//				i++;
//			}
//			System.out.println(ps.get(i).text().replaceAll("\r\n", ""));
//		}
	}
}
