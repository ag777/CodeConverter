package com.ag777.converter.utils;

import java.io.File;
import java.io.InputStream;

public class ResourceUtils {

	private ResourceUtils() {}
	
	public static InputStream getAsStream(String filePath) {
		return ResourceUtils.class.getClassLoader().getResourceAsStream(filePath);
	}
	
	public static String srcPath() {
		String path = System.getProperty("java.class.path");
		int firstIndex = path.lastIndexOf(System.getProperty("path.separator"))+1;
		int lastIndex = path.lastIndexOf(File.separator) +1; 
		path = path.substring(firstIndex, lastIndex); 
		return path;
	}

}
