package com.ag777.converter.view;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.ag777.converter.base.BasePanel;
import com.ag777.converter.utils.ConfigUtils;
import com.ag777.converter.utils.FileUtils;
import com.ag777.converter.utils.ui.DialogUtils;
import com.ag777.converter.utils.ui.GridBagLayoutHelper;
import com.ag777.converter.utils.ui.ViewUtils;
import com.ag777.converter.view.interf.QrcodeView;
import com.ag777.util.file.qrcode.QRCodeBuilder;
import com.ag777.util.lang.StringUtils;
import com.ag777.util.lang.img.ImageUtils;
import com.google.zxing.WriterException;

public class QrcodePanel extends BasePanel implements QrcodeView {

	private static final long serialVersionUID = 4128222596712686591L;

	private JButton btn_start;
	private JButton btn_toFile;
	private JTextArea ta_input;
	private JTextPane tp_output;
	
	@Override
	public void initView() {
		btn_start = new JButton("开始构建");
		btn_toFile = new JButton("保存到文件");
		
		ta_input = new JTextArea();
		tp_output = new JTextPane();
		
		//按钮组
		JPanel panel_btn_group = new JPanel();

		GridBagLayoutHelper.newInstance(panel_btn_group)
			.addComponent(btn_start, 0, 0)
			.addComponent(btn_toFile, 0, 1);
//		btn_start.setEnabled(false);
		btn_toFile.setEnabled(false);
		
		//输入框组
		//输入框组
		JPanel panel_ta_group = new JPanel();
		JScrollPane sp_input = new JScrollPane(ta_input);
			ta_input.setBorder(new TitledBorder(new EtchedBorder(), "输入二维码的内容"));
//			sp_input.add(ta_input);
		
		JScrollPane sp_output = new JScrollPane(tp_output);
			tp_output.setBorder(new TitledBorder(new EtchedBorder(), "输出"));
			tp_output.setEditable(false);
		
		GridBagLayoutHelper.newInstance(panel_ta_group)
			.addComponent(sp_input, 0, 0)
			.addComponent(sp_output, 1, 0);
		
		panel_ta_group.setSize(800,800);
		
		//整合
		setLayout(new BorderLayout());
		add(panel_ta_group,BorderLayout.CENTER);
		add(panel_btn_group,BorderLayout.SOUTH);
		
	}

	@Override
	public void initData() {
		btn_start.addActionListener(e->{
			String content = ta_input.getText().trim();
			try {
				BufferedImage img = new QRCodeBuilder().build(content, 400, 300);
				showResult(ImageUtils.toBytes(img));
				btn_toFile.setEnabled(true);
			} catch (WriterException | IOException e1) {
			}
		});
		
		btn_toFile.addActionListener(e->{
				
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
				/*截取前输入框前5个字(摘要)作为文件名的一部分*/
				String summary = ta_input.getText();
				if(summary.length()>5) {
					summary = summary.substring(0, 5);
				}
				summary = FileUtils.filenameFilter(summary);
				
				String savePath = StringUtils.concat(saveFile.getPath(),File.separator,"生成图片_", summary,".png");
				ImageIcon icon = ViewUtils.getImageFormTextPane(tp_output);
				ImageIO.write(ViewUtils.toBufferedImage(icon),ImageUtils.PNG,new File(savePath));
				DialogUtils.showMsgDialog(QrcodePanel.this, "系统提示", "保存成功");
			} catch (IOException e1) {
				e1.printStackTrace();
				DialogUtils.showMsgDialog(QrcodePanel.this, "系统提示", "未知异常");
			}
		});
	}

	
	
	@Override
	public void showResult(byte[] imgData) {
		SwingUtilities.invokeLater(()->{
			tp_output.setText("");
			tp_output.insertIcon(new ImageIcon(imgData));
//			StyledDocument doc = (StyledDocument) tp_output.getDocument();
//			 
//		    Style style = doc.addStyle("StyleName", null);
//		    StyleConstants.setIcon(style, new ImageIcon(imgData));
//		 
//		    try {
//				doc.insertString(doc.getLength(), "ignored text", style);
//				Dimension d = tp_output.getPreferredSize();
//				d.height = 500;
//				tp_output.setPreferredSize(d);
//			} catch (BadLocationException e) {
//				
//			}
		});
	}


}
