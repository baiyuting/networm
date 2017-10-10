package edu.buaa.crawler;

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

public class DangdangThreadCrawler {

	// 所有等待爬取数据的集合
	private static List<String> allWaitUrls = new ArrayList<String>();
	// 准备一个Object类型的对象，相当于一个闹钟，通过这个对象来实现睡眠和唤醒线程。
	private static Object obj = new Object();
	// url不重复的Set集合
	private static Set<String> allOverUrls = new HashSet<String>();
	// 记录深度
	private static Map<String, Integer> allUrlDepth = new HashMap<String, Integer>();
	// 允许的最大深度
	private static int MAX_DEPTH = 3;

	private static String SAVE_PATH = "/Users/kkb/Documents/dangdang_data/";

	public static void main(String[] args) {
		allUrlDepth.put("http://category.dangdang.com/cp01.05.16.00.00.00.html", 1);
		allWaitUrls.add("http://category.dangdang.com/cp01.05.16.00.00.00.html");
		for (int i = 0; i < 10; i++) {
			MyThread t = new DangdangThreadCrawler().new MyThread();
			t.start();
		}
		// getUrlData("http://product.dangdang.com/23407125.html");
	}

	// 提取数据的操作方法
	public static void getUrlData(String urlStr) {
		try {
			// 判断重复问题
			if (!allOverUrls.contains(urlStr)) {
				// 判断深度是否满足要求
				int depth = allUrlDepth.get(urlStr);
				if (depth <= MAX_DEPTH) {
					// JSoup是通过标准的DOM解析来提取数据的
					// 注意，导包时，需要导入带 jsoup 包的支持类
					Document doc = Jsoup.connect(urlStr).get();
					// System.out.println(doc.html());

					// 提取有效的数据
					// 判断url是否是需要提取数据的url
					if (urlStr.matches(".*product\\.dangdang\\.com.*\\d+\\.html")) {
						// 满足要求，再进行数据的提取
						// 根据要求提取标题和价格数据
						// 标题要找的是h1
						Element titleEl = doc.getElementsByTag("article").get(0);
						// 提取里面的文字内容
						String title = titleEl.text().trim();
						// 提取价格
						Element priceEl = doc.getElementById("main_price");
						String price = priceEl.text().trim();
						// 保存文件
						String fileName = System.currentTimeMillis() + ".txt";
						PrintWriter writer = new PrintWriter(new File(SAVE_PATH + fileName));
						writer.println(title);
						writer.println(price);
						writer.print(urlStr);
						writer.close();
					}

					// 提取所有的页面链接，加入到集合中
					Elements allHref = doc.getElementsByTag("a");
					for (Element aEl : allHref) {
						// 取得里面的href属性
						String href = aEl.attr("href");
						if (href.startsWith("http:")) {
							// 有用的链接就需要放入到集合中
							synchronized (allWaitUrls) {
								allWaitUrls.add(href);
								if (!allUrlDepth.containsKey(href)) {
									allUrlDepth.put(href, depth + 1);
								}
							}
							// 需要唤醒之前睡眠的线程，继续干活
							synchronized (obj) {
								obj.notify();
							}
						}
					}
					System.out.println("当前线程：" + Thread.currentThread().getName() + " , 已经处理完成链接：" + urlStr);
					// 记录处理好的链接
					allOverUrls.add(urlStr);
					// 判断是否有必要结束爬取操作
					if (allWaitUrls.size() == 0) {
						System.exit(0);
					}
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
