package net.penshorn.openevmap;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.util.Log;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by satri on 12/31/2017.
 */

@Dao
public interface EVPointDao
{
    @Query("SELECT * from evpoint")
    List<EVPoint> getAll();

    @Query("SELECT Count(*) from evpoint WHERE upload == 0")
    int getRowsNotUploaded();

    @Query("SELECT * from evpoint WHERE upload == 0 ORDER BY uid ASC LIMIT 1")
    EVPoint getNextToUpload();

    @Update
    void setUploadBit(EVPoint point);

    @Insert
    void insert(EVPoint evpoint);



}
