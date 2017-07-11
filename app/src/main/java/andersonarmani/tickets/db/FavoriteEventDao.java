package andersonarmani.tickets.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Armani andersonaramni@gmail.com on 10/07/2017.
 */

@Dao
public interface FavoriteEventDao {
    @Query("SELECT event_id FROM FavoriteEvent")
    Flowable<List<String>> getAllIds();

    @Query("SELECT * FROM FavoriteEvent ORDER BY uid DESC")
    Flowable<List<FavoriteEvent>> getAll();

    @Insert
    Long insert(FavoriteEvent favoriteEvent);

    @Delete
    void delete(FavoriteEvent favoriteEvent);

    @Query("DELETE FROM FavoriteEvent WHERE event_id IS(:favoriteID)")
    void deleteByID(String favoriteID);

    @Update
    void update(FavoriteEvent... favoriteEvents);
}
