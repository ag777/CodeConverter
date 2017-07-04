package com.ag777.converter.module;

import com.ag777.converter.base.BaseTemplate;
import com.ag777.converter.module.template.HtmlTemplate;
import com.ag777.converter.module.template.JavaTemplate;

public class ConverterModuleImpl implements ConverterModuleFactory{

	private BaseTemplate htmlTemplet = new HtmlTemplate();
	private BaseTemplate javaTemplet = new JavaTemplate();
	
	
	private static ConverterModuleImpl converter = new ConverterModuleImpl();
	
	public static ConverterModuleImpl getInstant(){
		
		return converter;
	}
	
	private ConverterModuleImpl(){
		//单例
	}
	
	@Override
	public String convertToJava(String input) {
		input = pretreatment(input);
		
		
		StringBuilder sb = new StringBuilder();
		String[] group = input.split("\n");
		
		for (String s : group) {
			String content = s.replaceAll("^ +", "").replaceAll(" +$", "").replaceAll("\"", "\\\\\"");
			if(!content.isEmpty()) {
				sb.append(javaTemplet.getString(content));
			}
			
		}
		return sb.toString();
	}

	@Override
	public String convertToJs(String input) {
		input = pretreatment(input);
		
		StringBuilder sb = new StringBuilder();
		String[] group = input.split("\n");
		
		for (String s : group) {
			String content = s.replaceAll("^ +", "").replaceAll(" +$", "");
			if(!content.isEmpty()){	
				sb.append(htmlTemplet.getString(content));
			}
			
		}
		return sb.toString();
	}
	
	private static String pretreatment(String input) {	//预处理
		return input.replaceAll("\t", "").replaceAll("\r", "");
	}

	
	/**
	 * 占位符功能，暂时没好想法，舍弃
	 */
//	private static void handlePlaceholder(String input){	//处理占位符
//		Pattern pattern = Pattern.compile("\\$\\{.+?\\}");
//		Matcher matcher = pattern.matcher(input);
//		
//		StringBuffer sb = new StringBuffer();
//		while(matcher.find()) {
//	       
//	       String target = matcher.group(0);
//	       String variable = target.substring(1, target.length()-1);
//	       int index = matcher.start();
//	       matcher.appendReplacement(sb,"bb");
//	    }
//		matcher.appendTail(sb);
//		System.out.println(sb.toString()); 
//	}
}
