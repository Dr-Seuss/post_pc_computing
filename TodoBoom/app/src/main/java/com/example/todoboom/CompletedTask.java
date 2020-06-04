package com.example.todoboom;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CompletedTask extends AppCompatActivity {

    TextView taskContent;
    Todo curTodo;
    SaveTasksActivity saveTasksActivity;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compleated_task);

        this.taskContent = (TextView) findViewById(R.id.edit_content);
        Button bReset = (Button) findViewById(R.id.reset_task);
        Button bDeleteTask = (Button) findViewById(R.id.delete_task);

        saveTasksActivity = (SaveTasksActivity) getApplicationContext();

        Intent intent = getIntent();
        final String idTodo = intent.getStringExtra("todo");
        curTodo = saveTasksActivity.taskListHandler.getTaskByIdFromDB(idTodo);
        taskContent.setText(curTodo.getContent().toUpperCase());

        bReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickResetTask();
                resumeToMainActivity();
            }
        });

        bDeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDeleteTask();
                resumeToMainActivity();
            }
        });
    }

    public void onClickResetTask() {
        curTodo.setDone(false);
        saveTasksActivity.taskListHandler.editTaskInDB(curTodo);
        Toast.makeText(CompletedTask.this, curTodo.getContent() + " been RESET !", Toast.LENGTH_SHORT).show();
    }

    public void onClickDeleteTask() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CompletedTask.this);
        alertBuilder.setTitle("Delete Task");
        alertBuilder.setMessage("Are you sure you want to delete this Task?");
        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                saveTasksActivity.taskListHandler.deleteTaskFromDB(curTodo);
                Toast.makeText(CompletedTask.this, "The Task Deleted Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        alertBuilder.show();
    }
    public void resumeToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
