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
    TextView content;
    Todo todo;
    SaveTasksActivity saveTasksActivity;

    //TODO: reset button -> set the task as notdone
    //TODO: delete button --> total delete --> including the DB
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compleated_task);

        this.content = (TextView) findViewById(R.id.curTodo);
        Button bResetTask = (Button) findViewById(R.id.reset_task);
        Button bDeleteTask = (Button) findViewById(R.id.delete_task);

        saveTasksActivity = (SaveTasksActivity) getApplicationContext();

        Intent intent = getIntent();
        final String taskID = intent.getStringExtra("todo");


        bResetTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Todo todo = new Todo(content.toString());
            }
        });

        bDeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CompletedTask.this);
                alertBuilder.setTitle("Delete Task");
                alertBuilder.setMessage("Are you sure you want to delete this Task?");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(CompletedTask.this, "The Task Deleted Successfully", Toast.LENGTH_SHORT).show();


                    }
                });
                alertBuilder.show();
            }
        });
    }


}
