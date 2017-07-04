package com.ag777.converter.view.interf;

import com.ag777.converter.base.BaseView;

/**
 * 供presenter层访问view层的接口
 * @author ag777
 *
 */
public interface ConverterView extends BaseView{

	void showResult(String output);
}
