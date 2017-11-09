package com.ag777.converter.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import com.ag777.util.file.FileUtils;
import com.ag777.util.file.IniHelper;
import com.ag777.util.file.PathUtils;
import com.ag777.util.lang.StringUtils;

public class ConfigUtils {
	
	private static String configPath = PathUtils.srcPath(ConfigUtils.class)+"converter_config.ini";
	private static ConfigUtils mInstance = new ConfigUtils("config.ini");
	
	
	private IniHelper iu;
	
	private ConfigUtils(String filePath) {
		try {
			System.out.println(configPath);
			if(!FileUtils.fileExists(configPath)) {
				InputStream in = PathUtils.getAsStream(filePath, ConfigUtils.class);
				com.ag777.util.lang.IOUtils.write(in, new FileOutputStream(new File(configPath)), 1024);
			}	
			List<String> lines = FileUtils.readLines(configPath);
			iu = new IniHelper(lines);
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
	
	public static ConfigUtils getInstance() {
		return mInstance;
	}
	
	/**
	 * [数据库转pojo模块]模板选择位置
	 * @return
	 */
	public Optional<String> beanPathTemplateIn() {
		return getResult("bean", "path.template.in");
	}
	
	public boolean beanPathTemplateIn(String pathTemplateIn) {
		return update("bean", "path.template.in", pathTemplateIn);
	}

	/**
	 * [数据库转pojo模块]文件导出
	 * @return
	 */
	public Optional<String> beanPathFileOut() {
		return getResult("bean", "path.file.out");
	}
	
	public boolean beanPathFileOut(String pathFileOut) {
		return update("bean", "path.file.out", pathFileOut);
	}
	
	/**
	 * [数据库转pojo模块]ip
	 * @return
	 */
	public Optional<String> beanIp() {
		return getResult("bean", "ip");
	}
	
	public boolean beanIp(String ip) {
		return update("bean", "ip", ip);
	}
	
	/**
	 * [数据库转pojo模块]端口号
	 * @return
	 */
	public Optional<String> beanPort() {
		return getResult("bean", "port");
	}
	
	public boolean beanPort(String port) {
		return update("bean", "port", port);
	}
	
	/**
	 * [数据库转pojo模块]数据库名
	 * @return
	 */
	public Optional<String> beanDbName() {
		return getResult("bean", "dbName");
	}
	
	public boolean beanDbName(String dbName) {
		return update("bean", "dbName", dbName);
	}
	
	/**
	 * [数据库转pojo模块]账号
	 * @return
	 */
	public Optional<String> beanUser() {
		return getResult("bean", "user");
	}
	
	public boolean beanUser(String userName) {
		return update("bean", "user", userName);
	}
	
	/**
	 * [数据库转pojo模块]账号对应密码
	 * @return
	 */
	public Optional<String> beanPwd() {
		return getResult("bean", "pwd");
	}
	
	public boolean beanPwd(String password) {
		return update("bean", "pwd", password);
	}
	
	/**
	 * [数据库转pojo模块]选择的模板
	 * @return
	 */
	public Optional<String> beanTemplate() {
		return getResult("bean", "template");
	}
	
	public boolean beanTemplate(String template) {
		return update("bean", "template", template);
	}
	
	
	private Optional<String> getResult(String sectionKey, String valueKey) {
		Optional<String> result = iu.getValue(sectionKey, valueKey);
		if(!result.isPresent() || StringUtils.isBlank(result.get())) {
			return Optional.empty();
		}
		return Optional.of(result.get());
	}
	
	private boolean update(String sectionKey, String valueKey, Object value) {
		try {
			iu.update(sectionKey, valueKey, value).save(configPath);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
}
