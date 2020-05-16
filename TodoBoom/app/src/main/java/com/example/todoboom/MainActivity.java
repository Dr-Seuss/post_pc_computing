package com.example.todoboom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;


import android.os.PersistableBundle;
import android.util.Log;
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
    private static final String TAG = "MyActivity";
    private  String SIZE_TAG = Integer.valueOf(todoList.size()).toString();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(SIZE_TAG, "get task array size ");
        setContentView(R.layout.activity_main);
        task = (EditText) findViewById(R.id.curTask);
        commit = findViewById(R.id.commit);
        final Context myView = this;
        final RecyclerView recyclerView = findViewById(R.id.task_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (savedInstanceState != null) {
            todoList = savedInstanceState.getParcelableArrayList("key");
        }

        final TaskRecyclerViewAdapter adapter = new TaskRecyclerViewAdapter(todoList);
        recyclerView.setAdapter(adapter);

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editTextString = task.getText().toString();
                if (editTextString.isEmpty()) {
                    Toast.makeText(myView, "You can't create an empty TODO item, oh silly !", Toast.LENGTH_LONG).show();
                } else {
                    task.getText().clear();
                    adapter.addItem(new Task(editTextString));
                    adapter.notifyItemInserted(adapter.getItemCount() - 1);
                }
            }
        });



    }



    private AlertDialog AskOption() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (!todoList.isEmpty()) {
                            todoList.remove(todoList.get(0));
                        }
                        //your deleting code
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();

        return myQuittingDialogBox;
    }
//    using it will be:


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("key", new ArrayList<Task>(TaskRecyclerViewAdapter.getList()));
        super.onSaveInstanceState(outState);
    }
}
