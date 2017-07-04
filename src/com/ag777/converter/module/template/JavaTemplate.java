package com.ag777.converter.module.template;

import com.ag777.converter.base.BaseTemplate;

public class JavaTemplate implements BaseTemplate{

	@Override
	public String getString(String content) {
		
		return String.format("sb.append(\"%s\");\n", content);
	}

	@Override
	public String getString(String... contents) {
		return getString(contents[0]);
	}

}
