package com.ag777.converter.base;


import javax.swing.JPanel;

public abstract class BasePanel extends JPanel implements BaseContainer{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4818485273815570584L;

	public BasePanel(){
		initView();
		initData();
	}
	
	

	
}
