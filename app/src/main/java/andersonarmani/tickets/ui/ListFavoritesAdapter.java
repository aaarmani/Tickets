package andersonarmani.tickets.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import andersonarmani.tickets.R;
import andersonarmani.tickets.db.FavoriteEvent;

/**
 * Created by Armani andersonaramni@gmail.com on 10/07/2017.
 */

public class ListFavoritesAdapter extends RecyclerView.Adapter<ListFavoritesAdapter.ViewHolder> {
    private List<FavoriteEvent> mListFavoriteEvents;

    public ListFavoritesAdapter(List<FavoriteEvent> listFavorites) {
        mListFavoriteEvents = listFavorites;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_item_layout, parent, false);
        return new ListFavoritesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListFavoritesAdapter.ViewHolder holder, int position) {
        FavoriteEvent favoriteEvent = mListFavoriteEvents.get(position);

        holder.eventName.setText(favoriteEvent.getName());
        holder.brandName.setText(favoriteEvent.getBrand());
        holder.eventDates.setText(favoriteEvent.getDate());
    }

    @Override
    public int getItemCount() {
        return mListFavoriteEvents.size();
    }

    //##############################################################################################
    class ViewHolder extends RecyclerView.ViewHolder {
        final View       view;
        final TextView   eventName;
        final TextView   brandName;
        final TextView   eventDates;

        public ViewHolder(View itemView) {
            super(itemView);

            this.view = itemView;
            eventName = (TextView) view.findViewById(R.id.favorite_name);
            brandName = (TextView) view.findViewById(R.id.favorite_brand);
            eventDates = (TextView) view.findViewById(R.id.favorite_date);
        }
    }

}
