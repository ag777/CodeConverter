package com.ag777.converter.presenter;

import com.ag777.converter.base.BasePresenter;
import com.ag777.converter.module.ConverterModuleFactory;
import com.ag777.converter.module.ConverterModuleImpl;
import com.ag777.converter.view.interf.ConverterView;

public class ConverterPresenter extends BasePresenter<ConverterView>{
	public static final byte TOJAVA = 1;
	public static final byte TOJS = 2;
	
	
	private ConverterModuleFactory mConverter;

	public ConverterPresenter() {
		mConverter = ConverterModuleImpl.getInstant();
	}
	
	@Override
	public void attachView(ConverterView container) {
		super.attachView(container);
	}
	
	@Override
	public void detachView() {
		super.detachView();
	}
	
	
	public void convert(String input,byte type) {
		String output = null;
		switch (type) {
		case TOJAVA:
			output = mConverter.convertToJava(input);
			if(isViewAttached()) {
				getView().showResult(output);
			}
			break;
		case TOJS:
			output = mConverter.convertToJs(input);
			if(isViewAttached()) {
				getView().showResult(output);
			}
			break;
		default:
			break;
		}
	}
}
