package com.example.simon.chillist;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Model for an item that will be persisted in SqlLite
 * Created by Simon on 12/13/2015.
 */
@Table(databaseName = AppDatabase.NAME)
public class Todo extends BaseModel{

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    private String text;
    @Column
    private boolean checked;

    public Todo(){
        this.checked = false;
    }

    public Todo(String text){
        this.text = text;
        this.checked = false;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public static Todo createAndSave(String text){
        Todo todo = new Todo(text);
        todo.save();
        return todo;
    }

    public static List<Todo> getAll() {
        return new Select().from(Todo.class).orderBy(false,"ID").queryList();
    }

}
