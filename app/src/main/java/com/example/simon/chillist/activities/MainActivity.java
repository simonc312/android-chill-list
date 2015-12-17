package com.example.simon.chillist.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.simon.chillist.R;
import com.example.simon.chillist.models.Todo;
import com.example.simon.chillist.adapters.TodoAdapter;
import com.example.simon.chillist.fragments.TodoDialogFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TodoDialogFragment.TodoListener {

    public final String EDIT_MODE_FILTER = "edit_mode";
    public final String DELETE_MODE_FILTER = "delete_mode";
    private TodoAdapter adapter;
    private ActionMode.Callback callback;
    private ActionMode actionMode;
    private TodoBroadcastReciever todoBroadcastReciever;

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
        todoBroadcastReciever = new TodoBroadcastReciever();

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(EDIT_MODE_FILTER);
        filter.addAction(DELETE_MODE_FILTER);
        LocalBroadcastManager.getInstance(this).registerReceiver(todoBroadcastReciever, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(todoBroadcastReciever);
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
        adapter.changeItem(position, data);
    }

    private void setUpRecyclerview(RecyclerView recyclerview){
        List<Todo> list = Todo.getAll();
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

                        int numDeleted = adapter.deleteCheckedItems();
                        showSnackBar(String.format(getString(R.string.number_todos_completed),numDeleted));

                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                actionMode = null;
                //if the back arrow icon was pressed
                adapter.unCheckAllItems();
            }

        };
    }

    private void showSnackBar(String message) {
        final Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), message, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.undo_action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                adapter.undoLastAction();
            }
        })
                .setActionTextColor(getResources().getColor(R.color.colorAccent))
                .show();
    }

    private class FabClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            createOrDismissTodo();
        }

    }

    private class TodoBroadcastReciever extends BroadcastReceiver{

        public final String POSITION_EXTRA = "position";
        public final String TEXT_EXTRA = "text";

        @Override
        public void onReceive(Context context, Intent intent) {
            switch(intent.getAction()){
                case EDIT_MODE_FILTER:
                    int pos = intent.getIntExtra(POSITION_EXTRA,-1);
                    String text = intent.getStringExtra(TEXT_EXTRA);
                    editOrDismissTodo(pos,text);
                    break;
                case DELETE_MODE_FILTER:
                    if(adapter.hasAnyCheckedItems())
                        startDeleteMode();
                    else
                        stopDeleteMode();
            }
        }

        private void startDeleteMode(){
            if(actionMode == null)
                actionMode = startSupportActionMode(callback);
        }

        private void stopDeleteMode(){
            if(actionMode != null) {
                actionMode.finish();
                actionMode = null;
            }
        }
    }

}
