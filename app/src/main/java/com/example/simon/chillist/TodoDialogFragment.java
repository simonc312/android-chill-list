package com.example.simon.chillist;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Simon on 12/13/2015.
 */
public class TodoDialogFragment extends DialogFragment{

    private static final String TEXT_EXTRA = "text_extra";
    private static final String TITLE_EXTRA = "title_extra";
    private static final String POSITION_EXTRA = "position_extra";
    private EditText mEditText;
    private TodoListener mListener;

    public static TodoDialogFragment newInstance(){
        TodoDialogFragment fragment = new TodoDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TITLE_EXTRA, R.string.create_todo_title);
        bundle.putInt(POSITION_EXTRA,0);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static TodoDialogFragment editInstance(int position, String text){
        TodoDialogFragment fragment = new TodoDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TITLE_EXTRA, R.string.edit_todo_item);
        bundle.putString(TEXT_EXTRA, text);
        bundle.putInt(POSITION_EXTRA,position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_todo, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEditText = (EditText) view.findViewById(R.id.et_create_todo);

        int title = getArguments().getInt(TITLE_EXTRA);
        final int position = getArguments().getInt(POSITION_EXTRA);
        String text = getArguments().getString(TEXT_EXTRA);
        getDialog().setTitle(title);
        if(text != null && !text.isEmpty())
            mEditText.setText(text);

        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        final Button cancelButton = (Button) view.findViewById(R.id.btn_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });

        final Button saveButton = (Button) view.findViewById(R.id.btn_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = getText();
                if (text != null && !text.isEmpty()) {
                    if(isEditMode())
                        mListener.onUpdate(text,position);
                    else
                        mListener.onAdd(text, position);

                    getDialog().dismiss();
                }

            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            mListener = (TodoListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement " + TodoListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach(){
        mListener = null;
        super.onDetach();
    }

    private String getText(){
        return mEditText.getText().toString();
    }

    private boolean isEditMode(){
        return getArguments().getInt(TITLE_EXTRA) == R.string.edit_todo_item;
    }

    interface TodoListener {
        void onAdd(String data, int position);
        void onUpdate(String data, int position);
    }
}
