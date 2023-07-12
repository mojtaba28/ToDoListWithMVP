package com.example.todolistmvp.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.todolistmvp.R;
import com.example.todolistmvp.contract.MainContract;
import com.example.todolistmvp.databinding.ActivityMainBinding;
import com.example.todolistmvp.presenter.MainPresenter;
import com.example.todolistmvp.model.AppDatabase;
import com.example.todolistmvp.model.Task;
import com.example.todolistmvp.view.adapter.TaskAdapter;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


public class MainActivity extends AppCompatActivity implements MainContract.View, TaskAdapter.TaskItemEventListener {

    private static final int REQUEST_CODE = 430;
    public static final int RESULT_CODE_ADD_TASK = 1001;
    public static final int RESULT_CODE_DELETE_TASK = 1003;
    public static final int RESULT_CODE_UPDATE_TASK = 1002;
    public static final String EXTRA_KEY_TASK = "task";
    private MainContract.Presenter presenter;
    TaskAdapter taskAdapter;
    private View emptyState;
     View addNewTaskBtn,deleteAllBtn;
     RecyclerView recyclerView;
     EditText searchEt;
     ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        addNewTask();
        presenter.onAttach(this);
        setAdapter();
        deleteAll();
        search();


    }

    public void init(){
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        presenter =new MainPresenter(AppDatabase.getAppDatabase(this).getTaskDao());
        taskAdapter=new TaskAdapter(getApplicationContext(),this);
        emptyState=binding.emptyState;
        addNewTaskBtn=binding.addNewTaskBtn;
        deleteAllBtn=binding.deleteAllBtn;
        recyclerView=binding.taskListRv;
        searchEt=binding.searchEt;
    }

    private void addNewTask(){
        addNewTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, TaskDetailActivity.class), REQUEST_CODE);
            }
        });
    }

    private void setAdapter(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(taskAdapter);
    }

    private void deleteAll(){
        deleteAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onDeleteAllBtnClick();
            }
        });
    }

    private void search(){
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                presenter.onSearch(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    public void showTask(List<Task> tasks) {

        taskAdapter.setTasks(tasks);
    }

    @Override
    public void clearTask() {

        taskAdapter.clearItems();
    }

    @Override
    public void updateTask(Task task) {

        taskAdapter.updateItem(task);
    }

    @Override
    public void addTask() {

    }

    @Override
    public void deleteTask() {

    }

    @Override
    public void setEmptyStateVisibility(Boolean visibility) {

        emptyState.setVisibility(visibility?View.VISIBLE: View.GONE);
    }

    @Override
    public void onClick(Task task) {

        presenter.onTaskItemClick(task);
    }

    @Override
    public void onLongClick(Task task) {
        Intent intent=new Intent(this,TaskDetailActivity.class);
        intent.putExtra(EXTRA_KEY_TASK,task);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if ((resultCode == RESULT_CODE_ADD_TASK || resultCode == RESULT_CODE_UPDATE_TASK || resultCode == RESULT_CODE_DELETE_TASK) && data != null) {
                Task task = data.getParcelableExtra(EXTRA_KEY_TASK);
                if (task != null) {
                    if (resultCode == RESULT_CODE_ADD_TASK) {
                        taskAdapter.addItem(task);
                    } else if (resultCode == RESULT_CODE_UPDATE_TASK)
                        taskAdapter.updateItem(task);
                    else
                        taskAdapter.deleteItem(task);

                    setEmptyStateVisibility(taskAdapter.getItemCount() == 0);
                }
            }
        }
    }
}

