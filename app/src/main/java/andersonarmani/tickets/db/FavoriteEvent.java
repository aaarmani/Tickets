package andersonarmani.tickets.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Armani andersonaramni@gmail.com on 10/07/2017.
 */
@Entity(indices = {@Index(value = {"event_id"}, unique = true)})
public class FavoriteEvent {
    @PrimaryKey(autoGenerate = true)
    private long uid;

    @ColumnInfo (name = "event_id")
    private String id;

    @ColumnInfo (name = "event_name")
    private String name;

    @ColumnInfo (name = "brand_name")
    private String brand;

    @ColumnInfo (name = "date")
    private String date;

    public FavoriteEvent(){}
    @Ignore
    public FavoriteEvent(String id, String name, String brand, String date) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.date = date;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
