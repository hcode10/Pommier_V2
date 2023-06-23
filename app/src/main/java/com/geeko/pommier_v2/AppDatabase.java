package com.geeko.pommier_v2;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Emplacement.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance = null;

    public abstract bdd bdd();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = (AppDatabase) Room.databaseBuilder(context, AppDatabase.class, "bdd").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}