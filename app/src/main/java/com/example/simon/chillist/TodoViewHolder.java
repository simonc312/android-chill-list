package com.example.simon.chillist;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by Simon on 12/13/2015.
 */
public class TodoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private Todo todo;
    private TextView mainTextView;
    private CheckBox checkBox;

    public TodoViewHolder(View itemView) {
        super(itemView);
        mainTextView = (TextView) itemView.findViewById(R.id.tv_main_text);
        checkBox = (CheckBox) itemView.findViewById(R.id.cb_checkbox);
        checkBox.setOnClickListener(this);
        itemView.setOnClickListener(this);
    }

    @Override
    public String toString() {
        return todo.toString();
    }

    @Override
    public void onClick(View v) {
        // broadcast edit intent
        if(v.equals(itemView)){
            Intent intent = new Intent("edit_mode");
            intent.putExtra("position",this.getAdapterPosition());
            intent.putExtra("text",todo.getText());
            LocalBroadcastManager.getInstance(v.getContext()).sendBroadcast(intent);
        } //broadcast delete intent
        else{
            Intent intent = new Intent("delete_mode");
            LocalBroadcastManager.getInstance(v.getContext()).sendBroadcast(intent);
            todo.setChecked(checkBox.isChecked());
        }
    }

    public void setTodo(Todo todo){
        this.todo = todo;
        mainTextView.setText(todo.getText());
    }

    public Todo getTodo(){
        return todo;
    }


}
