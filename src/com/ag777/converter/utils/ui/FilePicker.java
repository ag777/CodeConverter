package com.ag777.converter.utils.ui;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.function.Predicate;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;

public class FilePicker {

	private FilePicker() {}
	
	/**
	 * 选择并返回文件夹
	 * @return
	 */
	public static File chooseDir(String curPath, FileFilter filter) {
		return choose(curPath, filter, JFileChooser.DIRECTORIES_ONLY);
	}
	
	public static File choose(String curPath, FileFilter filter, int mode) {
		JFileChooser jfc=new JFileChooser();  
		if(curPath != null) {
			jfc.setCurrentDirectory(new File(curPath));
		} else {
			 //设置当前路径为桌面路径,否则将我的文档作为默认路径
	        FileSystemView fsv = FileSystemView .getFileSystemView();
	        jfc.setCurrentDirectory(fsv.getHomeDirectory());
		}
		
		//设置文件过滤
        jfc.setFileSelectionMode(mode);  
        if(filter != null) {
        	jfc.setFileFilter(filter);
        }
        //弹出的提示框的标题
        jfc.showDialog(new JLabel(), "确定");  
        //用户选择的路径或文件
        return jfc.getSelectedFile();  
	}
	
	public static JTextField chooseDirInput(Component parentComponent, String value, String path,  Predicate<String> onInput) {
		JTextField tf = new JTextField(value);
		return bind(tf, parentComponent, path, onInput, null,  JFileChooser.DIRECTORIES_ONLY);
	}
	
	
	public static JTextField bind(JTextField tf, Component parentComponent, String path,  Predicate<String> onInput, FileFilter filter, int mode) {
		tf.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				File file = FilePicker.choose(tf.getText().trim().isEmpty()?path:tf.getText().trim(), filter, mode);
				if(file != null) {
					String path = file.getAbsolutePath();
					if(file.isDirectory()) {
						path+="\\";
					}
					if(onInput.test(path)) {
						tf.setText(path);
					}
					
				}
				
			}
		});
		
		tf.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				e.consume();
			}
			@Override
			public void keyReleased(KeyEvent e) {
				e.consume();
			}
			@Override
			public void keyPressed(KeyEvent e) {
				e.consume();
			}
		});
		
		return tf;
	}
}
