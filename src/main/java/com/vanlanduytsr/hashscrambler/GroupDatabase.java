package com.vanlanduytsr.hashscrambler;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Group.class}, version = 3, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class GroupDatabase extends RoomDatabase {

    public abstract GroupDao groupDao();

    public static volatile GroupDatabase INSTANCE;
    public static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriterExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static GroupDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GroupDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GroupDatabase.class, "group_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}