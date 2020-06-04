package com.example.todoboom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Date;

import static com.example.todoboom.MainActivity.EMPTY_STRING_ERR;

public class UncompletedTask extends AppCompatActivity {


    TextView taskContent;
    private EditText editText;
    private String myEdit;
    TextView createDate;
    TextView EditDate;
    Todo curTask;
    SaveTasksActivity saveTasksActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uncompleated_task);
        final String EDIT_MSG = "Task last edit ";
        final String CREATED_MSG = "Yoc Committed to the Task at ";
        this.taskContent = (TextView) findViewById(R.id.my_content);
        this.editText = (EditText) findViewById(R.id.editButton);
        this.createDate = (TextView) findViewById(R.id.create_date);
        this.EditDate = (TextView) findViewById(R.id.edit_date);
        Button bApplySave = (Button) findViewById(R.id.applyButton);
        Button bDone = (Button) findViewById(R.id.done_button);

        saveTasksActivity = (SaveTasksActivity) getApplicationContext();

        Intent intent = getIntent();
        final String idTodo = intent.getStringExtra("todo");

        curTask = saveTasksActivity.taskListHandler.getTaskByIdFromDB(idTodo);

        taskContent.setText(curTask.getContent());
        Date date = new Date(curTask.getCreateTime());
        String s = CREATED_MSG + date;
        createDate.setText(s);
        date = new Date(curTask.getEditTime());
        s = EDIT_MSG + date;
        EditDate.setText(s);

        bApplySave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curTask.setCreateTime(curTask.getCreateTime());
                curTask.setId(curTask.getId());

                Date date = new Date(curTask.getEditTime());
                String s = EDIT_MSG + date;
                EditDate.setText(s);

                String newContent = editText.getText().toString();
                if(newContent.isEmpty()){
                    Toast.makeText(UncompletedTask.this, EMPTY_STRING_ERR, Toast.LENGTH_LONG).show();
                }else{
                    editText.getText().clear();
                    taskContent.setText(newContent.toUpperCase());
                    curTask.setContent(newContent);
                    curTask.setEditTime(System.currentTimeMillis());
                    date = new Date(curTask.getEditTime());
                    s =  EDIT_MSG + date;
                    EditDate.setText(s);

                    saveTasksActivity.taskListHandler.editTaskInDB(curTask);
                    Toast.makeText(UncompletedTask.this, "The Task Have Been Edited Successfully",Toast.LENGTH_SHORT).show();
                }
                resumeToMainActivity();
            }
        });

        bDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curTask.setDone(true);
                curTask.setEditTime(System.currentTimeMillis());
                saveTasksActivity.taskListHandler.editTaskInDB(curTask);
                Toast.makeText(UncompletedTask.this, "You Completed" + curTask.getContent(),Toast.LENGTH_SHORT).show();
                resumeToMainActivity();
            }
        });

    }

    public void resumeToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
