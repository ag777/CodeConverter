package com.ag777.converter.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.ag777.converter.base.BasePanel;
import com.ag777.converter.presenter.ConverterPresenter;
import com.ag777.converter.utils.ClipboardUtils;
import com.ag777.converter.utils.DialogUtils;
import com.ag777.converter.utils.GridBagLayoutHelper;
import com.ag777.converter.view.interf.ConverterView;

public class ConverterPanel extends BasePanel implements ActionListener, ConverterView{

	
	private JButton btn_java;
	private JButton btn_js;
	private JTextArea ta_input;
	private JTextArea ta_output;
	
	private ConverterPresenter mConverterPresenter;
	
	public ConverterPanel() {
		super();
		
	}
	
	@Override
	public void initView()
	{
		
		btn_java = new JButton("html -> java");
		btn_js = new JButton("html -> js");
		ta_input = new JTextArea();
		ta_output = new JTextArea();
		
		//按钮组
		JPanel panel_btn_group = new JPanel();
		
		
		GridBagLayoutHelper.newInstance(panel_btn_group)
			.addcomponent(btn_java, 0, 0)
			.addcomponent(btn_js, 0, 1);
		
		
		
		//输入框组
		JPanel panel_ta_group = new JPanel();
		ScrollPane sp_input = new ScrollPane();
			ta_input.setBorder(new TitledBorder(new EtchedBorder(), "输入html"));
			sp_input.add(ta_input);
		ScrollPane sp_output = new ScrollPane();
			ta_output.setBorder(new TitledBorder(new EtchedBorder(), "输出"));
			sp_output.add(ta_output);
			
		ta_output.setEditable(false);
		
		GridBagLayoutHelper.newInstance(panel_ta_group)
			.addcomponent(sp_input, 0, 0)
			.addcomponent(sp_output, 1, 0);
		
		
		panel_ta_group.setSize(800,800);
		
		//整合
		setLayout(new BorderLayout());
		add(panel_ta_group,BorderLayout.CENTER);
		add(panel_btn_group,BorderLayout.SOUTH);
		
		
//		this.setSize(320, 240);
	}

	@Override
	public void initData(){
		mConverterPresenter = new ConverterPresenter();
		mConverterPresenter.attachView(this);
		
		//按钮事件设置
		btn_java.addActionListener(this);
		btn_js.addActionListener(this);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object v = e.getSource();
		
		if(v.equals(btn_java)) {
			String input = ta_input.getText();
			mConverterPresenter.convert(input, ConverterPresenter.TOJAVA);
		}else if(v.equals(btn_js)){
			String input = ta_input.getText();
			mConverterPresenter.convert(input, ConverterPresenter.TOJS);
		}
		
		
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
		mConverterPresenter.detachView();	//解除业务层的绑定
	}
	
}
