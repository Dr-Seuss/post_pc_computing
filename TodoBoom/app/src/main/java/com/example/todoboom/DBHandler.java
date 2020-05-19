package com.example.todoboom;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class DBHandler {

    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    HashMap<String, Todo> myHash = new HashMap<String, Todo>();


    public void addTodoToDB(Todo todo){
        DocumentReference myDoc = db.collection("todos").document();
        todo.setId(myDoc.getId());
        long time = System.currentTimeMillis();
        todo.setCreationTimestamp(time);
        todo.setEditTimestamp(time);
        myHash.put(todo.getId(), todo);
        myDoc.set(todo);
    }

    public void deleteTodoFromDB(Todo todo){
        String id = todo.getId();
        myHash.remove(todo.getId());
        db.collection("todos").document(id).delete();
    }


    public void editTodoInDB(Todo newTodo){
        db.collection("todos").document(newTodo.getId()).set(newTodo);
    }


    public Todo getTaskByIdFromDB(String id){
        return myHash.get(id);
    }

}