package com.example.todoboom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {
    public static  final String EMPTY_STRING_ERR = "You can't create an empty TODO item, oh silly !";

    private EditText editText;
    ArrayList<Todo> lst = new ArrayList<>();
    Adapter adapter;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.editText = (EditText) findViewById(R.id.curTodo);
        Button button = (Button) findViewById(R.id.commit);

        final Context curView = this;
        final SaveTasksActivity saveTasksActivity = (SaveTasksActivity) getApplicationContext();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference colRef = db.collection("todos");

        final RecyclerView rv = (RecyclerView) findViewById(R.id.task_recycler);
        rv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        if (savedInstanceState != null) {
            editText.setText(savedInstanceState.getString("myText"));
        }

        adapter = new Adapter(lst, saveTasksActivity);
        rv.setAdapter(adapter);
        saveTasksActivity.taskListHandler.adapter = adapter;


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskInput = editText.getText().toString();
                if (taskInput.isEmpty()) {
                    Toast.makeText(curView, EMPTY_STRING_ERR, Toast.LENGTH_LONG).show();
                } else {
                    editText.getText().clear();
                    onClickCreateTask(saveTasksActivity, taskInput);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        final SaveTasksActivity saveTasksActivity = (SaveTasksActivity) getApplicationContext();
        lst = saveTasksActivity.taskListHandler.getDataFromDB();
        saveTasksActivity.taskListHandler.updateData();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("myText", editText.getText().toString());
        super.onSaveInstanceState(outState);
    }

    public void onClickCreateTask(SaveTasksActivity saveTasksActivity, String taskInput) {
        saveTasksActivity.taskListHandler.addTaskToDB(new Todo(taskInput, false));
        saveTasksActivity.taskListHandler.updateData();
    }

    public void onClickUncompletedTask(String id) {
        Intent intent = new Intent(this, UncompletedTask.class);
        intent.putExtra("todo", id);
        startActivity(intent);
    }

    public void onClickCompletedTask(String id) {
        Intent intent = new Intent(this, CompletedTask.class);
        intent.putExtra("todo", id);
        startActivity(intent);
    }

     class Adapter extends RecyclerView.Adapter<Adapter.Holder> {
        private ArrayList<Todo> taskList;

        Adapter(ArrayList<Todo> taskList, SaveTasksActivity act) {
            this.taskList = taskList;
        }

        ArrayList getTaskList() {
            return taskList;
        }

        void setTaskList(ArrayList<Todo> newLst) {
            taskList = newLst;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final Context myContext = parent.getContext();
            View rvView = LayoutInflater.from(myContext).inflate(R.layout.item_single_task, parent, false);
            final Holder rvHolder = new Holder(rvView);
            rvView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Todo cur = taskList.get(rvHolder.getAdapterPosition());
                    if (cur.getDone()) {
                        onClickCompletedTask(cur.getId());
                    } else {

                        onClickUncompletedTask(cur.getId());
                    }
                }
            });
            return rvHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            Todo myTodo = taskList.get(position);
            holder.getTextContent().setText(myTodo.getContent());
            ImageView myImg = holder.getTaskImage();
            if (myTodo.getDone()) {
                myImg.setImageResource(R.drawable.done_task_pic);
            } else {
                myImg.setImageResource(R.drawable.not_done_task);
            }
        }

        @Override
        public int getItemCount() {
            return getTaskList().size();
        }

        private class Holder extends RecyclerView.ViewHolder {
            private TextView textView;
            private ImageView taskImage;

            Holder(@NonNull View view) {
                super(view);
                textView = view.findViewById(R.id.task_text);
                taskImage = view.findViewById(R.id.task_img);
            }

            ImageView getTaskImage() {
                return taskImage;
            }

            TextView getTextContent() {
                return textView;
            }

        }

    }

}
