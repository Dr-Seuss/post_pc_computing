package com.example.todoboom;

import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder> {
    private static ArrayList<Task> taskList;
    private static final String TAG = "MyActivity";
    private SaveTasksActivity saveActivity;

    TaskRecyclerViewAdapter(ArrayList<Task> myList, SaveTasksActivity saveActivity) {
        taskList = myList;
        this.saveActivity = saveActivity;
    }

    static ArrayList<Task> getList() {
        return taskList;
    }

    void addItem(Task task) {
        taskList.add(task);
    }

    void removeItem(Task task) {
        taskList.remove(task);
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
                if (!(taskList.get(holder.getAdapterPosition())).getTaskStatus()) {
                    Toast.makeText(context, "TODO " + taskList.get(holder.getAdapterPosition()).getDescription() + " is now DONE. BOOM!", Toast.LENGTH_SHORT).show();
                    ImageView myImg = holder.taskImg;
                    myImg.setImageResource(R.drawable.done_task_pic);
                    taskList.get(holder.getAdapterPosition()).setTaskStatus(true);
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
                        taskList.remove(taskList.get(holder.getAdapterPosition()));
                        notifyDataSetChanged();
                        saveActivity.taskList.save(taskList);
                        taskList = saveActivity.taskList.restore(); //todo check

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
        Task curTask = taskList.get(position);
        holder.taskData.setText(curTask.getDescription());
        ImageView myImg = holder.taskImg;
        if (curTask.getTaskStatus()) {
            myImg.setImageResource(R.drawable.done_task_pic);
        } else {
            myImg.setImageResource(R.drawable.not_done_task);
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView taskData;
        final ImageView taskImg;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskData = itemView.findViewById(R.id.task_text);
            taskImg = itemView.findViewById(R.id.task_img);
        }

    }
}
