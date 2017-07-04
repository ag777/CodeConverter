package com.ag777.converter.utils;

import java.io.File;

public class FileUtils {

	private FileUtils(){}
	
	public static String getJarPath(){
		// 关键是这行...  
        String path = FileUtils.class.getProtectionDomain().getCodeSource().getLocation().getFile();  
        try  
        {  
            path = java.net.URLDecoder.decode(path, "UTF-8"); // 转换处理中文及空格  
        }  
        catch (java.io.UnsupportedEncodingException e)  
        {  
            return null;  
        }  
        return new File(path).getAbsolutePath();
	}
}
