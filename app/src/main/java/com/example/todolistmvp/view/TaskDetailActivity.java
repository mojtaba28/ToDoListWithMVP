package com.example.todolistmvp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.todolistmvp.R;
import com.example.todolistmvp.databinding.ActivityTaskDetailBinding;
import com.example.todolistmvp.contract.TaskDetailContract;
import com.example.todolistmvp.presenter.TaskDetailPresenter;
import com.example.todolistmvp.model.AppDatabase;
import com.example.todolistmvp.model.Task;


public class TaskDetailActivity extends AppCompatActivity implements TaskDetailContract.View {

    private int selectedImportance = Task.IMPORTANCE_NORMAL;
    private ImageView lastSelectedImportanceIv;
    private TaskDetailContract.Presenter presenter;
    private EditText textEt;
    private View deleteButton,backBtn,
            normalImportanceBtn,
            highImportanceBtn,
            lowImportanceBtn;
    private Button saveChangeBtn;
    ActivityTaskDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        init();
        presenter.onAttach(this);
        onBackClick();
        deleteTask();
        saveChanges();
        onImportanceViewClick();
    }

    private void init(){

        binding= DataBindingUtil.setContentView(this,R.layout.activity_task_detail);
        presenter=new TaskDetailPresenter(AppDatabase.getAppDatabase(this)
                .getTaskDao(),getIntent().getParcelableExtra(MainActivity.EXTRA_KEY_TASK));

        backBtn=binding.backBtn;
        deleteButton=binding.deleteTaskBtn;
        textEt =binding.taskEt;
        saveChangeBtn=binding.saveChangesBtn;
        normalImportanceBtn=binding.normalImportanceBtn;
        highImportanceBtn=binding.highImportanceBtn;
        lowImportanceBtn=binding.lowImportanceBtn;
        lastSelectedImportanceIv = normalImportanceBtn.findViewById(R.id.normalImportanceCheckIv);



    }

    @Override
    public void showTask(Task task) {
        textEt.setText(task.getTitle());
        switch (task.getImportance()){
            case Task.IMPORTANCE_HIGH:
                highImportanceBtn.performClick();
                break;
            case Task.IMPORTANCE_NORMAL:
                normalImportanceBtn.performClick();
                break;
            case Task.IMPORTANCE_LOW:
                lowImportanceBtn.performClick();
                break;
        }

    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setDeleteButtonVisibility(boolean visible) {
        deleteButton.setVisibility(visible?View.VISIBLE:View.GONE);
    }

    @Override
    public void returnResult(int resultCode, Task task) {
        Intent intent=new Intent();
        intent.putExtra(MainActivity.EXTRA_KEY_TASK,task);
        setResult(resultCode,intent);
        finish();
    }

    private void onBackClick(){
        backBtn.setOnClickListener(v->{
            finish();
        });
    }

    private void deleteTask(){
        deleteButton.setOnClickListener(v->{
            presenter.deleteTask();
        });

    }

    private void saveChanges(){
        saveChangeBtn.setOnClickListener(v->{
            presenter.saveChanges(selectedImportance, textEt.getText().toString());
        });
    }

    private void onImportanceViewClick(){
        normalImportanceBtn.setOnClickListener(v->{
            if (selectedImportance != Task.IMPORTANCE_NORMAL) {
                lastSelectedImportanceIv.setImageResource(0);
                ImageView imageView = binding.normalImportanceCheckIv;
                imageView.setImageResource(R.drawable.ic_check_white_24dp);
                selectedImportance = Task.IMPORTANCE_NORMAL;

                lastSelectedImportanceIv = imageView;
            }

        });

            lowImportanceBtn.setOnClickListener(v->{
                if (selectedImportance != Task.IMPORTANCE_LOW) {
                    lastSelectedImportanceIv.setImageResource(0);
                    ImageView imageView = binding.lowImportanceCheckIv;
                    imageView.setImageResource(R.drawable.ic_check_white_24dp);
                    selectedImportance = Task.IMPORTANCE_LOW;

                    lastSelectedImportanceIv = imageView;
                }
            });

            highImportanceBtn.setOnClickListener(v->{
                if (selectedImportance != Task.IMPORTANCE_HIGH) {
                    lastSelectedImportanceIv.setImageResource(0);
                    ImageView imageView = binding.highImportanceCheckIv;
                    imageView.setImageResource(R.drawable.ic_check_white_24dp);
                    selectedImportance = Task.IMPORTANCE_HIGH;

                    lastSelectedImportanceIv = imageView;
                }
            });

    }
}