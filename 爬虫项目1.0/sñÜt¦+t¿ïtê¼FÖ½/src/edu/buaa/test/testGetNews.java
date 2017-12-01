package edu.buaa.test;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class testGetNews {

		// 所有等待爬取数据的集合
		private static List<String> allWaitUrls = new ArrayList<String>();
		
		private static Object obj = new Object();
		// url不重复的Set集合
		private static Set<String> allOverUrls = new HashSet<String>();
		// 记录深度
		private static Map<String, Integer> allUrlDepth = new HashMap<String, Integer>();
		// 允许的最大深度
		private static int MAX_DEPTH = 2;

		private static String SAVE_PATH = "F:\\tech_sina_data\\";
		
		public static void main(String[] args) {
			
			allUrlDepth.put("http://tech.sina.com.cn/", 1);
			allWaitUrls.add("http://tech.sina.com.cn/");
			for (int i = 0; i < 5; i++) {
				MyThread t = new testGetNews().new MyThread();
				t.start();
			}
			// getUrlData("http://product.dangdang.com/23407125.html");
		}

		// 提取数据的操作方法
		public static void getUrlData(String urlStr) {
			try {
				Document doc = Jsoup.connect(urlStr).get();
				
				String title = doc.title();
				// 描述
				Elements allHref = doc.getElementsByTag("a");
				for (Element aEl : allHref) {
					// 取得里面的href属性
					String href = aEl.attr("href");
					if (href.startsWith("http://tech.sina.com.cn")&& href.matches(".*\\d{4}-\\d{2}-\\d{2}.*\\d+\\.shtml")) {
						String itemText = aEl.text();
						String fileName = System.currentTimeMillis() + ".txt";
						PrintWriter writer = new PrintWriter(new File(SAVE_PATH + fileName));
						writer.println(itemText);
						writer.close();
				    }
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		class MyThread extends Thread {
			@Override
			public void run() {
				System.out.println("子线程初始化完成，当前线程为：" + Thread.currentThread().getName());
				// 不能让线程自己结束，因此必须编写一个死循环。
				while (true) {
					// 判断当前是否有活要干，如果有，则进行处理，如果没有，则休息。
					// 编写一个同步操作，同时只能有一个线程来判断allWaitUrls中的连接数，以及提取链接
					String readyUrl = null;
					// 通过一个同步块来实现同步的操作
					synchronized (allWaitUrls) {
						if (allWaitUrls.size() > 0) {
							readyUrl = allWaitUrls.get(0);
							allWaitUrls.remove(0);
						}
					}
					if (readyUrl != null) {
						// 有任务
						// 调用读取数据的方法
						getUrlData(readyUrl);
					} else {
						// 没有任务，需要等待
						try {
							synchronized (obj) {
								obj.wait();
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

}
