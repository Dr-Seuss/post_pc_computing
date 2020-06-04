package com.example.todoboom;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Todo implements Parcelable {
    private String content;
    private long creationTimestamp;
    private long editTimestamp;
    private String id;
    private boolean isDone;

    Todo() {
    }

    Todo(String content) {
        this.content = content;
        creationTimestamp = System.currentTimeMillis();
        editTimestamp = creationTimestamp;
        this.isDone = false;
    }

    private Todo(Parcel in) {
        content = Objects.requireNonNull(in.readString());
        isDone = in.readByte() != 0;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public long getEditTimestamp() {
        return editTimestamp;
    }

    public String getContent() {
        return content;
    }

    public void setEditTimestamp(long editTimestamp) {
        this.editTimestamp = editTimestamp;
    }

    public static final Creator<Todo> CREATOR = new Creator<Todo>() {
        @Override
        public Todo createFromParcel(Parcel in) {
            return new Todo(in);
        }

        @Override
        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };

    boolean getTaskStatus() {
        return isDone;
    }

    void setTaskStatus(boolean mark) {
        isDone = mark;
    }

    String getDescription() {
        return content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeByte((byte) (isDone ? 1 : 0));
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

}

//@Dao
//interface TaskDao {
//
//    // allowing the insert of the same word multiple times by passing a
//    // conflict resolution strategy
////    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    void insert(Task task);
//
////    @Query("DELETE FROM task_table")
//    void deleteAll();
//
////    @Query("SELECT * from task_table ORDER BY msg ASC")
//    List<Task> getAlphabetizedWords();
//    HashMap<String, Task> myHash = new HashMap<String, Task>();
//
//}
