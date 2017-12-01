package edu.buaa.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jeasy.analysis.MMAnalyzer;

public class testLoaddb {
	
	private static Map<String, Integer> keywordCounts = new HashMap<String, Integer>();

	public static void main(String[] args) throws Exception {
		load();
	}
	
	public static void load() throws Exception{
		// 找到保存所有数据的文件夹，将这个文件夹下的所有文本文件读取出来，分别进行分词操作
		File dir = new File("F:\\tech_sina_data\\");
		// 读取下面的所有文本文件
		File[] allFiles = dir.listFiles();

		MMAnalyzer mm = new MMAnalyzer();

		for (File f : allFiles) {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			// 只读取第一行和第二行的内容，进行内容的分析
			String content = reader.readLine();
			content += "|" + reader.readLine();
			// 进行分词操作
			String result = mm.segment(content, "|");
			String[] allKeywords = result.split("\\|");
			for (String keyword : allKeywords) {
				// 先判断这个词在Map中是否已经保存过了
				if (keywordCounts.containsKey(keyword)) {
					// 如果之前保存过，在原有的出现次数基础上 ＋ 1
					keywordCounts.put(keyword, keywordCounts.get(keyword) + 1);
				} else {
					// 这是第一次出现
					keywordCounts.put(keyword, 1);
				}
			}
			reader.close();
		}
		
		// 保存数据库
		// 下面就需要掌握通过Java进行数据库操作的方法。
		// 固定用到3个类，首先需要建立与数据库的链接
		// 加载数据库驱动
		Class.forName("org.gjt.mm.mysql.Driver");
		// 根据数据库的位置和用户名密码，来链接数据库
		// 注意 Connection 的包 使用 java.sql.包
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/newdb?useUnicode=true&characterEncoding=UTF-8", "root", "root123");
		
		// 编写SQL语句，完成数据库的相关操作
		String sql = "INSERT INTO tech_sina (hotWord,counts) VALUES (?,?)" ;
		
		// 循环所有的数据，准备进行添加
		Set<String> allKeys = keywordCounts.keySet();
		for (String key : allKeys) {
			int value = keywordCounts.get(key);
			
			// 建立数据库操作对象
			PreparedStatement pst = conn.prepareStatement(sql);
			// 传入?参数
			pst.setString(1, key);
			pst.setInt(2, value);
			// 执行SQL命令，添加，修改和删除都使用 executeUpdate操作
			pst.executeUpdate();
			pst.close();
		}
		
		// 关闭链接等
		conn.close();
	}

}
