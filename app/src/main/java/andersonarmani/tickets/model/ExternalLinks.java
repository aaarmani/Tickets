
package andersonarmani.tickets.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExternalLinks {

    @SerializedName("youtube")
    @Expose
    private List<Youtube> youtube = null;
    @SerializedName("facebook")
    @Expose
    private List<Facebook> facebook = null;
    @SerializedName("wiki")
    @Expose
    private List<Wiki> wiki = null;
    @SerializedName("musicbrainz")
    @Expose
    private List<Musicbrainz> musicbrainz = null;
    @SerializedName("homepage")
    @Expose
    private List<Homepage> homepage = null;

    public List<Youtube> getYoutube() {
        return youtube;
    }

    public void setYoutube(List<Youtube> youtube) {
        this.youtube = youtube;
    }

    public List<Facebook> getFacebook() {
        return facebook;
    }

    public void setFacebook(List<Facebook> facebook) {
        this.facebook = facebook;
    }

    public List<Wiki> getWiki() {
        return wiki;
    }

    public void setWiki(List<Wiki> wiki) {
        this.wiki = wiki;
    }

    public List<Musicbrainz> getMusicbrainz() {
        return musicbrainz;
    }

    public void setMusicbrainz(List<Musicbrainz> musicbrainz) {
        this.musicbrainz = musicbrainz;
    }

    public List<Homepage> getHomepage() {
        return homepage;
    }

    public void setHomepage(List<Homepage> homepage) {
        this.homepage = homepage;
    }

}
