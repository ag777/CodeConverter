package com.ag777.converter.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ag777
 * 字符串工具类
 */
public class StringUtils {

	private StringUtils(){}
	
	/**
	 * 获取一个随机的uuid
	 * @return
	 */
	public static String uuid(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * 正则相关工具类
	 */
	public static class Regex {
		
		private Regex(){}
		
		/**
		 * regex
		 * @param source 源字符串
		 * @param regex 正则表达式
		 * @return
		 */
		public static List<MyFinder> find(String source,String regex) {
			List<MyFinder> result = new ArrayList<MyFinder>();
			
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(source);
			
			if (matcher.find()) {
				int gc = matcher.groupCount();
				for (int i = 0; i <= gc; i++) {
					MyFinder m = new MyFinder();
					m.content = matcher.group(i);
					m.start = matcher.start(i);
					m.end = matcher.end(i);
					result.add(m);
				}
			}
			
			return result;
		}
		
		public static class MyFinder{
			String content;
			int start;
			int end;
		}
		
	}
	
	
	/**
	 * json(格式化)相关工具
	 */
	public static class Json {

		private Json() {}

		/**
		 * json字符串的格式化(用于输出文件)
		 * 
		 * @param json
		 * @param
		 * @return
		 */
		public static String formatJson(String json) {
			return formatJson(json, "\t");
		}

		/**
		 * json字符串的格式化(用于输出文件)
		 * 
		 * @param json
		 * @param fillStringUnit
		 *            换行后添加的字符串
		 * @return
		 */
		public static String formatJson(String json, String fillStringUnit) {
			if (json == null || json.trim().length() == 0) {
				return null;
			}

			int fixedLenth = 0;
			ArrayList<String> tokenList = new ArrayList<String>();
			{
				String jsonTemp = json;
				// 预读取
				while (jsonTemp.length() > 0) {
					String token = getToken(jsonTemp);
					jsonTemp = jsonTemp.substring(token.length());
					token = token.trim();
					tokenList.add(token);
				}
			}

			for (int i = 0; i < tokenList.size(); i++) {
				String token = tokenList.get(i);
				int length = token.getBytes().length;
				if (length > fixedLenth && i < tokenList.size() - 1 && tokenList.get(i + 1).equals(":")) {
					fixedLenth = length;
				}
			}

			StringBuilder buf = new StringBuilder();
			int count = 0;
			for (int i = 0; i < tokenList.size(); i++) {

				String token = tokenList.get(i);

				if (token.equals(",")) {
					buf.append(token);
					doFill(buf, count, fillStringUnit);
					continue;
				}
				if (token.equals(":")) {
					buf.append(" ").append(token).append(" ");
					continue;
				}
				if (token.equals("{")) {
					String nextToken = tokenList.get(i + 1);
					if (nextToken.equals("}")) {
						i++;
						buf.append("{ }");
					} else {
						count++;
						buf.append(token);
						doFill(buf, count, fillStringUnit);
					}
					continue;
				}
				if (token.equals("}")) {
					count--;
					doFill(buf, count, fillStringUnit);
					buf.append(token);
					continue;
				}
				if (token.equals("[")) {
					String nextToken = tokenList.get(i + 1);
					if (nextToken.equals("]")) {
						i++;
						buf.append("[ ]");
					} else {
						count++;
						buf.append(token);
						doFill(buf, count, fillStringUnit);
					}
					continue;
				}
				if (token.equals("]")) {
					count--;
					doFill(buf, count, fillStringUnit);
					buf.append(token);
					continue;
				}

				buf.append(token);
				// 左对齐
				if (i < tokenList.size() - 1 && tokenList.get(i + 1).equals(":")) {
					int fillLength = fixedLenth - token.getBytes().length;
					if (fillLength > 0) {
						for (int j = 0; j < fillLength; j++) {
							buf.append(" ");
						}
					}
				}
			}
			return buf.toString();
		}

		private static String getToken(String json) {
			StringBuilder buf = new StringBuilder();
			boolean isInYinHao = false;
			while (json.length() > 0) {
				String token = json.substring(0, 1);
				json = json.substring(1);

				if (!isInYinHao && (token.equals(":") || token.equals("{") || token.equals("}") || token.equals("[") || token.equals("]") || token.equals(","))) {
					if (buf.toString().trim().length() == 0) {
						buf.append(token);
					}

					break;
				}

				if (token.equals("\\")) {
					buf.append(token);
					buf.append(json.substring(0, 1));
					json = json.substring(1);
					continue;
				}
				if (token.equals("\"")) {
					buf.append(token);
					if (isInYinHao) {
						break;
					} else {
						isInYinHao = true;
						continue;
					}
				}
				buf.append(token);
			}
			return buf.toString();
		}

		private static void doFill(StringBuilder buf, int count, String fillStringUnit) {
			buf.append("\r\n");
			for (int i = 0; i < count; i++) {
				buf.append(fillStringUnit);
			}
		}
	}
	
	/**
     * 下划线转驼峰法
     * @param line 源字符串
     * @param smallCamel 大小驼峰,是否为小驼峰
     * @return 转换后的字符串
     */
    public static String underline2Camel(String line,boolean smallCamel){
        if(line==null||"".equals(line)){
            return "";
        }
        StringBuffer sb=new StringBuffer();
        Pattern pattern=Pattern.compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher=pattern.matcher(line);
        while(matcher.find()){
            String word=matcher.group();
            sb.append(smallCamel&&matcher.start()==0?Character.toLowerCase(word.charAt(0)):Character.toUpperCase(word.charAt(0)));
            int index=word.lastIndexOf('_');
            if(index>0){
                sb.append(word.substring(1, index).toLowerCase());
            }else{
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }
	
    /**
	 * 首字母大写
	 * @param str
	 * @return
	 */
	public static String upperCase(String str) {  
	    char[] ch = str.toCharArray();  
	    if (ch[0] >= 'a' && ch[0] <= 'z') {  
	        ch[0] = (char) (ch[0] - 32);  
	    }  
	    return new String(ch);  
	}  
}
