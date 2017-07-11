package andersonarmani.tickets;

import android.app.Application;

import andersonarmani.tickets.db.AppDatabase;

/**
 * Created by Armani andersonaramni@gmail.com on 10/07/2017.
 */

public class AppMain extends Application {
    private static AppDatabase appDatabase = null;
    private static AppMain instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static AppDatabase getDataBase() {
        if(appDatabase == null) {
            appDatabase = AppDatabase.getDatabase(instance);
        }
        return appDatabase;
    }
}
