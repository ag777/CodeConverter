package com.ag777.converter.module.template;

import com.ag777.converter.base.BaseTemplate;

public class HtmlTemplate implements BaseTemplate{

	@Override
	public String getString(String content) {
		return String.format("html += '%s';\n", content);
	}
	
	@Override
	public String getString(String... contents) {
		return getString(contents[0]);
	}

}
