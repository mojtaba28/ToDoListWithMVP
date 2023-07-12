package com.example.todolistmvp.contract;

import com.example.todolistmvp.base.BasePresenter;
import com.example.todolistmvp.base.BaseView;
import com.example.todolistmvp.model.Task;

public interface TaskDetailContract {

    interface View extends BaseView{

        void showTask(Task task);
        void showError(String error);

        void setDeleteButtonVisibility(boolean visible);
        void returnResult(int resultCode,Task task);

    }

    interface Presenter extends BasePresenter<View>{

        void deleteTask();
        void saveChanges(int importance,String title);
    }
}
