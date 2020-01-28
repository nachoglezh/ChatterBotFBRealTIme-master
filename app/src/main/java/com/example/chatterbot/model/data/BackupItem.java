package com.example.chatterbot.model.data;

public class BackupItem {

    private String time;

    public BackupItem(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "BackupItem{" +
                "time='" + time + '\'' +
                '}';
    }
}
