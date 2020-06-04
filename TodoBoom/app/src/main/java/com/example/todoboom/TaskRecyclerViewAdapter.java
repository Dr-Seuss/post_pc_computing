package com.example.todoboom;
 //TODO moved to the MainActivity easier activity management
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder> {
//    public void setTaskList(ArrayList<Todo> todoList) {
//        TaskRecyclerViewAdapter.todoList = todoList;
//    }
//
//    private static ArrayList<Todo> todoList;
//    private static final String TAG = "MyActivity";
//    private SaveTasksActivity saveActivity;
//
//    TaskRecyclerViewAdapter(ArrayList<Todo> myList, SaveTasksActivity saveActivity) {
//        todoList = myList;
//        this.saveActivity = saveActivity;
//    }
//
//    static ArrayList<Todo> getList() {
//        return todoList;
//    }
//
//    void addItem(Todo todo) {
//        todoList.add(todo);
//    }
//
//    void removeItem(Todo todo) {
//        todoList.remove(todo);
//    }
//
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        final Context context = parent.getContext();
//        final View myView = LayoutInflater.from(context).inflate(R.layout.item_single_task, parent, false);
//        final ViewHolder holder = new ViewHolder(myView);
//
//        myView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if ((todoList.get(holder.getAdapterPosition())).getTaskStatus()) {
//
//                    final Intent intent = new Intent(holder.context, CompletedTask.class);
//                    intent.putExtra("taskID", todoList.get(holder.getAdapterPosition()).getId());
//                    context.startActivity(intent);
//                } else {
//                    final Intent intent = new Intent(holder.context, UncompletedTask.class);
//                    intent.putExtra("taskID", todoList.get(holder.getAdapterPosition()).getId());
//                    context.startActivity(intent);
//                }
//            }
//        });
//        myView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Log.d(TAG, "START LONG CLICK");
//                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
//                alertBuilder.setTitle("Delete Task");
//                alertBuilder.setMessage("Are you sure you want to delete this Task?");
//                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Log.d(TAG, "Trying to Delete a task");
//                        todoList.remove(todoList.get(holder.getAdapterPosition()));
//                        notifyDataSetChanged();
////                        saveActivity.taskList.save(todoList);
////                        todoList = saveActivity.taskList.restore(); //todo check
//
//                    }
//                });
//                alertBuilder.setNegativeButton(android.R.string.no, null);
//                alertBuilder.setIcon(android.R.drawable.ic_dialog_alert);
//                alertBuilder.show();
//                Log.d(TAG, "END LONG CLICK");
//                return true;
//            }
//        });
//
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Todo curTodo = todoList.get(position);
//        holder.taskData.setText(curTodo.getDescription());
//        ImageView myImg = holder.taskImg;
//        if (curTodo.getTaskStatus()) {
//            myImg.setImageResource(R.drawable.done_task_pic);
//        } else {
//            myImg.setImageResource(R.drawable.not_done_task);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return todoList.size();
//    }
//
//    static class ViewHolder extends RecyclerView.ViewHolder {
//        final TextView taskData;
//        final ImageView taskImg;
//        Context context;
//
//        ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            taskData = itemView.findViewById(R.id.task_text);
//            taskImg = itemView.findViewById(R.id.task_img);
//        }
//
//    }
//}
