package com.example.simon.chillist;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Simon on 12/16/2015.
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME = "ChilList_database";

    public static final int VERSION = 1;
}
