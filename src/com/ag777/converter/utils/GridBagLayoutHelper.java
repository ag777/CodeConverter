package com.ag777.converter.utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

/**
 * 支持链式调用的包布局辅助类
 * @author ag777
 *
 */
public class GridBagLayoutHelper {

	private Container mContainer;
	private GridBagLayout gr;
	private GridBagConstraints gc;
	
	private GridBagLayoutHelper(){}
	
	public static GridBagLayoutHelper newInstance(Container container){
		return new GridBagLayoutHelper(container);
	}
	
	private GridBagLayoutHelper(Container container){
		mContainer = container;
		
		gr = new GridBagLayout();
		mContainer.setLayout(gr);
		gc = new GridBagConstraints();
	}
	
	public GridBagLayoutHelper addcomponent(Component view, int row, int col){
		gc.gridx= col;
		gc.gridy = row;
		gc.gridwidth = 1;
		gc.gridheight = 1;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.BOTH;
		
		gr.setConstraints(view,gc);
		mContainer.add(view);
		
		return this;
	}
}
