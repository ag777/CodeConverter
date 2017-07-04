package com.ag777.converter.presenter;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ag777.converter.base.BasePresenter;
import com.ag777.converter.module.DBModuleFactory;
import com.ag777.converter.module.DBModuleImpl;
import com.ag777.converter.utils.StringUtils;
import com.ag777.converter.view.interf.JfinalDbBuilderView;


public class JFinalDBPresenter extends BasePresenter<JfinalDbBuilderView> {

	private DBModuleFactory mDb;
	private Connection conn;
	
	public JFinalDBPresenter() {
		mDb = DBModuleImpl.getInstance();
		init();
	}
	
	@Override
	public void attachView(JfinalDbBuilderView view) {
		super.attachView(view);
	}
	
	@Override
	public void detachView() {
		super.detachView();
		mDb.closeDb(conn);	//关闭数据库连接
	}
	
	private void init() {
		conn = null;
	}
	
	public boolean init(String ip, String port, String dbName,
			String userName, String pwd) {
		Connection conn = mDb.connect(ip, port, dbName, userName, pwd);
		if(conn != null) {
			init();
			this.conn = conn;
			return true;
		}
		return false;
	}
	
	/**
	 * 获取所有表名,先执行init(...)方法来连接数据库
	 */
	public List<String> getTbaleList() {
		
		try {
			DatabaseMetaData dbmd = conn.getMetaData();
			List<String> tableNames = new ArrayList<String>();
	        ResultSet rs = null;
	        String[] typeList = new String[] { "TABLE" };
	        rs = dbmd.getTables(null, "%", "%",  typeList);
	        for (boolean more = rs.next(); more; more = rs.next()) {
	            String s = rs.getString("TABLE_NAME");
	            String type = rs.getString("TABLE_TYPE");
	            if (type.equalsIgnoreCase("table") && s.indexOf("$") == -1)
	            	tableNames.add(s);
	        }
	        return tableNames;
		} catch(Exception ex) {
			mDb.closeDb(conn);
		}
		return new ArrayList<String>();
	}
	
	/**
	 * 通过相关信息生成代码
	 * @param tableName
	 * @param className
	 */
	public void build(String tableName, String className) {
		List<Col> cols = getColumns(tableName);
		String result = getJFinalBeanString(cols, className);
		getView().showResult(result);
	}
	
	/**
	 * 根据字段及类型生成代码
	 * @param list
	 * @param className
	 * @return
	 */
	private static String getJFinalBeanString(List<Col> list, String className) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("public class ")
			.append(className)
			.append(" extends com.jfinal.plugin.activerecord.Model<")
			.append(className)
			.append("> {\n")
			.append('\n');
		
		for (Col col : list) {
			String upperName = StringUtils.upperCase(StringUtils.underline2Camel(col.getName(),true));
			
			sb.append('\t')
				.append("public ")
				.append(className)
				.append(" set")
				.append(upperName)
				.append("(")
				.append(col.getType())
				.append(" ")
				.append(col.getName())
				.append(") {\n")
				.append('\t')
				.append("\tset(\"")//set("open_id", open_id);
				.append(col.getName())
				.append("\", ")
				.append(col.getName())
				.append(");\n")
				.append('\t')
				.append("\treturn this;\n")//return this;
				.append('\t')
				.append("}\n");
			sb.append('\t')
				.append("public ")
				.append(col.getType())
				.append(" get")
				.append(upperName)
				.append("() {\n")
				.append('\t')
				.append("\treturn get(\"")
				.append(col.getName())
				.append("\");\n")
				.append('\t')
				.append("}\n\n");
		}
		
		sb.append("\n}");
		return sb.toString();
	}
	
	/**
	 * 通过表名获取所有字段
	 * @param tableName
	 * @return
	 */
	private List<Col> getColumns(String tableName) {
		List<Col> cols = new ArrayList<JFinalDBPresenter.Col>();
		
		try {
			DatabaseMetaData dbmd = conn.getMetaData();
			ResultSet columnSet = dbmd.getColumns(null, "%", tableName, "%");
			while (columnSet.next()) {
				String columnName = columnSet.getString("COLUMN_NAME");
			    String columnComment = columnSet.getString("REMARKS");
			    Integer sqlType = columnSet.getInt("DATA_TYPE");
//			    enResult(columnSet);
			    Long length = columnSet.getLong("COLUMN_SIZE");
			    
			    Col col = new Col(columnName, columnComment);
//			    System.out.println(sqlType);
			    switch(sqlType) {
				    case java.sql.Types.BIGINT:
				    	col.setType("Long");
				    	break;
				    case java.sql.Types.INTEGER:
				    	col.setType("INTEGER");
				    	break;
				    case java.sql.Types.VARCHAR:
				    	col.setType("String");
				    	break;
				    case java.sql.Types.TIMESTAMP:
				    	col.setType("java.util.Date");	//java.util.Timestamp
				    	break;
				    default:
				    	col.setType(sqlType+"");
				    	break;
			    }
			    cols.add(col);
			}
//		   System.out.println(JSON.toJSON(cols));
		} catch(Exception ex) {
			ex.printStackTrace();
			mDb.closeDb(conn);
		}
		return cols;
	}
	
	static class Col {
		public String name;
		public String remark;
		public String type;
		public Col(String name, String remark) {
			this.name = name;
			this.remark = remark;String a = "{\"data\":{\"siteUrl\":\"www.baidu.com\",\"warningDate\":\"2017-02-03 11:22:33\",\"warningType\":\"预警信息\"},\"openIdList\":[\"oMDXpwLQpNIBE0nZ5Y0a3m9d-JJY\"],\"typeId\":1}";
			
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		
	}
}
