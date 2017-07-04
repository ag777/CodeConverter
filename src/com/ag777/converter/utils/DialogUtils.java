package com.ag777.converter.utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * 弹窗工具类
 * @author ag777
 *
 */
public class DialogUtils {

	private DialogUtils(){}
	
	public static void showMsgDialog(Component parentComponent, String content, String title){
		JOptionPane.showMessageDialog(parentComponent, content, title,JOptionPane.WARNING_MESSAGE);
	}
	
	public static void showMsgDialog(JFrame parentComponent,String title, Map<String, String> msgMap, Dimension size){
		JDialog jd = new JDialog(parentComponent, title,true);
		Container c = jd.getContentPane();
		c.setLayout(new FlowLayout());
		Iterator<String> iter = msgMap.keySet().iterator();
        while(iter.hasNext()){
            String key = iter.next();
            Object value = msgMap.get(key);
            c.add(new JLabel(String.format("%s : %s", key,value)));
        }
		
		jd.setSize(size);
		ViewUtils.showInCenter(jd);
	}
	
	public static void showMsgDialog(JFrame parentComponent, String title, JPanel content, Dimension size) {
		JDialog jd = new JDialog(parentComponent, title,true);
		Container c = jd.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(content,BorderLayout.CENTER);
		
		jd.setSize(size);
		ViewUtils.showInCenter(jd);
	}
}
