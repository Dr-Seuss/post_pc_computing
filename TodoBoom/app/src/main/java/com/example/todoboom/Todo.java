package com.example.todoboom;

public class Todo {

    private String content;
    private boolean isDone;
    private String id;
    private long createTime;
    private long editTime;


    // But why ?
    public Todo(){}


    Todo(String todoItem, boolean isDone){
        this.content = todoItem;
        this.isDone = isDone;
    }

    // Getters

    public String getContent() {
        return content;
    }

    public boolean getDone(){
        return isDone;
    }

    public String getId(){ return id;}

    public long getCreateTime() {
        return createTime;
    }

    public long getEditTime(){
        return editTime;
    }

    // Setters

    public void setContent(String content) {
        this.content = content;
    }

    public void setDone(boolean done) {
        this.isDone = done;
    }

    public void setId(String id){ this.id = id;}

    public void setCreateTime (long createTime){
        this.createTime = createTime;
    }

    public void setEditTime(long editTime) {
        this.editTime = editTime;
    }
}
