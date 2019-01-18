package com.ag777.converter.view.interf;

import com.ag777.converter.base.BaseView;

public interface QrcodeView extends BaseView {
	void showResult(byte[] imgData);
}
