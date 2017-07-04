package com.ag777.converter.utils;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class ClipboardUtils {
	private ClipboardUtils(){}
	
	/**
	  * 向剪贴板中添加内容
	  */
	public static void setClipbordContents(String contents) {
		StringSelection stringSelection = new StringSelection( contents );
		// 系统剪贴板
		Clipboard  clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}
}
