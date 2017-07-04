package com.ag777.converter.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.ag777.converter.base.BasePanel;
import com.ag777.converter.presenter.JFinalDBPresenter;
import com.ag777.converter.utils.BorderLayoutHelper;
import com.ag777.converter.utils.ClipboardUtils;
import com.ag777.converter.utils.DialogUtils;
import com.ag777.converter.utils.GridBagLayoutHelper;
import com.ag777.converter.utils.StringUtils;
import com.ag777.converter.utils.bean.RegexRule;
import com.ag777.converter.view.interf.JfinalDbBuilderView;

public class JfinalDbBuilderPanel extends BasePanel implements JfinalDbBuilderView{

	private JFinalDBPresenter mPresenter;
	
	private JButton btn_start;
	private JTextField tf_ip;
	private JTextField tf_port;
	private JTextField tf_dbName;
	private JTextField tf_userName;
	private JTextField tf_pwd;
	private JTextField tf_clazzName;
	private JComboBox cb_tableName;
	private JTextArea ta_output;
	
	@Override
	public void initView() {
		btn_start = new JButton("开始构建");
		tf_ip = new JTextField();
		tf_port = new JTextField();
		tf_dbName = new JTextField();
		tf_userName = new JTextField();
		tf_pwd = new JTextField();
		
		tf_clazzName = new JTextField();
		
		cb_tableName = new JComboBox();
		
		ta_output = new JTextArea();
		
		JPanel panel_ta_group = new JPanel();
		
		
		//按钮组
		JPanel panel_btn_group = new JPanel();

		
		GridBagLayoutHelper.newInstance(panel_btn_group)
			.addcomponent(btn_start, 0, 0);
		btn_start.setEnabled(false);
		
		//输入框组
		JPanel panel_top = new JPanel();
		panel_top.setBorder(new TitledBorder(new EtchedBorder(), "输入json"));
//		panel_top.setLayout(new FlowLayout(FlowLayout.LEADING));
		
			JPanel panel_ip = new JPanel();
			BorderLayoutHelper.newInstance(panel_ip)
				.addComponent(new JLabel("IP: "), BorderLayout.WEST)
				.addComponent(tf_ip, BorderLayout.CENTER);
			
			JPanel panel_port = new JPanel();
			BorderLayoutHelper.newInstance(panel_port)
				.addComponent(new JLabel("端口号: "), BorderLayout.WEST)
				.addComponent(tf_port, BorderLayout.CENTER);
			
			JPanel panel_dbName = new JPanel();
			BorderLayoutHelper.newInstance(panel_dbName)
				.addComponent(new JLabel("数据库名: "), BorderLayout.WEST)
				.addComponent(tf_dbName, BorderLayout.CENTER);
		
			JPanel panel_userName = new JPanel();
			BorderLayoutHelper.newInstance(panel_userName)
				.addComponent(new JLabel("用户名: "), BorderLayout.WEST)
				.addComponent(tf_userName, BorderLayout.CENTER);
			
			JPanel panel_pwd = new JPanel();
			BorderLayoutHelper.newInstance(panel_pwd)
				.addComponent(new JLabel("密码: "), BorderLayout.WEST)
				.addComponent(tf_pwd, BorderLayout.CENTER);
			
			JPanel panel_tableName = new JPanel();
			BorderLayoutHelper.newInstance(panel_tableName)
				.addComponent(new JLabel("表名: "), BorderLayout.WEST)
				.addComponent(cb_tableName, BorderLayout.CENTER);
			
			JPanel panel_clazzName = new JPanel();
			BorderLayoutHelper.newInstance(panel_clazzName)
				.addComponent(new JLabel("类名: "), BorderLayout.WEST)
				.addComponent(tf_clazzName, BorderLayout.CENTER);
			
//		panel_top.add(panel_ip);
//		panel_top.add(panel_port);
//		panel_top.add(panel_dbName);
		GridBagLayoutHelper.newInstance(panel_top)
			.addcomponent(panel_ip, 0, 0)
			.addcomponent(panel_port, 1, 0)
			.addcomponent(panel_dbName, 2, 0)
			.addcomponent(panel_userName, 3, 0)
			.addcomponent(panel_pwd, 4, 0)
			.addcomponent(panel_clazzName, 5, 0)
			.addcomponent(panel_tableName, 6, 0);
		
		fillDefault();	//往输入框组填入一些默认值
		
		//输出框组
		ScrollPane sp_output = new ScrollPane();
			ta_output.setBorder(new TitledBorder(new EtchedBorder(), "输出"));
			sp_output.add(ta_output);
			
		ta_output.setEditable(false);
		
//		GridBagLayoutHelper.newInstance(panel_ta_group)
//			.addcomponent(panel_top, 0, 0)
//			.addcomponent(sp_output, 1, 0);
		
		BorderLayoutHelper.newInstance(panel_ta_group)
			.addComponent(panel_top, BorderLayout.NORTH)
			.addComponent(sp_output, BorderLayout.CENTER);
		
		
		//整合
		this.setLayout(new BorderLayout());
		add(panel_ta_group,BorderLayout.CENTER);
		add(panel_btn_group,BorderLayout.SOUTH);
	}

	@Override
	public void initData() {
		mPresenter = new JFinalDBPresenter();
		mPresenter.attachView(this);
		
		initValidate();
		btn_start.addActionListener(new ActionListener() {	//"开始构建"按钮
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String clazzName = tf_clazzName.getText().trim();
				if(!clazzName.isEmpty()) {
					String tableName = cb_tableName.getSelectedItem().toString();
					clazzName = StringUtils.upperCase(clazzName);	//首字母大写
					mPresenter.build(tableName, clazzName);
				} else {
					DialogUtils.showMsgDialog(JfinalDbBuilderPanel.this, "【类名不能为空】", "系统提示");
				}
				
			}
		});
	}

	
	@Override
	public void showResult(String output) {
		ta_output.setText(output);
		ClipboardUtils.setClipbordContents(output);
		DialogUtils.showMsgDialog(this, "已经复制到剪贴板", "系统提示"); 
	}
	
	@Override
	public void remove(Component comp) {	//安卓由于有onDestroy方法所以这样封装没问题，swing的话就先这样吧
		super.remove(comp);
		mPresenter.detachView();	//解除业务层的绑定
	}
	
	/**
	 * 填入一些默认值
	 */
	public void fillDefault() {
		tf_ip.setText("127.0.0.1");
		tf_port.setText("3306");
		tf_dbName.setText("");
		tf_userName.setText("root");
		tf_pwd.setText("");
	}
	
	private void initValidate() {
		initOnchangeListener(tf_ip);
		initOnchangeListener(tf_port);
		initOnchangeListener(tf_dbName);
		initOnchangeListener(tf_userName);
		initOnchangeListener(tf_pwd);
	}
	
	private void doValidate() {
		String ip = tf_ip.getText().trim();
		String port = tf_port.getText().trim();
		String dbName = tf_dbName.getText().trim();
		String userName = tf_userName.getText().trim();
		String pwd = tf_pwd.getText();
		boolean ipValid = ip.matches(RegexRule.Ip.IPV4);
		boolean portValid = port.matches(RegexRule.Port.DEFAULT);
		boolean dbNameValid = !dbName.trim().isEmpty();
		boolean userNameValid = !userName.trim().isEmpty();
		boolean pwdValid = true;//!pwd.trim().isEmpty();
		
		if(ipValid&&
				portValid&&
				dbNameValid&&
				userNameValid&&
				pwdValid) {
			System.out.println("验证通过");
			cb_tableName.removeAllItems();	//先移除所有下拉框项
			if(mPresenter.init(ip, port, dbName, userName, pwd)) {
				System.out.println("连接成功");
				List<String> tableNameList = mPresenter.getTbaleList();
				for (String tableName : tableNameList) {
					cb_tableName.addItem(tableName);
				}
				btn_start.setEnabled(true);
			} else {
				System.out.println("连接失败");
			}
		} else {
			
		}
		
		if(cb_tableName.getItemCount()<=0) {
			btn_start.setEnabled(false);
		}
	}
	
	private void initOnchangeListener(JTextField tf) {
		tf.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				doValidate();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {	
				doValidate();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				doValidate();
			}
		});
	}
	
	
}
