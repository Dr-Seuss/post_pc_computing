package com.example.todoboom;
import android.app.Application;

public class SaveTasksActivity extends Application {
    TaskListHandler taskListHandler;
    public void onCreate(){
        super.onCreate();
        taskListHandler = new TaskListHandler();
    }

}
