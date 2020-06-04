package com.example.todoboom;

public class Todo {

    private String content;
    private boolean isDone;
    private String id;
    private long createTime;
    private long editTime;

    public Todo(){}

    Todo(String todoItem, boolean isDone){
        this.content = todoItem;
        this.isDone = isDone;
    }

    public String getContent() {
        return content;
    }

    public boolean getDone(){
        return isDone;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getEditTime(){
        return editTime;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDone(boolean done) {
        this.isDone = done;
    }

    public void setCreateTime (long createTime){
        this.createTime = createTime;
    }

    public void setEditTime(long editTime) {
        this.editTime = editTime;
    }

    public String getId(){ return id;}

    public void setId(String id){ this.id = id;}
}
