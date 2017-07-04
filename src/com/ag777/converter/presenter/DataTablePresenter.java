package com.ag777.converter.presenter;

import com.ag777.converter.base.BasePresenter;
import com.ag777.converter.module.DataTableModuleFactory;
import com.ag777.converter.module.DataTableModuleImpl;
import com.ag777.converter.view.interf.ConverterView;

public class DataTablePresenter extends BasePresenter<ConverterView>{
	
	private DataTableModuleFactory mdataTable;

	public DataTablePresenter() {
		mdataTable = DataTableModuleImpl.getInstant();
	}
	
	@Override
	public void attachView(ConverterView container) {
		super.attachView(container);
	}
	
	@Override
	public void detachView() {
		super.detachView();
	}
	
	public void getDataTable(String input,String url) {
		getView().showResult(mdataTable.getDataTable(input,url));
	}
	
}
