package com.example.simon.chillist;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TodoDialogFragment.TodoListener {

    private TodoAdapter adapter;
    private ActionMode.Callback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        RecyclerView recyclerview = (RecyclerView) findViewById(R.id.rv_todo_list);
        setSupportActionBar(toolbar);
        setUpRecyclerview(recyclerview);
        setUpActionModeCallback();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new FabClickListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAdd(String data, int position) {
        adapter.addItem(position,data);
    }

    @Override
    public void onUpdate(String data, int position) {
        adapter.changeItem(position,data);
    }

    private void setUpRecyclerview(RecyclerView recyclerview){
        List<Todo> list = new ArrayList<>();
        list.add(new Todo("Netflix and chill"));
        list.add(new Todo("Netflix and chill again"));
        list.add(new Todo("Netflix and chill even more"));
        adapter = new TodoAdapter(this,list);
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    private void createOrDismissTodo(){
        android.support.v4.app.Fragment fragment = getSupportFragmentManager().findFragmentByTag("create");
        if(fragment == null)
            TodoDialogFragment.newInstance().show(getSupportFragmentManager(),"create");
        else
            ((DialogFragment)fragment).dismiss();
    }

    private void editOrDismissTodo(int position, String text){
        android.support.v4.app.Fragment fragment = getSupportFragmentManager().findFragmentByTag("edit");
        if(fragment == null)
            TodoDialogFragment.editInstance(position, text).show(getSupportFragmentManager(),"edit");
        else
            ((DialogFragment)fragment).dismiss();
    }

    private void setUpActionModeCallback() {
        this.callback = new ActionMode.Callback(){
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.delete_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_done:
                        adapter.deleteCheckedItems();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                Log.d("action menu","destroy");
            }

        };
    }

    private class FabClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            createOrDismissTodo();
        }

    }

}