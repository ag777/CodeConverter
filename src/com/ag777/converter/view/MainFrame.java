package com.ag777.converter.view;


import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.*;

import com.ag777.converter.base.BaseContainer;
import com.ag777.converter.view.interf.MainView;

public class MainFrame extends JFrame implements MainView,BaseContainer{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4686487273039225125L;

//	private ConverterPanel converterPanel = new ConverterPanel();
	
	private Container c;		//自身容器
	private JPanel curPanel;	//当前界面
	private Menu menu = new Menu(this);
	
	public MainFrame() {
		super("html代码拼接工具");
		initView();
		initData();
	}
	
	@Override
	public void initView() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c = this.getContentPane();
		c.setLayout(new BorderLayout());
		
		
		setJMenuBar(menu);
		
		this.setSize(800,800);
	}

	@Override
	public void initData() {
	}

	@Override
	public void switchView(JPanel panel) {
		if(curPanel!=null)
			c.remove(curPanel);
		c.add(panel,BorderLayout.CENTER);
		curPanel = panel;
		//重画页面
		validate();
		repaint();
	}
	

}
