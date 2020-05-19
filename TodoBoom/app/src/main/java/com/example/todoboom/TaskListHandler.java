package com.example.todoboom;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

class TaskListHandler {
    private SharedPreferences sp;
    private ArrayList<Task> lst;
    private Gson gson;

    TaskListHandler(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        lst = new ArrayList<>();
        gson = new Gson();
    }

    ArrayList<Task> restore() {
        String saved = sp.getString("SavedTasks", null);
        if (saved != null) {
            lst = gson.fromJson(saved, new TypeToken<Task>() {
            }.getType());
        }
        return lst;
    }

    void save(ArrayList<Task> tasks) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("SavedTasks", gson.toJson(tasks));
        editor.apply();
    }
}
