package com.ag777.converter.presenter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.ag777.converter.base.BasePresenter;
import com.ag777.converter.utils.ConfigUtils;
import com.ag777.converter.utils.lang.IOUtils;
import com.ag777.converter.view.interf.JfinalDbBuilderView;
import com.ag777.util.db.DbHelper;
import com.ag777.util.db.model.ColumnPojo;
import com.ag777.util.lang.StringUtils;
import com.ag777.util.lang.collection.ListUtils;
import com.ag777.util.lang.reflection.ReflectionUtils;


public class JFinalDBPresenter extends BasePresenter<JfinalDbBuilderView> {

	private DbHelper helper;
	
	public JFinalDBPresenter() {
		init();
	}
	
	@Override
	public void attachView(JfinalDbBuilderView view) {
		super.attachView(view);
	}
	
	@Override
	public void detachView() {
		super.detachView();
		helper.dispose();	//关闭数据库连接
	}
	
	private void init() {
		helper = null;
	}
	
	public boolean init(String ip, String port, String dbName,
			String userName, String pwd) {
		try {
			DbHelper helper = DbHelper.connectDB(ip, StringUtils.toInteger(port), dbName, userName, pwd);
			if(helper != null) {
				init();
				this.helper = helper;
				//保存数据库相关信息
				ConfigUtils config = ConfigUtils.getInstance();
				config.beanIp(ip);
				config.beanPort(port);
				config.beanDbName(dbName);
				config.beanUser(userName);
				config.beanPwd(pwd);
				
				return true;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 获取所有表名,先执行init(...)方法来连接数据库
	 */
	public List<String> getTbaleList() {
		return helper.tableList();
	}
	
	/**
	 * 通过相关信息生成代码
	 * @param tableName
	 * @param className
	 */
	public void build(String tableName, String className, File modelFile) {
		try {
			build(tableName, className, new FileInputStream(modelFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			getView().showResult("读取文件异常!");
			return;
		}
	}
	
	public void build(String tableName, String className, InputStream inpuStream) {
		try {
			List<String> lines = IOUtils.readLines(inpuStream, "UTF-8");
			
			String whole = replaceWhole(tableName, className, lines);
			String result = replaceItem(tableName, whole);
			
			getView().showResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			getView().showResult("读取文件异常!");
			return;
		}
	}
	
	/**
	 * 替换#{xxx}
	 * @param tableName
	 * @param className
	 * @param modelLines
	 * @return
	 */
	private String replaceWhole(String tableName, String className, List<String> modelLines) {
		StringBuilder sb = new StringBuilder();
		
		Pattern p = Pattern.compile("#\\{(.+?)\\}");
		String keys = null;
		
		for (String line : modelLines) {
			int begin = 0;
			Matcher m = p.matcher(line);
			while(m.find()) {
				sb.append(line.substring(begin,m.start()));
				
				String temp = m.group(1);	//tableName
				if("tableName".equals(temp)) {
					sb.append(tableName);
				} else if("className".equals(temp)) {
					sb.append(className);
				} else if("keys".equals(temp)) {
					if(keys == null) {
						keys = getKeys(tableName);	//主键(多个主键逗号分隔)
					}
					sb.append(keys);
				} else {
					sb.append(m.group());
				}
				
				begin = m.end();
			}
			
			sb.append(line.substring(begin))
				.append(System.getProperty("line.separator"));	//加入换行符
		}
		return sb.toString();
	}
	
	/**
	 * 替换#{item.xxx}
	 * @param tableName
	 * @param model
	 * @return
	 */
	private String replaceItem(String tableName, String model) {
		StringBuilder sb = new StringBuilder();
		List<ColumnPojo> cols = helper.columnList(tableName);
		
		Pattern p_item = Pattern.compile("#\\{item\\.(.+?)\\}");
		
		Matcher m = Pattern.compile("<foreach>([\\s\\S]+?)</foreach>").matcher(model);
		
		int begin = 0;
		while(m.find()) {	//需要循环的主体
			
			sb.append(model.substring(begin,m.start()));
			String item = m.group(1);
			
			for (ColumnPojo col : cols) {
				begin = 0;
				
				Matcher m1 = p_item.matcher(item);
				while(m1.find()) {	//需要替换的内容
					
					sb.append(item.substring(begin,m1.start()));
					
					String temp = m1.group(1);	//class
//					System.out.println("找到:"+temp);
					if("class".equals(temp)) {
						Class<?> clazz = DbHelper.toPojoType(col.getSqlType(), col.getSize());
						if(ReflectionUtils.inPackage(clazz, "java.lang")) {
							sb.append(clazz.getSimpleName());
						} else {
							sb.append(clazz.getName());
						}
						
					} else if("name".equals(temp)) {
						sb.append(col.getName());
					} else if("remark".equals(temp)) {
						sb.append(col.getRemarks());
					} else if("nameCamel".equals(temp)) {
						sb.append(StringUtils.underline2Camel(col.getName(), true));
					} else if("nameCamelBig".equals(temp)) {
						sb.append(StringUtils.underline2Camel(col.getName(), false));
					} else {
						sb.append(m1.group());
					}
					
					begin = m1.end();
					
				}
				sb.append(item.substring(begin));
				begin = m.end();
			}
		}
		sb.append(model.substring(begin));	//加入换行符
		return sb.toString();
	}
	
	private String getKeys(String tableName) {
		List<String> keyList = helper.primaryKeyList(tableName);
		return ListUtils.toString(keyList, ",");
	}
	
	
	
}
