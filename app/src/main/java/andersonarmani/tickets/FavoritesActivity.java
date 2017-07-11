package andersonarmani.tickets;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import andersonarmani.tickets.db.AppDatabase;
import andersonarmani.tickets.db.FavoriteEvent;
import andersonarmani.tickets.ui.ListFavoritesAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DefaultSubscriber;

public class FavoritesActivity extends AppCompatActivity {
    private static final String TAG = FavoritesActivity.class.getSimpleName();
    private AppDatabase         mAppDatabase = null;
    private List<FavoriteEvent> mListFavoriteEvents;
    private RecyclerView        mRecyclerView;
    private ListFavoritesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Shows the Return button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initRecyclerView();
        initDataBase();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * RecyclerView configuration
     *
     * Added SeparatorItem and SwipeAction
     */
    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.favorite_list);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(getItemTouchHelper());
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * Database initialization and RxJava event listener
     */
    private void initDataBase() {
        if(mAppDatabase == null) {
            mAppDatabase = AppMain.getDataBase();
        }
        mAppDatabase.favoriteEventDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSubscriber());
    }

    /**
     * RxJava Subscriber for DataBase getAll event
     * @return
     */
    private DefaultSubscriber<List<FavoriteEvent>> getSubscriber() {
        return new DefaultSubscriber<List<FavoriteEvent>>() {
            @Override
            public void onNext(List<FavoriteEvent> favoriteEvents) {
                Log.d(TAG, "onNext");
                handleResponse(favoriteEvents);
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
     * Method to handle dataBase responses populating the Recyclerview
     * @param listEvents
     */
    private void handleResponse(List<FavoriteEvent> listEvents) {
        Log.d(TAG, "handleResponse");

        if(mAdapter == null) {
            mListFavoriteEvents = new ArrayList<>(listEvents);
            mAdapter = new ListFavoritesAdapter(mListFavoriteEvents);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mListFavoriteEvents.clear();
            mListFavoriteEvents.addAll(listEvents);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * get item touch helper callback
     * @return
     */
    private ItemTouchHelper.Callback getItemTouchHelper() {
        return new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Log.d(TAG, "onSwiped");

                int position = viewHolder.getAdapterPosition();
                removeFavoriteItem(position);
            }
        };
    }

    /**
     * Delete one swiped item from the database
     * @param position
     */
    private void removeFavoriteItem(int position) {
        FavoriteEvent removeItem = mListFavoriteEvents.get(position);

        new AsyncTask<FavoriteEvent, Void, Void>() {
            @Override
            protected Void doInBackground(FavoriteEvent... params) {
                mAppDatabase.favoriteEventDao().delete(params[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mAdapter.notifyDataSetChanged();
            }
        }.execute(removeItem);
    }
}
