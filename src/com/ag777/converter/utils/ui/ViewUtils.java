package com.ag777.converter.utils.ui;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import javax.swing.text.StyleConstants;

public class ViewUtils {

	private ViewUtils(){}
	
	/**
	 * 从Jtextpanel中获取第一张图片,如果没有则返回null
	 * @param tp
	 * @return
	 */
	public static ImageIcon getImageFormTextPane(JTextPane tp) {
		List<ImageIcon> list = getImagesFormTextPane(tp);
		if(list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 从Jtextpanel中获取所有图片
	 * @param tp
	 * @return
	 */
	public static List<ImageIcon> getImagesFormTextPane(JTextPane tp) {
		List<ImageIcon> list = new ArrayList<>();
		for (int i = 0; i < tp.getStyledDocument().getRootElements()[0].getElement(0).getElementCount(); i++) {

			ImageIcon icon = (ImageIcon) StyleConstants
					.getIcon(tp.getStyledDocument().getRootElements()[0].getElement(0).getElement(i).getAttributes());
			if (icon != null) {
				list.add(icon);
			}
		}
		return list;
	}
	
	/**
	 * 将swing下的ImageIcon转化为buffedImage
	 * @param icon
	 * @return
	 */
	public static BufferedImage toBufferedImage(ImageIcon icon) {
		BufferedImage bi = new BufferedImage(icon.getIconWidth(),icon.getIconHeight(),BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bi.createGraphics();
		try {
			g2d.drawImage(icon.getImage(),0,0,null);
			return bi;
		} finally {
			g2d.dispose();
		}
		
	}
	
	public static void showInCenter(Component component){
		int w = (Toolkit.getDefaultToolkit().getScreenSize().width - component.getWidth()) / 2;
		int h = (Toolkit.getDefaultToolkit().getScreenSize().height - component.getHeight()) / 2;
		component.setLocation(w,h); 
		component.setVisible(true);
	}
	
}
