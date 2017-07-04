package com.ag777.converter.module;

import java.util.Iterator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ag777.converter.utils.StringUtils;

public class DataTableModuleImpl implements DataTableModuleFactory{

	private static DataTableModuleImpl dataTable = new DataTableModuleImpl();
	
	public static DataTableModuleImpl getInstant(){
		
		return dataTable;
	}
	
	@Override
	public String getDataTable(String input, String url) {
		try{
			JSONObject jb = JSON.parseObject(input);
			JSONArray array = jb.getJSONArray("data");
			
			StringBuilder sb = new StringBuilder();
			
			sb .append("$.Codeapes.datatables({");
			
				//头部
				initTableHeader(url, sb);
				//列表
				initTableColumns(array, sb);
				//尾部
				initTableFooter(sb);
			
			sb.append("});");
			
			return getResult(sb);
		}catch(Exception ex) {
			return null;
		}
		
	}
	
	private static void initTableHeader(String url, StringBuilder sb){
		sb.append("id:'datatables',");
		sb.append("url:'");
			sb.append(url);
		sb.append("',");
		sb.append("form:'datatables-search-form',");
		sb.append("lengthMenu : [ 10,25,50,100 ],");
	}
	
	private static void initTableColumns(JSONArray array, StringBuilder sb) throws Exception{
		try {
		sb.append("columns:");
		sb.append("[");
		
			sb.append(" {");
			sb.append("title : '序号',");
			sb.append("width:70,");
			sb.append("className:'thc tdc',");
			sb.append("render:function(data, type, row, meta){");
			sb.append("return meta.row + 1;");
			sb.append("}");
			sb.append("},");
		
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = (JSONObject) array.get(i);
			Iterator<String> iter = obj.keySet().iterator();
	        while(iter.hasNext()){
	            String key = iter.next();
	            Object value = obj.get(key);
	            sb.append(" {");
	            	fillRow(key, value, sb);
	            sb.append("},");
	        }
		}
		
		sb.deleteCharAt(sb.length()-1);	//去掉最后一个逗号
		
		sb.append(" ],");
		}catch(Exception ex) {
			throw ex;
		}

	}
	
	private static void fillRow(String key, Object value, StringBuilder sb){
        sb.append("title : '列表头',");
        sb.append("width:100,");
		sb.append("name : '");
			sb.append(key);
		sb.append("',");
		sb.append("data : '");
			sb.append(key);
		sb.append("',");
		sb.append("className:'thc tdc'");
		if(value instanceof Long) {
			if(key.toLowerCase().contains("date")) {
				sb.append(",");
				sb.append("render:function(data, type, row, meta){");
				sb.append("if(data != undefined){");
				sb.append("return new Date(data).pattern('yyyy-MM-dd');");
				sb.append("}");
				sb.append("}");
			}else if(key.toLowerCase().contains("datetime")){
				sb.append(",");
				sb.append("render:function(data, type, row, meta){");
				sb.append("if(data != undefined){");
				sb.append("return new Date(data).pattern('yyyy-MM-dd HH:mm:ss');");
				sb.append("}");
				sb.append("}");
			}
		}
		
		//System.out.println(key+":"+value.getClass());
	}
	
	private static void initTableFooter(StringBuilder sb){
		sb.append("fnDrawCallback:function(setting){");
		sb.append("/**");
		sb.append("* 初始化提示");
		sb.append("*/");
		sb.append("$('[data-toggle=\"tooltip\"]').tooltip({container: 'body'});");
		sb.append("/*查看站点*/");
		sb.append("$('.xxx').click(function(){");
		sb.append("var record = $.Codeapes.dt['org-datatables'].getData($(this).data('row'));");
		sb.append("if(record != null){");
		sb.append("//do sth");
		sb.append("}");
		sb.append("return false;");
		sb.append("});");
		sb.append("},");
		sb.append("buttons:(function(){");
		sb.append("var buttons = new Array();");
		sb.append("buttons.push({");
		sb.append("sExtends:'text',");
		sb.append("sButtonText:'重置',");
		sb.append("sButtonClass: \"btn btn-default\",");
		sb.append("sButtonIClass:\"fa fa-undo\",");
		sb.append("sIconcls:'',");
		sb.append("fnClick:function(nButton, oConfig, oFlash){");
		sb.append("$.Codeapes.dt['org-datatables'].reset();");
		sb.append("}");
		sb.append("});");
		sb.append("buttons.push({");
		sb.append("sExtends:'text',");
		sb.append("sButtonText:'搜索',");
		sb.append("sButtonClass: \"btn btn-default\",");
		sb.append("sButtonIClass:\"fa fa-search\",");
		sb.append("sIconcls:'',");
		sb.append("fnClick:function(nButton, oConfig, oFlash){");
		sb.append("$.Codeapes.dt['org-datatables'].search();");
		sb.append("}");
		sb.append("});");
		sb.append("return buttons;");
		sb.append("})()");
	}
	
	private static String getResult(StringBuilder sb) {	//处理并获取最终字符串
		String result = StringUtils.Json.formatJson(sb.toString());
		//result = result.replaceAll(regex, replacement);	//function()间的 \n去除,暂时没方法,正则只能匹配到之间的所有字符
		return result;
	}
}
