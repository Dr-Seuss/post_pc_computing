package com.example.todoboom;

import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder> {
    private static ArrayList<Task> taskList;

    TaskRecyclerViewAdapter(ArrayList<Task> myList) {
        taskList = myList;
    }

    static ArrayList<Task> getList() {
        return taskList;
    }

    void addItem(Task task) {
        taskList.add(task);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View myView = LayoutInflater.from(context).inflate(R.layout.item_single_task, parent, false);
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
