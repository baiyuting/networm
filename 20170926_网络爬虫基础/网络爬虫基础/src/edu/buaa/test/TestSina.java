package edu.buaa.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestSina {

	// 准备一个队列，保存所有等待爬取的连接地址
	// List集合类来模拟队列的处理，添加的连接都放入到最后一个，所以提取连接都从第一个开始提取。
	private static List<String> allWaitUrls = new ArrayList<String>();

	// 使用Set集合保存所有已经处理过的连接地址，该集合类型不允许重复，所以适合完成这个工作
	private static Set<String> allOverUrls = new HashSet<String>();

	// 使用Map集合保存url地址对应的深度，key保存地址，value保存深度值
	private static Map<String, Integer> allUrlDepth = new HashMap<String, Integer>();

	// 最大允许的深度
	private static final int MAX_DEPTH = 2;

	// 准备一个判断URL是否符合我们匹配的条件
	private static String urlRegex = ".*sina.*\\d{4}-\\d{2}-\\d{2}.*\\d+\\.shtml";

	public static void main(String[] args) throws Exception {
		String seedUrl = "http://news.sina.com.cn/";
		// 处理深度值，进行保存
		allUrlDepth.put(seedUrl, 1);
		getUrlData(seedUrl);

	}

	public static void getUrlData(String urlStr) throws Exception {
		try {
			// 先判断连接是否被处理过
			if (!allOverUrls.contains(urlStr)) {
				// 取得当前连接地址的深度值
				int depth = allUrlDepth.get(urlStr);
				// 判断是否满足我们的要求
				if (depth <= MAX_DEPTH) {
					// 建立与网页的连接
					URL url = new URL(urlStr);
					URLConnection conn = url.openConnection();
					// 读取输入的数据内容
					InputStream is = conn.getInputStream();
					// 转换为缓冲字符流
					BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
					// 逐行读取数据
					String line = null;
					// 可变字符串类，类似StringBuffer
					StringBuilder builder = new StringBuilder();

					String desc = null;

					// 准备提取超链接的匹配规则 和 相关类
					String hrefRegex = "<a .*href=.*</a>";
					Pattern hrefP = Pattern.compile(hrefRegex);

					while ((line = reader.readLine()) != null) {
						builder.append(line);
						// 提取描述信息
						String descRegex = "<meta .*name=\"[dD][eE][sS][cC][rR][iI][pP][tT][iI][oO][nN]\".*>";
						Pattern descP = Pattern.compile(descRegex);
						Matcher descM = descP.matcher(line);

						if (descM.find()) {
							desc = descM.group();
							desc = desc.substring(desc.indexOf("content=") + 9);
							desc = desc.substring(0, desc.indexOf("\""));
							// System.out.println(desc);
						}
						// 匹配超链接的格式
						Matcher hrefM = hrefP.matcher(line);
						while (hrefM.find()) {
							String href = hrefM.group();
							href = href.substring(href.indexOf("href=") + 6);
							href = href.substring(0, href.indexOf("\"")).trim();
							// 判断连接地址是否以http开头，如果是，才表示为有效连接
							if (href.startsWith("http:")) {
								// 将连接加入到队列中
								allWaitUrls.add(href);
								// 除了把url地址加入到队列中以外，还需要将地址的深度也加入到Map集合中。
								// 如果之前没有记录过这个连接的深度，就将深度记录下来。
								if (!allUrlDepth.containsKey(href)) {
									allUrlDepth.put(href, depth + 1);
								}
							}
						}
					}

					reader.close();
					// 提取有效的数据
					String titleRegex = "<title>.*</title>";
					Pattern titleP = Pattern.compile(titleRegex);
					Matcher titleM = titleP.matcher(builder.toString());
					// 取得标题的文字内容
					String title = null;
					// 判断是否有满足条件的内容
					if (titleM.find()) {
						title = titleM.group();
						title = title.substring(7);
						title = title.substring(0, title.indexOf("</title>"));
						// System.out.println(title);
					}

					// 判断url是否为有必要保存的url
					if (urlStr.matches(urlRegex)) {
						// 这里为了能够简单的保存好数据，我们直接使用IO流来将内容保存到一个文件里
						String savePath = "/Users/kkb/Documents/sina_data/";
						// 随机生成一个文件名，防止重复
						// 可以使用时间戳或者UUID方式来生成
						String fileName = System.currentTimeMillis() + ".txt";
						// 使用一个JavaIO中的输出流来保存数据。
						PrintWriter writer = new PrintWriter(new File(savePath + fileName));
						// 完成打印保存的操作
						writer.println(title);
						writer.println(desc);
						writer.close();
						// 把当前连接加入到处理过的连接集合里
						allOverUrls.add(urlStr);
						System.out.println("已经处理完成：" + urlStr + ", 还有 " + allWaitUrls.size() + " 等待处理");
					}
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		// 准备处理下一个连接地址
		// 判断队列中是否有等待处理的连接
		if (allWaitUrls.size() > 0) {
			// 把第一个连接提取出来
			String newUrl = allWaitUrls.get(0);
			// 将该连接从队列中移除
			allWaitUrls.remove(0);
			// 进行新连接的处理
			getUrlData(newUrl);
		}

	}

}
