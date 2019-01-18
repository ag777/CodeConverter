package com.ag777.converter.utils.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * borderLayout相关工具类
 * @author wanggz
 *
 */
public class BorderLayoutUtils {

	private BorderLayoutUtils() {}
	
	/**
	 * 构造一个左右布局,左边是一个label,右边是任意component
	 * @param text
	 * @param c
	 * @return
	 */
	public static JPanel buildPanel(String text, Component c) {
		JPanel panel = new JPanel();
		BorderLayoutHelper.newInstance(panel)
			.addComponent(new JLabel(text), BorderLayout.WEST)
			.addComponent(c, BorderLayout.CENTER);
		return panel;
	}
}
