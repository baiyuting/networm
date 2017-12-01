package edu.buaa.test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import jeasy.analysis.MMAnalyzer;
import edu.buaa.test.testGetNews.*;
import edu.buaa.test.testJdbc.*;
import edu.buaa.test.testLoaddb.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Test {

	//private static String SAVE_PATH = "F:\\double_eleven_data\\";
	
	//static String url = "http://tech.sina.com.cn/z/doubleeleven/";
	
	private static String SAVE_JSON_PATH = "F:\\tech_sina_json\\";	
	static String url = "http://tech.sina.com.cn/";
	
	
	public static void main(String[] args) throws Exception {
		
		testGetNews.getUrlData(url);
		
		testLoaddb.load();
		
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        //List<Map<String, String>> resultList = null;
        Map<String,List<String>> resultJson = null;
        
        String fileName = System.currentTimeMillis() + ".json";
		PrintWriter writer = new PrintWriter(new File(SAVE_JSON_PATH + fileName));
        
        try {
            connection = testJdbc.getConn();
            
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select hotWord,counts from tech_sina order by counts desc limit 0,20");
            
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            String[] columnNames = new String[columnCount + 1];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i] = resultSetMetaData.getColumnName(i);
                //System.out.println("columnName is :"+ i +"  "+ columnNames[i] + "\n");
            }

            resultJson = new HashMap<String,List<String>>();
            List<String> result1 = new ArrayList<String>();
            List<String> result2 = new ArrayList<String>();
            resultSet.beforeFirst();
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                	if (i==1){
                		result1.add("\""+resultSet.getString(i)+"\"");
                	}
                	else{
                		result2.add(resultSet.getString(i));
                	}
                		
                	//System.out.println(result1 + "\n");
                	//System.out.println(result2 + "\n");
                    
                    
					
                }
                
            }
            resultJson.put(columnNames[1], result1);
            resultJson.put(columnNames[2], result2);
            
            writer.println("{\n\"" + columnNames[1] + "\":\n"+result1+",\n");
            writer.println("\"" + columnNames[2] + "\":\n"+result2+"\n}");
            writer.close();
        } catch(Throwable t) {
            // TODO 处理异常
            t.printStackTrace();
        } finally {
        	testJdbc.closeResource(connection, statement, resultSet);
        }
	  
   }
}
