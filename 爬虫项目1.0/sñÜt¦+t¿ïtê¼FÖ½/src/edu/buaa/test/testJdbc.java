package edu.buaa.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class testJdbc {
    
	
    public static void main(String[] args) throws Exception {
    	//testJdbc.query("select * from my_dd_book");
    	String book_nm = "Web development";
    	int count = 1001;
    	String sql = "insert into my_dd_book(book_nm, comment_count) values ('" + book_nm + "'," + count + ")";
    	testJdbc.execute(sql);
        //testJdbc.execute("insert into my_dd_book(book_nm, comment_count) values ('excel 2016', 300)");
    }
    
    public static String HOST = "localhost";
    public static String PORT = "3306";
    public static String DATABASE_NAME = "newdb";
    public static String USER_NAME = "root";
    public static String PASSWORD = "root123";
    
    
    /**
     * 获取数据库连接
     * @return 数据库连接
     */
    public static Connection getConn() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        System.out.println("成功加载驱动");
        
        String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE_NAME + "?user=" + USER_NAME + "&password=" + PASSWORD + "&useUnicode=true&characterEncoding=UTF8";
        Connection connection = DriverManager.getConnection(url);
        System.out.println("成功获取连接");
        return connection;
    }
    
    /**
     * 关闭资源
     */
    public static void closeResource(Connection conn, Statement st, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO 处理异常
                e.printStackTrace();
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                // TODO 处理异常
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO 处理异常
                e.printStackTrace();
            }
        }
        System.out.println("成功关闭资源");
    }

    /**
     * 查询SQL
     * @param sql 查询语句
     * @return 数据集合
     * @throws SQLException
     */
    public static List<Map<String, String>> query(String sql) throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<Map<String, String>> resultList = null;
        
        try {
            connection = testJdbc.getConn();
            
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            String[] columnNames = new String[columnCount + 1];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i] = resultSetMetaData.getColumnName(i);
            }

            resultList = new ArrayList<Map<String, String>>();
            Map<String, String> resultMap = new HashMap<String, String>();
            resultSet.beforeFirst();
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    resultMap.put(columnNames[i], resultSet.getString(i));
                }
                resultList.add(resultMap);
            }
            System.out.println("成功查询数据库，查得数据：" + resultList);
        } catch(Throwable t) {
            // TODO 处理异常
            t.printStackTrace();
        } finally {
        	testJdbc.closeResource(connection, statement, resultSet);
        }
        
        return resultList;
    }
    
    /**
     * 执行SQL
     * @param sql 执行的SQL
     * @return 操作条数
     */
    public static int execute(String sql) throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        int num = 0;
        
        try {
            connection = testJdbc.getConn();
            connection.setAutoCommit(false);
            
            statement = connection.createStatement();
            num = statement.executeUpdate(sql);
            System.out.println("成功操作数据库，影响条数：" + num);
            
            // 模拟异常，用于测试事务
            /*
            if (1 == 1) {
                throw new RuntimeException();
            }
            */
            
            connection.commit();
        } catch(Exception e) {
            // 处理异常：回滚事务后抛出异常
            e.printStackTrace();
            connection.rollback();
            System.out.println("事务回滚");
            throw e;
        } finally {
        	testJdbc.closeResource(connection, statement, resultSet);
        }
        
        return num;
    }
    
}

