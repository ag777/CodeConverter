package com.ag777.converter.utils;

import java.awt.Component;
import java.awt.Toolkit;

public class ViewUtils {

	private ViewUtils(){}
	
	public static void showInCenter(Component component){
		int w = (Toolkit.getDefaultToolkit().getScreenSize().width - component.getWidth()) / 2;
		int h = (Toolkit.getDefaultToolkit().getScreenSize().height - component.getHeight()) / 2;
		component.setLocation(w,h); 
		component.setVisible(true);
	}
	
}
