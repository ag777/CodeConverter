package com.ag777.converter.utils.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 弹窗工具类
 * @author ag777
 *
 */
public class DialogUtils {

	private DialogUtils(){}
	
	public static void showMsgDialog(Component parentComponent, String title, String content){
		JOptionPane.showMessageDialog(parentComponent, content, title,JOptionPane.WARNING_MESSAGE);
	}
	
	public static void showWarningDialog(Component parentComponent, String title, String content){
		JOptionPane.showMessageDialog(parentComponent, content, title,JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * 弹出一个二选框,并监听回调
	 * @param parentComponent
	 * @param title
	 * @param content
	 * @param consumer
	 */
	public static void showConfirmDialog(JFrame parentComponent, String title, String content, java.util.function.Consumer<Boolean> consumer) {
		int result = JOptionPane.showConfirmDialog(parentComponent, content, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		consumer.accept(result == JOptionPane.OK_OPTION);
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
	
	public static void showFileChooserDialog(JFrame parentComponent, String curPath,  Predicate<String> onInput, Consumer<String> onSuccess) {
		// 创建一个模态对话框
        final JDialog dialog = new JDialog(parentComponent, "提示", false);
        // 设置对话框的宽高
        dialog.setSize(450, 80);
        // 设置对话框大小不可改变
        dialog.setResizable(false);
        // 设置对话框相对显示的位置
        dialog.setLocationRelativeTo(parentComponent);

        // 创建一个标签显示消息内容 
    	JPanel panel_filePath = new JPanel();
    	JTextField tf_filePath = FilePicker.chooseDirInput(parentComponent, curPath, curPath, onInput);
    	JButton b_copy = new JButton("复制");
		BorderLayoutHelper.newInstance(panel_filePath)
			.addComponent(new JLabel("工程WebRoot路径: "), BorderLayout.WEST)
			.addComponent(tf_filePath, BorderLayout.CENTER)
			.addComponent(b_copy, BorderLayout.EAST);

		b_copy.addActionListener(e->{
			ClipboardUtils.setClipbordContents(tf_filePath.getText().trim());
			DialogUtils.showMsgDialog(parentComponent, "系统提示", "已经复制到剪贴板"); 
		});
		
		//按钮组
		JPanel panel_btnGroup = new JPanel();
        // 创建一个按钮用于关闭对话框
        JButton okBtn = new JButton("保存");
        okBtn.addActionListener(e->{
        	onSuccess.accept(tf_filePath.getText().trim());
        	dialog.dispose();
        });
        
        JButton cancelBtn = new JButton("取消");
        cancelBtn.addActionListener(e->{
        	dialog.dispose();
        });
        
        GridBagLayoutHelper.newInstance(panel_btnGroup)
				.addComponent(okBtn, 0, 0)
				.addComponent(cancelBtn, 0, 1);
        
        // 创建对话框的内容面板, 在面板内可以根据自己的需要添加任何组件并做任意是布局
        JPanel panel = new JPanel();

        panel.setLayout(new BorderLayout());
        panel.add(panel_filePath,BorderLayout.CENTER);
        panel.add(panel_btnGroup,BorderLayout.SOUTH);
        
        // 设置对话框的内容面板
        dialog.setContentPane(panel);
        // 显示对话框
        dialog.setVisible(true);
    }
}
