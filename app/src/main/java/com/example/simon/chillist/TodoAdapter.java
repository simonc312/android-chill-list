package com.example.simon.chillist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Simon on 12/13/2015.
 */
public class TodoAdapter extends RecyclerView.Adapter<TodoViewHolder> {
    private List<Todo> mData;
    private Context mContext;

    public TodoAdapter(Context context, List<Todo> list){
        mData = list;
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
        mData.add(position, new Todo(data));
        notifyItemInserted(position);
    }

    public void deleteItem(int position){
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public void changeItem(int position, String newItem){
        Todo todo = mData.get(position);
        todo.setText(newItem);
        mData.set(position, todo);
        notifyItemChanged(position);
    }

    /**
     * Uncheck all checked items and delete them too
     */
    public void deleteCheckedItems() {
        for(int i=mData.size()-1; i >= 0; i--){
            if(mData.get(i).isChecked()) {
                deleteItem(i);
            }
        }
    }

    public boolean hasAnyCheckedItems() {
        for(Todo t : mData){
            if(t.isChecked())
                return true;
        }
        return false;
    }
}
