package andersonarmani.tickets;

import andersonarmani.tickets.model.Dates;

/**
 * Created by Armani andersonaramni@gmail.com on 10/07/2017.
 */

public final class utils {
    public static String getFriendlyDateString(Dates dates){
        return dates.getStart().getDateTime();
    }
}
