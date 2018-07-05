package com.example.shivamkumar.todolist;

public class ToDo {
    private String topic;
    private String note;
    private int position;
    private long id;
    private String time;
    private String timeSet;

    public String getTimeSet() {
        return timeSet;
    }

    public void setTimeSet(String timeSet) {
        this.timeSet = timeSet;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ToDo(String topic, String note) {
        this.topic = topic;
        this.note = note;
    }

    public String getTopic() {

        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
