package andersonarmani.tickets;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.List;

import andersonarmani.tickets.db.AppDatabase;
import andersonarmani.tickets.db.FavoriteEvent;
import andersonarmani.tickets.http.TicketWebservice;
import andersonarmani.tickets.model.Event;
import andersonarmani.tickets.model.EventsRequest;
import andersonarmani.tickets.ui.ItemClickCallback;
import andersonarmani.tickets.ui.ListEventsAdapter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DefaultSubscriber;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventsActivity extends AppCompatActivity {
    private static final String TAG = EventsActivity.class.getSimpleName();
    private static final String API_KEY = "5ike7MSNlAAvxYKqXhSyNY324bnkkwld";
    private static final String BASE_URL = "https://app.ticketmaster.com/discovery/v2/";
    private static final int MIN_WIDTH = 639;
    private TicketWebservice mTicketWebservice;
    private RecyclerView mRecyclerView;
    private List<Event> mListEvents;
    private ListEventsAdapter mAdapter;
    private AppDatabase mAppDatabase;
    private List<String> mListFavoriteIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initDataBase();
        initRecyclerView();
        startWebserver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_favorites) {
            Intent intent = new Intent(EventsActivity.this, FavoritesActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * Data len is fixed in 50
         * in the future should be implemented a GET pagination with RxJava2
         * Could be implemente to addOnScrollListener to load more data
         */
        mTicketWebservice.getEvents(API_KEY, 50)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Database initialization and RxJava event listener
     */
    private void initDataBase() {
        if(mAppDatabase == null) {
            mAppDatabase = AppMain.getDataBase();
        }
        mAppDatabase.favoriteEventDao().getAllIds()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSubscriber());
    }

    /**
     * RxJava Subscriber for DataBase getAllIDs event
     * @return
     */
    private DefaultSubscriber<List<String>> getSubscriber() {
        return new DefaultSubscriber<List<String>>() {
            @Override
            public void onNext(List<String> favoriteEventsIds) {
                Log.d(TAG, "onNext");
                handleGetIDsResponse(favoriteEventsIds);
            }

            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        };
    }

    /**
     * Load list of favorite IDs
     * @param favoriteEventsIds
     */
    private void handleGetIDsResponse(List<String> favoriteEventsIds) {
        mListFavoriteIDs = new ArrayList<>(favoriteEventsIds);
    }

    /**
     * Initialize RecyclerView
     */
    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.events_list);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * Webserver initialize
     * It should be passed to ViewModel or Repository class
     */
    private void startWebserver() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mTicketWebservice = retrofit.create(TicketWebservice.class);
    }

    /**
     * Webserver
     * Should be implemented error handler
     * @return
     */
    private Observer<EventsRequest> getObserver() {
        return new Observer<EventsRequest>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(EventsRequest value) {
                Log.d(TAG, "onNext");
                handleResponse(value.getEmbedded().getEvents());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        };
    }

    /**
     *
     * @param eventList
     */
    private void handleResponse(List<Event> eventList) {
        mListEvents = new ArrayList<>(eventList);

        /**
         * Load favorites data into the Event list
         */
        if(mListFavoriteIDs != null) {
            for (Event event : mListEvents) {
                if (mListFavoriteIDs.contains(event.getId())) {
                    event.setFavorite(true);
                }
            }
        }

        mAdapter = new ListEventsAdapter(getBaseContext(),
                MIN_WIDTH,
                mListEvents, mItemClickCallback);
        mRecyclerView.setAdapter(mAdapter);
    }


    /**
     * Recyclerview item callback handler
     */
    private final ItemClickCallback mItemClickCallback = new ItemClickCallback() {
        @Override
        public void onClick(Event event, View v) {
            Log.d(TAG, "OnItemClicke Callback");

            if(mAppDatabase == null) {
                mAppDatabase = AppMain.getDataBase();
            }

            FavoriteEvent favoriteEvent = new FavoriteEvent(event.getId(), event.getName(),
                    event.getPromoter().getName(), event.getDates().getStart().getDateTime());

            if(event.isFavorite()) {
                addToFavorite(favoriteEvent);
            }
            else {
                removeFromFavorite(favoriteEvent);
            }
        }
    };

    /**
     * ADD item to favorite database
     * @param favoriteEvent
     */
    private void addToFavorite(FavoriteEvent favoriteEvent) {
        //Execute in background
        new AsyncTask<FavoriteEvent, Void, Void>() {

            @Override
            protected Void doInBackground(FavoriteEvent... params) {
                try {
                    FavoriteEvent favoriteEvent = params[0];
                    Long id = mAppDatabase.favoriteEventDao().insert(favoriteEvent);
                    favoriteEvent.setUid(id);
                } catch (SQLiteConstraintException e) {
                    Log.e(TAG, "Add Favorite" + e.getLocalizedMessage());
                }
                return null;
            }
        }.execute(favoriteEvent);
    }

    /**
     * Remove item from favorite database
     * @param favoriteEvent
     */
    private void removeFromFavorite(FavoriteEvent favoriteEvent) {
        //Execute in background
        new AsyncTask<FavoriteEvent, Void, Void>() {

            @Override
            protected Void doInBackground(FavoriteEvent... params) {
                try {
                    mAppDatabase.favoriteEventDao().deleteByID(params[0].getId());
                } catch (SQLiteConstraintException e) {
                    Log.e(TAG, "Delete Favorite" + e.getLocalizedMessage());
                }
                return null;
            }
        }.execute(favoriteEvent);
    }
}
