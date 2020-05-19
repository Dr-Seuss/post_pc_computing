package com.example.todoboom;

import android.app.Application;

public class SaveTasksActivity extends Application {
     TaskListHandler taskList;

    public void onCreate() {
        super.onCreate();
        taskList = new TaskListHandler(this);
    }
}
