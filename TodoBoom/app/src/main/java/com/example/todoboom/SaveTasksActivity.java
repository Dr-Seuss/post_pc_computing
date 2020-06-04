//package com.example.todoboom;
//
//import android.app.Application;
//
//public class SaveTasksActivity extends Application {
//
//    TaskListHandler tdLst;
//
//    public void onCreate(){
//
//        super.onCreate();
//        tdLst = new TaskListHandler(this);
//
//    }
//
//}
package com.example.todoboom;
import android.app.Application;

import java.util.ArrayList;

public class SaveTasksActivity extends Application {
    TaskListHandler taskListHandler;
    public void onCreate(){
        super.onCreate();
        taskListHandler = new TaskListHandler();
    }

}
