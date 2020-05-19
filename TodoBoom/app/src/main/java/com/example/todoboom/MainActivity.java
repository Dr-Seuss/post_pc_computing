package com.example.todoboom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button commit;
    EditText task;
    ArrayList<Todo> todoList = new ArrayList<>();
    private String SIZE_TAG = Integer.valueOf(todoList.size()).toString();
    TaskRecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        task = (EditText) findViewById(R.id.curTodo);
        commit = findViewById(R.id.commit);
        final Context myView = this;
        final SaveTasksActivity saveTasksActivity = (SaveTasksActivity) getApplicationContext();
        Log.i(SIZE_TAG, "get task array size ");
        final RecyclerView recyclerView = findViewById(R.id.task_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (savedInstanceState != null) {
            todoList = savedInstanceState.getParcelableArrayList("key");
        }

        recyclerViewAdapter = new TaskRecyclerViewAdapter(todoList, saveTasksActivity);
        recyclerView.setAdapter(recyclerViewAdapter);
        saveTasksActivity.taskList.adapter = recyclerViewAdapter;

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editTextString = task.getText().toString();
                if (editTextString.isEmpty()) {
                    Toast.makeText(myView, "You can't create an empty TODO item, oh silly !", Toast.LENGTH_LONG).show();
                } else {
                    task.getText().clear();
                    Todo nTodo =new Todo(editTextString);
                    saveTasksActivity.taskList.addTodo(nTodo);
                    recyclerViewAdapter.setTaskList(todoList);
                    recyclerViewAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        final SaveTasksActivity app = (SaveTasksActivity) getApplicationContext();
        todoList = app.taskList.getTaskList();
        recyclerViewAdapter.setTaskList(todoList);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("key", new ArrayList<>(recyclerViewAdapter.getList()));
    }

    public void onClickCompletedTask(String id) {
        Intent intent = new Intent(this, CompletedTask.class);
        intent.putExtra("todo", id);
        startActivity(intent);
    }


    public void onClickUncompletedTask(String id) {
        Intent intent = new Intent(this, UncompletedTask.class);
        intent.putExtra("todo", id);
        startActivity(intent);
    }

    class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder> {

        private ArrayList<Todo> todoList;
        private static final String TAG = "MyActivity";
        private SaveTasksActivity saveActivity;

        void setTaskList(ArrayList<Todo> todoList) {
            this.todoList = todoList;
        }

        TaskRecyclerViewAdapter(ArrayList<Todo> myList, SaveTasksActivity saveActivity) {
            todoList = myList;
            this.saveActivity = saveActivity;
        }

        ArrayList<Todo> getList() {
            return todoList;
        }

        void addItem(Todo todo) {
            todoList.add(todo);
        }

        void removeItem(Todo todo) {
            todoList.remove(todo);
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final Context context = parent.getContext();
            final View myView = LayoutInflater.from(context).inflate(R.layout.item_single_task, parent, false);
            final ViewHolder holder = new ViewHolder(myView);

            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Todo cur = (todoList.get(holder.getAdapterPosition()));
                    if (cur.getTaskStatus()) {
                        onClickCompletedTask(cur.getId());
                    } else {
                        onClickUncompletedTask(cur.getId());
                    }
                }
            });
            myView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d(TAG, "START LONG CLICK");
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setTitle("Delete Task");
                    alertBuilder.setMessage("Are you sure you want to delete this Task?");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "Trying to Delete a task");
                            todoList.remove(todoList.get(holder.getAdapterPosition()));
                            notifyDataSetChanged();
                        }
                    });
                    alertBuilder.setNegativeButton(android.R.string.no, null);
                    alertBuilder.setIcon(android.R.drawable.ic_dialog_alert);
                    alertBuilder.show();
                    Log.d(TAG, "END LONG CLICK");
                    return true;
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Todo curTodo = todoList.get(position);
            holder.taskData.setText(curTodo.getDescription());
            ImageView myImg = holder.taskImg;
            if (curTodo.getTaskStatus()) {
                myImg.setImageResource(R.drawable.done_task_pic);
            } else {
                myImg.setImageResource(R.drawable.not_done_task);
            }
        }

        @Override
        public int getItemCount() {
            return todoList.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            final TextView taskData;
            final ImageView taskImg;
            Context context;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                taskData = itemView.findViewById(R.id.task_text);
                taskImg = itemView.findViewById(R.id.task_img);
            }

        }
    }
}
