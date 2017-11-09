package com.ag777.converter.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
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
import com.ag777.converter.utils.ConfigUtils;
import com.ag777.converter.utils.DialogUtils;
import com.ag777.converter.utils.GridBagLayoutHelper;
import com.ag777.converter.utils.bean.RegexRule;
import com.ag777.converter.view.interf.JfinalDbBuilderView;
import com.ag777.util.file.FileUtils;
import com.ag777.util.lang.StringUtils;
import com.ag777.util.lang.collection.ListUtils;

public class JfinalDbBuilderPanel extends BasePanel implements JfinalDbBuilderView{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8164616182191960801L;

	private JFinalDBPresenter mPresenter;
	
	private JButton btn_start;
	private JButton btn_toFile;
	private JTextField tf_ip;
	private JTextField tf_port;
	private JTextField tf_dbName;
	private JTextField tf_userName;
	private JTextField tf_pwd;
	private JTextField tf_clazzName;
	private JComboBox<String> cb_tableName;
	private JComboBox<String> cb_modelName;
	private JTextArea ta_output;
	
	@Override
	public void initView() {
		btn_start = new JButton("开始构建");
		btn_toFile = new JButton("保存到文件");
		tf_ip = new JTextField();
		tf_port = new JTextField();
		tf_dbName = new JTextField();
		tf_userName = new JTextField();
		tf_pwd = new JTextField();
		
		tf_clazzName = new JTextField();
		
		cb_modelName = new JComboBox<>();
		  
		
		cb_tableName = new JComboBox<>();
		
		ta_output = new JTextArea();
		
		JPanel panel_ta_group = new JPanel();
		
		
		//按钮组
		JPanel panel_btn_group = new JPanel();

		
		GridBagLayoutHelper.newInstance(panel_btn_group)
			.addcomponent(btn_start, 0, 0)
			.addcomponent(btn_toFile, 0, 1);
		btn_start.setEnabled(false);
		btn_toFile.setEnabled(false);
		
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
			
			JPanel panel_modelName = new JPanel();
			BorderLayoutHelper.newInstance(panel_modelName)
				.addComponent(new JLabel("模板: "), BorderLayout.WEST)
				.addComponent(cb_modelName, BorderLayout.CENTER);
			
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
			.addcomponent(panel_modelName, 6, 0)
			.addcomponent(panel_tableName, 7, 0);
		
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
					String templateName = cb_modelName.getSelectedItem().toString();
					clazzName = StringUtils.upperCaseFirst(clazzName);	//首字母大写
					
					
					if("other".equals(templateName)) {
						JFileChooser jfc = null;
						Optional<String> path = ConfigUtils.getInstance().beanPathTemplateIn();	//模板路径(文件)
						if(path.isPresent()) {
							jfc = new JFileChooser(path.get());
						} else {
							jfc = new JFileChooser();
						}
						
						jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
						jfc.showDialog(new JLabel(), "选择");
						
						File modelFile=jfc.getSelectedFile();    
						if(modelFile == null) {
							return;
						}
						ConfigUtils.getInstance().beanPathTemplateIn(modelFile.getParent());
						ConfigUtils.getInstance().beanTemplate(templateName);
						mPresenter.build(tableName, clazzName, modelFile);
					} else {
						ConfigUtils.getInstance().beanTemplate(templateName);
						mPresenter.build(tableName, clazzName, getClass().getClassLoader().getResourceAsStream(
								"template/"+templateName+".txt"));
					}
					
					
				} else {
					DialogUtils.showMsgDialog(JfinalDbBuilderPanel.this, "【类名不能为空】", "系统提示");
				}
				
			}
		});
		
		btn_toFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String clazzName = tf_clazzName.getText().trim();
				if(!clazzName.isEmpty()) {
					clazzName = StringUtils.upperCaseFirst(clazzName);	//首字母大写
					JFileChooser jfc = null;
					Optional<String> path = ConfigUtils.getInstance().beanPathFileOut();	//文件路径(文件夹)
					if(path.isPresent()) {
						jfc = new JFileChooser(path.get());
					} else {
						jfc = new JFileChooser();
					}
					
					jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					jfc.showDialog(new JLabel(), "保存路径");
					
					File saveFile=jfc.getSelectedFile();    
					if(saveFile == null) {
						return;
					}
					ConfigUtils.getInstance().beanPathFileOut(saveFile.getPath());
					try {
						String savePath = StringUtils.concat(saveFile.getPath(),File.separator,clazzName,".java");
						FileUtils.write(savePath, ta_output.getText(), null, true);
						DialogUtils.showMsgDialog(JfinalDbBuilderPanel.this, "保存成功", "系统提示");
					} catch (IOException e1) {
						e1.printStackTrace();
						DialogUtils.showMsgDialog(JfinalDbBuilderPanel.this, "未知异常", "系统提示");
					}
				} else {
					DialogUtils.showMsgDialog(JfinalDbBuilderPanel.this, "【类名不能为空】", "系统提示");
				}
				
			}
		});
		
		cb_tableName.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){  
					String item = (String) e.getItem();
					tf_clazzName.setText(StringUtils.underline2Camel(item, false));
					cb_tableName.updateUI();	//不加这句点击完会有残像
				}
				
			}
		});
		
		doValidate();	//验证参数并尝试连接数据库(initView方法填入了一些默认配置)
	}
	
	@Override
	public void showResult(String output) {
		ta_output.setText(output);
		ClipboardUtils.setClipbordContents(output);
		DialogUtils.showMsgDialog(this, "已经复制到剪贴板", "系统提示"); 
		btn_toFile.setEnabled(true);
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
		fillTextField(tf_ip, ConfigUtils.getInstance().beanIp());
		fillTextField(tf_port, ConfigUtils.getInstance().beanPort());
		fillTextField(tf_dbName, ConfigUtils.getInstance().beanDbName());
		fillTextField(tf_userName, ConfigUtils.getInstance().beanUser());
		fillTextField(tf_pwd, ConfigUtils.getInstance().beanPwd());
		
		String[] templates = new String[]{"base", "model_jfinal_sepcial"};
		for(int i=0; i<templates.length; i++) {
			cb_modelName.addItem(templates[i]);
		}
		cb_modelName.addItem("other");
		Optional<String> template = ConfigUtils.getInstance().beanTemplate();
		if(template.isPresent() && ListUtils.inArray(templates, template.get()).isPresent()) {
			cb_modelName.setSelectedItem(template.get());
		}
		
	}
	
	
	
	private void fillTextField(JTextField tf, Optional<String> result) {
		if(result.isPresent()) {
			tf.setText(result.get());
		}
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
