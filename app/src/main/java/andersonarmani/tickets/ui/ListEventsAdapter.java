package andersonarmani.tickets.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import andersonarmani.tickets.R;
import andersonarmani.tickets.model.Event;
import andersonarmani.tickets.model.Image;
import andersonarmani.tickets.utils;

/**
 * Created by Armani andersonaramni@gmail.com on 10/07/2017.
 */

public class ListEventsAdapter extends RecyclerView.Adapter <ListEventsAdapter.ViewHolder> {
    private static final String TAG = ListEventsAdapter.class.getSimpleName();
    private Context     mContext;
    private List<Event> mListEvents;
    private ItemClickCallback mItemClickCallback;
    private int         mMinWidth = 639; //CHANGE


    public ListEventsAdapter(Context context, int minWidth, List<Event> listEvents, @Nullable ItemClickCallback itemClickCallback) {
        mContext = context;
        mListEvents = listEvents;
        mItemClickCallback = itemClickCallback;
        mMinWidth = minWidth;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item_layout, parent, false);
        return new ListEventsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListEventsAdapter.ViewHolder holder, int position) {
        Event event = mListEvents.get(position);

        holder.eventName.setText(event.getName());
        holder.brandName.setText(event.getPromoter().getName());
        holder.eventDates.setText(utils.getFriendlyDateString(event.getDates()));
        setFavoriteImage(event, holder);
        loadImage(holder.eventImage, event.getImages());

        if(mItemClickCallback != null) {
            holder.itemView.setOnClickListener(getItemClickListener(event, holder));
        }

    }

    @Override
    public int getItemCount() {
        return mListEvents.size();
    }

    /**
     * Receive click event and call clickCallback
     * @param event
     * @param holder
     * @return
     */
    private View.OnClickListener getItemClickListener(final Event event, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.setFavorite(!event.isFavorite());
                setFavoriteImage(event, holder);
                mItemClickCallback.onClick(event, holder.view);
            }
        };
    }

    /**
     * Set favorite icon
     * @param event
     * @param holder
     */
    private void setFavoriteImage(Event event, ViewHolder holder) {
        ImageView imageView = holder.eventFavoriteImage;
        Drawable drawable;

        if(event.isFavorite()) {
            drawable = mContext.getResources().getDrawable(R.drawable.favorite_full, null);
        }
        else {
            drawable = mContext.getResources().getDrawable(R.drawable.favorite_border, null);
        }

        imageView.setImageDrawable(drawable);
    }

    /**
     * Load Item Image
     * @param eventImage
     * @param images
     */
    private void loadImage(ImageView eventImage, List<Image> images){
        for(Image img: images) {
            if(img.getRatio().equals(Image.RATIO_16_9) && img.getWidth() > mMinWidth) {
                try {
                    Picasso.with(mContext).load(img.getUrl())
                            //.resizeDimen(R.dimen.listitem_image_size, R.dimen.listitem_image_size)
                            //.onlyScaleDown()
                            .into(eventImage);
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
                break;
            }
        }
    }

    //##############################################################################################
    class ViewHolder extends RecyclerView.ViewHolder {
        final View       view;
        final ImageView  eventImage;
        final ImageView  eventFavoriteImage;
        final TextView   eventName;
        final TextView   brandName;
        final TextView   eventDates;

        ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.eventImage = (ImageView) view.findViewById(R.id.item_image);
            this.eventFavoriteImage = (ImageView) view.findViewById(R.id.item_image_favorite);
            this.eventName = (TextView) view.findViewById(R.id.item_title);
            this.brandName = (TextView) view.findViewById(R.id.item_brand);
            this.eventDates = (TextView) view.findViewById(R.id.item_dates);
        }
    }
}