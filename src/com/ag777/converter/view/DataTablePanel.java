package com.ag777.converter.view;

import java.awt.BorderLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.ag777.converter.base.BasePanel;
import com.ag777.converter.presenter.DataTablePresenter;
import com.ag777.converter.utils.BorderLayoutHelper;
import com.ag777.converter.utils.ClipboardUtils;
import com.ag777.converter.utils.DialogUtils;
import com.ag777.converter.utils.GridBagLayoutHelper;
import com.ag777.converter.utils.HttpUtils;
import com.ag777.converter.view.interf.ConverterView;

public class DataTablePanel extends BasePanel implements ActionListener, ConverterView {

	/**
	 * 
	 */
	private static final long serialVersionUID = -647741842623682495L;
	
	private JButton btn_start;
	private JPanel panel_top;
	private JTextField tf_url;
	private JButton btn_post;
	
	private JTextArea ta_input;
	private JTextArea ta_output;
	
	private DataTablePresenter mDataTablePresenter;
	
	public DataTablePanel() {
		super();
	}

	@Override
	public void initView() {
		
		btn_start = new JButton("开始构建");
		panel_top = new JPanel();
		tf_url = new JTextField();
		btn_post = new JButton("获取json");
		ta_input = new JTextArea();
		ta_output = new JTextArea();
		
		//按钮组
		JPanel panel_btn_group = new JPanel();

		
		GridBagLayoutHelper.newInstance(panel_btn_group)
			.addcomponent(btn_start, 0, 0);
		
		//输入框组
		JPanel panel_ta_group = new JPanel();
		ScrollPane sp_input = new ScrollPane();
		panel_top.setBorder(new TitledBorder(new EtchedBorder(), "输入json"));
			sp_input.add(ta_input);
		panel_top.setLayout(new BorderLayout());
		
		JPanel panel_top_url = new JPanel();
		BorderLayoutHelper.newInstance(panel_top_url)
			.addComponent(new JLabel("输入url: "), BorderLayout.WEST)
			.addComponent(tf_url, BorderLayout.CENTER)
			.addComponent(btn_post, BorderLayout.EAST);
		
		BorderLayoutHelper.newInstance(panel_top)
			.addComponent(panel_top_url, BorderLayout.NORTH)
			.addComponent(sp_input, BorderLayout.CENTER);
		
		//输出框组
		ScrollPane sp_output = new ScrollPane();
			ta_output.setBorder(new TitledBorder(new EtchedBorder(), "输出"));
			sp_output.add(ta_output);
			
		ta_output.setEditable(false);
		
		GridBagLayoutHelper.newInstance(panel_ta_group)
			.addcomponent(panel_top, 0, 0)
			.addcomponent(sp_output, 1, 0);
		
		
		panel_ta_group.setSize(800,800);
		
		//整合
		this.setLayout(new BorderLayout());
		this.add(panel_ta_group,BorderLayout.CENTER);
		this.add(panel_btn_group,BorderLayout.SOUTH);
	}

	@Override
	public void initData() {
		mDataTablePresenter = new DataTablePresenter();
		mDataTablePresenter.attachView(this);
		
		btn_start.addActionListener(this);
		btn_post.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object v = e.getSource();
		
		if(v.equals(btn_start)) {
			String url = tf_url.getText();
			String input = ta_input.getText();
			mDataTablePresenter.getDataTable(input,url);
		}else if(v.equals(btn_post)) { 
			String url = tf_url.getText();
			String result = HttpUtils.doPost(url, new HashMap<String, String>());
			if(result == null){	//连不上服务端
				DialogUtils.showMsgDialog(this, "获取JSON失败，请检查url及网络状态!", "系统提示"); 
			}else {
				ta_input.setText(result);
			}	
			
		}
	}
	
	@Override
	public void showResult(String output) {
		if(output!=null){
			ta_output.setText(output);
			ClipboardUtils.setClipbordContents(output);
			DialogUtils.showMsgDialog(this, "已经复制到剪贴板", "系统提示"); 
		}else {
			DialogUtils.showMsgDialog(this, "构筑失败，请检查JSON格式是否正确!", "系统提示"); 
		}
	}
	
}
