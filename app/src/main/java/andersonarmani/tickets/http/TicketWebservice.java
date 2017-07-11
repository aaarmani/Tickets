package andersonarmani.tickets.http;

import andersonarmani.tickets.model.EventsRequest;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Armani andersonaramni@gmail.com on 10/07/2017.
 */

public interface TicketWebservice {
    @GET("events.json")
    Observable<EventsRequest> getEvents(
            @Query("apikey") String apikey,
            @Query("size") int size);
}
