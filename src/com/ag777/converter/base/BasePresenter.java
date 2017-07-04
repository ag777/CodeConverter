package com.ag777.converter.base;


public class BasePresenter<T extends BaseView> implements Presenter<T>{

	private T mContainer;
	
	public T getView() {
        return mContainer;
    }
	
	@Override
	public void attachView(T container) {
		mContainer = container;
    }

	@Override
	public void detachView() {
		mContainer = null;
	}
	
	public boolean isViewAttached() {
        return mContainer != null;
    }
	
	public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }
	
	
	public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("请先调用attachView(container)方法绑定视图" +
                    " requesting data to the Presenter");
        }
    }
}
