package com.ag777.converter.utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;


public class BorderLayoutHelper {

	private Container mContainer;
	
	public static BorderLayoutHelper newInstance(Container container){
		return new BorderLayoutHelper(container);
	}
	
	public BorderLayoutHelper(Container container) {
		this.mContainer = container;
		
		container.setLayout(new BorderLayout());
	}
	
	public BorderLayoutHelper addComponent(Component view,Object constraints) {
		mContainer.add(view,constraints);
		return this;
	}
	
}
