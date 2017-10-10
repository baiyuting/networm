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

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;

public class TestSinaHTMLParser {

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

					// 然后，通过 HTMLParser来解析出有效的内容。
					// 需要建立一个处理的核心对象
					Parser parser = new Parser(conn);

					// 开始解析标题内容
					String title = null;
					String desc = null;

					// 建立一个解析标题的过滤规则
					TagNameFilter titleFilter = new TagNameFilter("title");
					// 根据这个条件，从网页中过滤
					Node titleNode = parser.parse(titleFilter).elementAt(0);
					// 提取里面的内容
					title = titleNode.toPlainTextString();

					// 解析描述内容
					// 先将原有条件清空
					parser.reset();
					AndFilter descFilter = new AndFilter(new TagNameFilter("meta"),
							new HasAttributeFilter("name", "description"));
					Node descNode = parser.parse(descFilter).elementAt(0);

					desc = descNode.toHtml();
					desc = desc.substring(desc.indexOf("content=") + 9);
					desc = desc.substring(0, desc.indexOf("\""));

					// 解析所有的超链接的内容
					parser.reset();
					TagNameFilter aFilter = new TagNameFilter("a");
					NodeList allANode = parser.parse(aFilter);
					// 循环取得每一个连接
					for (int i = 0; i < allANode.size(); i++) {
						Node aNode = allANode.elementAt(i);
						String href = aNode.toHtml();
						href = href.substring(href.indexOf("href=") + 6);
						href = href.substring(0, href.indexOf("\""));
						if (href.startsWith("http:")) {
							allWaitUrls.add(href);
							if (!allUrlDepth.containsKey(href)) {
								allUrlDepth.put(href, depth + 1);
							}
						}
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
