package com.example.todoboom;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class DBHandler {

    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    HashMap<String, Todo> hash = new HashMap<String, Todo>();


    public void addTodo(Todo todo){
        DocumentReference myDoc = db.collection("todos").document();
        todo.setId(myDoc.getId());
        long time = System.currentTimeMillis();
        todo.setCreateTime(time);
        todo.setEditTime(time);
        hash.put(todo.getId(), todo);
        myDoc.set(todo);
    }

    public void deleteTodoForever(Todo todo){
        String id = todo.getId();
        hash.remove(todo.getId());
        db.collection("todos").document(id).delete();
    }


    public void editTodo(Todo newTodo){
        db.collection("todos").document(newTodo.getId()).set(newTodo);
    }


    public Todo getTodoById(String id){
        return hash.get(id);
    }

}
