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

import com.example.todoboom.R;
import com.example.todoboom.Task;

import java.util.ArrayList;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder>{
    private static ArrayList<Task> taskList;
    public TaskRecyclerViewAdapter(ArrayList<Task> myList){
        taskList = myList;
    }

    public static ArrayList<Task> getList() {
        return taskList;
    }

    public void addItem(Task task){
        taskList.add(task);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context myContext = parent.getContext();
        View myView = LayoutInflater.from(myContext).inflate(R.layout.item_single_task, parent, false);
        final ViewHolder myHolder = new ViewHolder(myView);

        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( ! (taskList.get(myHolder.getAdapterPosition())).getTaskStatus()){
                    Toast.makeText(myContext,"TODO "+ taskList.get(myHolder.getAdapterPosition()).getDescription() + " is now DONE. BOOM!",Toast.LENGTH_SHORT).show();
                    ImageView myImg = myHolder.getMyImage();
                    myImg.setImageResource(R.drawable.not_done_task);
                    taskList.get(myHolder.getAdapterPosition()).setTaskStatus(true);
                }
            }
        });

        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task curTask = taskList.get(position);
        holder.getMyText().setText(curTask.getDescription());

        ImageView myImg = holder.getMyImage();
        if(curTask.getTaskStatus()){
            myImg.setImageResource(R.drawable.done_task_pic);
        }
        else{
            myImg.setImageResource(R.drawable.not_done_task);
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView myText;
        private ImageView myImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myText = itemView.findViewById(R.id.task_text);
            myImage = itemView.findViewById(R.id.task_img);
        }
        public ImageView getMyImage() {
            return myImage;
        }

        public TextView getMyText() {
            return myText;
        }

        public void setMyImage(ImageView myImage) {
            this.myImage = myImage;
        }

        public void setMyText(TextView myText) {
            this.myText = myText;
        }
    }


}
