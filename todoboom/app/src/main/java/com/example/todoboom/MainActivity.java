package com.example.todoboom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;


import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Button commit;
    EditText task;
    //    TextView todoList;
    ArrayList<Task> todoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        task = (EditText) findViewById(R.id.curTask);
        commit = findViewById(R.id.commit);
        final Context myView = this;
        final RecyclerView myRecycler = (RecyclerView) findViewById(R.id.task_recycler);
        myRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));


        if (savedInstanceState != null) {
            onRestoreInstanceState(Objects.requireNonNull(savedInstanceState.getBundle("key")));
        }

        final TaskRecyclerViewAdapter myAdaptater = new TaskRecyclerViewAdapter(todoList);
        myRecycler.setAdapter(myAdaptater);

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editTextString = task.getText().toString();
                if(editTextString.isEmpty())
                {
                    Toast.makeText(myView, "You can't create an empty TODO item, oh silly !", Toast.LENGTH_LONG).show();
                }
                else{
                    task.getText().clear();
                    myAdaptater.addItem(new Task(editTextString));
                    myAdaptater.notifyItemInserted(myAdaptater.getItemCount()-1);

                }
            }
        });

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("key", new ArrayList<Task>(TaskRecyclerViewAdapter.getList()));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        assert savedInstanceState != null;
        this.todoList = savedInstanceState.getParcelableArrayList("key");
    }
}
