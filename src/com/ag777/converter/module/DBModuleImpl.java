package com.ag777.converter.module;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBModuleImpl implements DBModuleFactory{

	private static DBModuleImpl mInstance = new DBModuleImpl();
	
	public static DBModuleImpl getInstance(){
		
		return mInstance;
	}
	
	@Override
	public Connection connect(String ip, String port, String dbName,
			String userName, String pwd) {
		String driver = "com.mysql.jdbc.Driver";

		// URL指向要访问的数据库名scutcs
		String url = getDbString(ip, port, dbName);

		// MySQL配置时的用户名
		String user = userName;

		// Java连接MySQL配置时的密码
		String password = pwd;

		// 加载驱动程序
		try {
			Class.forName(driver);
			// 连接数据库
			return DriverManager.getConnection(url, user, password);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void closeDb(Connection conn) {
		 if (conn != null){
             try {
				conn.close();
				conn=null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
	}
	
	public static String getDbString(String ip, String port, String dbName){
		return "jdbc:mysql://"+ip+":"+port+"/"+dbName+"?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
	}

}
