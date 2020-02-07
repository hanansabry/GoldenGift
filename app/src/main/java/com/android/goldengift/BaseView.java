package com.android.goldengift;

public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);

}
