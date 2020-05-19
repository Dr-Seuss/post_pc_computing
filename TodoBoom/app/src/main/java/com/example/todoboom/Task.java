package com.example.todoboom;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;
import java.util.Objects;

//@Entity(tableName = "task_table")
public class Task implements Parcelable { // parcelable - let us create object class
//    @PrimaryKey
//    @NonNull
//    @ColumnInfo(name = "task_content")
    private String msg;
//    @ColumnInfo(name = "task_status")
    private boolean isDone;

    Task(@NonNull String msg) {
        this.msg = msg;
        this.isDone = false;
    }

    private Task(Parcel in) {
        msg = Objects.requireNonNull(in.readString());
        isDone = in.readByte() != 0;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    boolean getTaskStatus() {
        return isDone;
    }

    void setTaskStatus(boolean mark) {
        isDone = mark;
    }

    String getDescription() {
        return msg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(msg);
        dest.writeByte((byte) (isDone ? 1 : 0));
    }
}

//@Dao
//interface TaskDao {
//
//    // allowing the insert of the same word multiple times by passing a
//    // conflict resolution strategy
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    void insert(Task task);
//
//    @Query("DELETE FROM task_table")
//    void deleteAll();
//
//    @Query("SELECT * from task_table ORDER BY msg ASC")
//    List<Task> getAlphabetizedWords();
//}
