package com.example.todolistmvp.contract;


import com.example.todolistmvp.base.BasePresenter;
import com.example.todolistmvp.base.BaseView;
import com.example.todolistmvp.model.Task;

import java.util.List;

public interface MainContract {

    interface View extends BaseView{
        void showTask(List<Task> tasks);

        void clearTask();

        void updateTask(Task task);

        void addTask();

        void deleteTask();

        void setEmptyStateVisibility(Boolean visibility);

    }

    interface Presenter extends BasePresenter<View> {

        void onDeleteAllBtnClick();

        void onSearch(String q);

        void onTaskItemClick(Task task);


    }
}
