package com.example.todoboom;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

class TaskListHandler {
    private ArrayList<Todo> lst;
    MainActivity.TaskRecyclerViewAdapter adapter;
    public DBHandler fb;

    TaskListHandler(Context context) {
        lst = new ArrayList<Todo>();
        Query();
        fb = new DBHandler();
    }

    void setTaskList(ArrayList<Todo> list) {
        lst = list;
    }

    ArrayList<Todo> getTaskList() {
        return lst;
    }

    Todo getTaskById(String id) {
        return fb.getTaskByIdFromDB(id);    }

    void addTodo(Todo todo) {
        fb.addTodoToDB(todo);
        lst.add(todo);
        adapter.setTaskList(lst);
        adapter.notifyDataSetChanged();

    }

    void deleteTodo(Todo todo) {
        fb.deleteTodoFromDB(todo);
        lst.remove(todo);
        adapter.setTaskList(lst);
        adapter.notifyDataSetChanged();
    }

    void editTodo(Todo newTodo) {

        for (int i = 0; i < lst.size(); i++) {

            Todo todo = lst.get(i);

            if (todo.getId().equals(newTodo.getId())) {

                lst.set(i, newTodo); // At the same place
                adapter.setTaskList(lst);
                adapter.notifyDataSetChanged();

                break;

            }
        }
        fb.editTodoInDB(newTodo);
    }


    private void Query() {
        // Get the instance of Cloud Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get the todos collection reference
        CollectionReference tdLstCollectionRef = db.collection("todos");

        // Listen for todos
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

                    // Update the list (restore)
                    lst.clear();

                    for (QueryDocumentSnapshot myDoc : queryDocumentSnapshots) {
                        Todo todo = myDoc.toObject(Todo.class);
                        lst.add(todo);
                    }

                    adapter.setTaskList(lst);
                    adapter.notifyDataSetChanged();

                    Log.d("Firestore", "Current data: " + queryDocumentSnapshots);

                }
            }
        });

        tdLstCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                adapter.setTaskList(lst);
                adapter.notifyDataSetChanged();
            }
        });

        tdLstCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                adapter.setTaskList(lst);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
