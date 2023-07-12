package com.example.todolistmvp.base;

import com.example.todolistmvp.base.BaseView;

public interface BasePresenter<T extends BaseView> {
    void onAttach(T view);

    void onDetach();
}
