package com.example.simon.chillist.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.simon.chillist.R;
import com.example.simon.chillist.viewholders.TodoViewHolder;
import com.example.simon.chillist.models.Todo;

import java.util.List;
import java.util.Stack;

/**
 * Created by Simon on 12/13/2015.
 */
public class TodoAdapter extends RecyclerView.Adapter<TodoViewHolder> {
    private List<Todo> mData;
    private Stack<Todo.Momento> backupDataStack;
    private Context mContext;

    public TodoAdapter(Context context, List<Todo> list){
        mData = list;
        backupDataStack = new Stack<>();
        mContext = context;
    }
    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.todo_list_item, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, int position) {
        Todo data = mData.get(position);
        holder.setTodo(data);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItem(int position, String data){
        mData.add(position, Todo.createAndSave(data));
        notifyItemInserted(position);
    }

    public void restoreItem(int position, Todo data){
        data.save();
        mData.add(position, data);
        notifyItemInserted(position);
    }

    public void deleteItem(int position){
        Todo todo = mData.remove(position);
        todo.delete();
        notifyItemRemoved(position);
    }

    public void changeItem(int position, String newItem){
        Todo todo = mData.get(position);
        todo.setText(newItem);
        todo.update();
        mData.set(position, todo);
        notifyItemChanged(position);
    }

    public int deleteCheckedItems() {
        backupDataStack.clear();
        int count = 0;
        for(int i=mData.size()-1; i >= 0; i--){
            Todo todo = mData.get(i);
            if(todo.getChecked()) {
                deleteItem(i);
                addToBackupStack(i, todo);
                count++;
            }
        }

        return count;
    }

    public void unCheckAllItems() {
        for(int i=mData.size()-1; i >= 0; i--){
            Todo todo = mData.get(i);
            if(todo.getChecked()) {
                todo.setChecked(false);
                todo.update();
                notifyItemChanged(i);
            }
        }
    }

    public boolean hasAnyCheckedItems() {
        for(Todo t : mData){
            if(t.getChecked())
                return true;
        }
        return false;
    }

    public void undoLastAction() {
        while(!backupDataStack.isEmpty()){
            Todo.Momento momento = backupDataStack.pop();
            restoreItem(momento.getPosition(), momento.getTodo());
        }

    }

    private void addToBackupStack(int position, Todo todo) {
        backupDataStack.push(new Todo.Momento(position, todo));
    }
}
