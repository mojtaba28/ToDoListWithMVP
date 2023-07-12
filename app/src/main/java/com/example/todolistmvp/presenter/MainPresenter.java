package com.example.todolistmvp.presenter;

import com.example.todolistmvp.contract.MainContract;
import com.example.todolistmvp.model.Task;
import com.example.todolistmvp.model.TaskDao;

import java.util.List;

public class MainPresenter implements MainContract.Presenter {
    private TaskDao taskDao;
    private List<Task> tasks;
    private MainContract.View view;

    public MainPresenter(TaskDao taskDao){
        this.taskDao=taskDao;
        this.tasks=taskDao.getAll();

    }
    @Override
    public void onAttach(MainContract.View view) {

        this.view=view;
        if (!tasks.isEmpty()){
            view.showTask(tasks);
            view.setEmptyStateVisibility(false);
        }else {
            view.setEmptyStateVisibility(true);
        }


    }

    @Override
    public void onDetach() {

    }

    @Override
    public void onDeleteAllBtnClick() {
        taskDao.deleteAll();
        view.clearTask();
        view.setEmptyStateVisibility(true);
    }

    @Override
    public void onSearch(String q) {
        if (!q.isEmpty()){
            List<Task> tasks=taskDao.search(q);
            view.showTask(tasks);
        }else {
            List<Task> tasks=taskDao.getAll();
            view.showTask(tasks);
        }
    }

    @Override
    public void onTaskItemClick(Task task) {

        task.setCompleted(!task.isCompleted());
        int result=taskDao.update(task);
        if (result>0){
            view.updateTask(task);
        }
    }
}
