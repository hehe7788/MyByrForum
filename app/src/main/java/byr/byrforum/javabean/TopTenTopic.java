package byr.byrforum.javabean;

/**
 * Created by L.Y.C on 2016/1/13.
 */
public class TopTenTopic {
    private String title;
    private String author;
    private String link;
    private String pubDate;

    public TopTenTopic(String title, String author, String link, String pubDate) {
        this.title = title;
        this.author = author;
        this.link = link;
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getAuthor() {
        return author;
    }

    public String getLink() {
        return link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}
