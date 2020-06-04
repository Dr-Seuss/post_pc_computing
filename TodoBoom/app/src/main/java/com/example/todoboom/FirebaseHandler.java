package com.example.todoboom;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class FirebaseHandler {
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    ArrayList<Todo> myTodoList;
    HashMap<String, Todo> taskHashMap;

    public void writeTask(Todo todo){
        databaseReference.child("${todoItem.id}").setValue(todo);
    }
    public Todo getTaskFromDB(String id){
        return taskHashMap.get(id);
    }
    public void updateTask(Todo todo){
        databaseReference.child(todo.getId()).setValue(todo);

    }
    public void replaceTask(Todo todo){}
    public void addTask(Todo todo){}
}
