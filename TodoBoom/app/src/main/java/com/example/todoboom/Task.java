package com.example.todoboom;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable { // parcelable class that let us create object class
    private String task;
    private boolean isDone;

    public Task(String task) {
        this.task = task;
        this.isDone = false;
    }

    protected Task(Parcel in) {
        task = in.readString();
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

    public boolean getTaskStatus() {
        return isDone;
    }

    public void setTaskStatus(boolean mark) {
        isDone = mark;
    }

    public String getDescription() {
        return task;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(task);
        dest.writeByte((byte) (isDone ? 1 : 0));
    }
}
