package com.example.todoboom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class UncompletedTask extends AppCompatActivity {
    private static final String NULL = "CHECK IF NULL" ;
    // todo: 1/ edit button  --> after the edit put alertdialog for the SaveState
// todo: 2/ done button
    TextView myContent;
    private EditText editText;
    TextView createDate;
    TextView EditDate;
    Todo myTodo;
    SaveTasksActivity saveTasksActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uncompleated_task);

        this.myContent = (TextView) findViewById(R.id.my_content);
        this.editText = (EditText) findViewById(R.id.editButton);
        this.createDate = (TextView) findViewById(R.id.create_date);
        this.EditDate = (TextView) findViewById(R.id.edit_date);
        Button bSaveState = (Button) findViewById(R.id.applyButton);
        Button bMarkDone = (Button) findViewById(R.id.done_button);

        saveTasksActivity = (SaveTasksActivity) getApplicationContext();

        Intent intent = getIntent();
        final String idTodo = intent.getStringExtra("todo");
        myTodo = saveTasksActivity.taskList.getTaskById(idTodo);
        myTodo.getContent();
//        myContent.setText(myTodo.getContent().toUpperCase());

        bSaveState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tells the change was made
                myTodo.setCreationTimestamp(System.currentTimeMillis());
                myTodo.setId(myTodo.getId());

                Date date = new Date(myTodo.getEditTimestamp());
                String dateStr = "Item edited at : " + date;
                EditDate.setText(dateStr);


                String newContent = editText.getText().toString();
                if (!newContent.isEmpty()) {
                    editText.getText().clear();
                    myContent.setText(newContent.toUpperCase());
                    myTodo.setContent(newContent);
                    myTodo.setCreationTimestamp(System.currentTimeMillis());
                    date = new Date(myTodo.getEditTimestamp());
                    dateStr = "Item edited at : " + date;
                    EditDate.setText(dateStr);
                    notifyAll();
                    Toast.makeText(UncompletedTask.this, "The Task have been saved Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bMarkDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTodo.setTaskStatus(true);
                Toast.makeText(UncompletedTask.this, "TODO " + myTodo.getContent() + " is now DONE. BOOM!", Toast.LENGTH_SHORT).show();
                notifyAll();
            }
        });
    }
}