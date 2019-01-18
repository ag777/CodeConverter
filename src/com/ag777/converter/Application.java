package com.ag777.converter;

import com.ag777.converter.utils.ui.ViewUtils;
import com.ag777.converter.view.ConverterPanel;
import com.ag777.converter.view.MainFrame;
import com.ag777.converter.view.interf.MainView;

/**
 * 单例类,作用相当于安卓的同名类
 * @author ag777
 *	create time 2016/06/29
 */
public class Application {

	public static final Application mApp = new Application();
	
	private MainView mainFrame;
	
	private Application(){}
	
	public static Application getInstance(){
		return mApp;
	}
	
	public void setMainFrame(MainView mainFrame) {
		this.mainFrame = mainFrame;
	}
	
	public MainView getMainView() {
		return mainFrame;
	}
	
	
	public static void main(String[] args) {
		
		MainFrame mf = new MainFrame();
		mf.switchView(new ConverterPanel());
		getInstance().setMainFrame(mf);
		
		//放在屏幕中央
		ViewUtils.showInCenter(mf);
	}
	
}
