package com.ag777.converter.base;


/**
 * Every com.ag777.converter.presenter in the app must either implement this interface or extend BasePresenter
 * indicating the MvpView type that wants to be attached with.
 */
public interface Presenter<V extends BaseView> {

    void attachView(V container);	//建立view和precenter间的联系

    void detachView();				//断开view和precenter间的联系(android必须在view的生命周期结束时调用此方法避免空指针)
}
