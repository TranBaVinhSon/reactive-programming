package com.example.sontbv.roomexample;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = { Repo.class, User.class }, version = 1)
public abstract class RepoDatabase extends RoomDatabase {

    private static final String DB_NAME = "repoDatabase.db";
    private static volatile RepoDatabase instance;

    static synchronized RepoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static RepoDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                RepoDatabase.class,
                DB_NAME).build();
    }

    public abstract RepoDao getRepoDao();
    public abstract UserDao getUserDao();
}
