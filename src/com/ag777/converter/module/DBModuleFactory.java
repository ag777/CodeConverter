package com.ag777.converter.module;

import java.sql.Connection;

public interface DBModuleFactory {

	public Connection connect(String ip, String port, String dbName, String userName, String pwd);
	
	public void closeDb(Connection conn);
}
