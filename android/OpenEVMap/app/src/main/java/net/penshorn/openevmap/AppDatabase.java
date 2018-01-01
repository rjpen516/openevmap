package net.penshorn.openevmap;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {EVPoint.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EVPointDao evpointdao();
}