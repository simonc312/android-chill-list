package com.example.simon.chillist;

/**
 * Model for an item that will be persisted in SqlLite
 * Created by Simon on 12/13/2015.
 */
public class Todo {
    private String text;
    private boolean isChecked;

    public Todo(String text){
        this.text = text;
        this.isChecked = false;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }
}
