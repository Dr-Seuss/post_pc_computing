package com.example.todoboom;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

class TaskListHandler {

    private ArrayList<Todo> data;
    private DBHandler db;
    MainActivity.Adapter adapter;
    TaskListHandler() {
        data = new ArrayList<>();
        Query();
        db = new DBHandler();

    }
    public void updateData() {
        adapter.setTaskList(data);
        adapter.notifyDataSetChanged();
    }
    ArrayList<Todo> getDataFromDB() {
        return data;
    }


    void addTaskToDB(Todo todo) {
        db.addTodo(todo);
        data.add(todo);
        updateData();

    }

    void deleteTaskFromDB(Todo todo) {
        db.deleteTodoForever(todo);
        data.remove(todo);
        updateData();

    }

    void editTaskInDB(Todo todo) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(todo.getId())) {
                data.set(i, todo);
                updateData();
                break;
            }
        }
        db.editTodo(todo);
    }

    Todo getTaskByIdFromDB(String id) {
        return db.getTodoById(id);
    }



    private void Query() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference tdLstCollectionRef = db.collection("todos");
        tdLstCollectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Firestore", "Listen failed.", e);
                    return;
                }
                if (queryDocumentSnapshots == null) {
                    Log.d("Firestore", "Current data: null");
                } else {
                    data.clear();
                    for (QueryDocumentSnapshot myDoc : queryDocumentSnapshots) {
                        Todo todo = myDoc.toObject(Todo.class);
                        data.add(todo);
                        TaskListHandler.this.db.hash.put(todo.getId(), todo);
                    }
                    updateData();
                    Log.d("Firestore", "Current data: " + queryDocumentSnapshots);
                }
            }
        });

        tdLstCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                updateData();

            }
        });

        tdLstCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                updateData();

            }
        });
    }

}
