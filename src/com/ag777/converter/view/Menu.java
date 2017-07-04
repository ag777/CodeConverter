package com.ag777.converter.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.ag777.converter.base.BaseContainer;
import com.ag777.converter.utils.DialogUtils;

public class Menu extends JMenuBar implements ActionListener, BaseContainer{

	private MainFrame mContainer;
	
	private JMenu menu_tool = new JMenu("工具");
	private JMenuItem item_help = new JMenuItem("帮助");
	private JMenuItem item_about = new JMenuItem("关于");
	
		private JMenu menu_module = new JMenu("选择模块");
		private JMenuItem item_module_converter = new JMenuItem("html转换");
		private JMenuItem item_module_dataTable = new JMenuItem("表单构建");
		private JMenuItem item_module_jfinaldbbuilder = new JMenuItem("jfinal_bean生成");
	
	public Menu(MainFrame mainFrame) {
		super();
		
		mContainer = mainFrame;
		
		initView();
		
		initData();
	}
	
	@Override
	public void initView() {
		
		//二级
			menu_module.add(item_module_converter);
			menu_module.add(item_module_dataTable);
			menu_module.add(item_module_jfinaldbbuilder);
		menu_tool.add(menu_module);
		
		menu_tool.add(item_help);
		menu_tool.add(item_about);
		
		
		add(menu_tool);
	}

	@Override
	public void initData() {
		item_help.addActionListener(this);
		item_about.addActionListener(this);
		item_module_converter.addActionListener(this);
		item_module_dataTable.addActionListener(this);
		item_module_jfinaldbbuilder.addActionListener(this);
	}
	
	private void showHelp() {
		String basicMsg = "将html代码放到上面的输入框中，点击下面的转换按钮就能在下面的数据框中看到结果。\n结果自动复制到剪贴板";
		String datableMsg = "输入url,（点击post按钮获取json这个功能目前的公司的框架不支持login=false，暂时无用）\n"
				+ "输入jsonObject串点击【开始构建】按钮完成转换\n"
				+ "其他:\n"
				+ "①如果字段名包含'date(不区分大小写)'或包含'datetime(同上)'\n"
				+ "且字段类型为Long,则插入render方法";
		
		JPanel panel_help = new JPanel();
			JTextArea basic = new JTextArea(basicMsg);
			
			basic.setEditable(false);
			
			basic.setSize(700, 700);
			
			basic.setBorder(new TitledBorder(new EtchedBorder(), "基本操作"));
			panel_help.add(basic);
			
			
			JTextArea datatable = new JTextArea(datableMsg);
			
			datatable.setEditable(false);
			
			
			datatable.setSize(700, 700);
			
			datatable.setBorder(new TitledBorder(new EtchedBorder(), "表单构建"));
			panel_help.add(datatable);
		
			
			
		DialogUtils.showMsgDialog(mContainer, "帮助", panel_help,new Dimension(600, 240));
	}
	
	
	private void showAbout() {
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("版本", "1.1.0");
		map.put("编译环境", "JDK 1.6");
		map.put("作者", "王高瞻");

		DialogUtils.showMsgDialog(mContainer, "关于", map, new Dimension(80, 120));
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object v = e.getSource();
		
		if(v.equals(item_help)) {
			showHelp();
		}else if(v.equals(item_about)) {
			showAbout();
		}else if(v.equals(item_module_converter)) {
			mContainer.switchView(new ConverterPanel());
		}else if(v.equals(item_module_dataTable)) {
			mContainer.switchView(new DataTablePanel());
		}else if(v.equals(item_module_jfinaldbbuilder)) {
			mContainer.switchView(new JfinalDbBuilderPanel());
		}
		
	}

	
}
