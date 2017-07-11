package andersonarmani.tickets.ui;

import android.view.View;

import andersonarmani.tickets.model.Event;

/**
 * Created by Armani andersonaramni@gmail.com on 10/07/2017.
 */

public interface ItemClickCallback {
    void onClick(Event event, View v);
}
