package com.example.todoboom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button commit;
    EditText task;
    TextView todoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        task = (EditText) findViewById(R.id.curTask);
        commit = findViewById(R.id.commit);
        todoList = findViewById(R.id.todoList);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cur = task.getText().toString(); // takes the strings from editTAsk
                todoList.setText(cur); // put it in TextView
                task.getText().clear(); // clear the task box
            }
        });
    }
}
