package andersonarmani.tickets.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by Armani andersonaramni@gmail.com on 10/07/2017.
 */

@Database(entities = {FavoriteEvent.class}, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "Launcher DB";
    private static AppDatabase INSTANCE;

    public abstract FavoriteEventDao favoriteEventDao();

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
