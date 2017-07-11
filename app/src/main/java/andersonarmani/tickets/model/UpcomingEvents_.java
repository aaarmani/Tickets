
package andersonarmani.tickets.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpcomingEvents_ {

    @SerializedName("_total")
    @Expose
    private Integer total;
    @SerializedName("tmr")
    @Expose
    private Integer tmr;
    @SerializedName("ticketmaster")
    @Expose
    private Integer ticketmaster;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTmr() {
        return tmr;
    }

    public void setTmr(Integer tmr) {
        this.tmr = tmr;
    }

    public Integer getTicketmaster() {
        return ticketmaster;
    }

    public void setTicketmaster(Integer ticketmaster) {
        this.ticketmaster = ticketmaster;
    }

}
