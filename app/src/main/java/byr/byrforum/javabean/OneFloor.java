package byr.byrforum.javabean;

import java.util.ArrayList;

/**
 * Created by L.Y.C on 2016/1/14.
 */
public class OneFloor {
    private String author;
    private String content;
    private String floor;
    private ArrayList<String> pics;

    public void setPics(ArrayList<String> pics) {
        this.pics = pics;
    }

    public ArrayList<String> getPics() {

        return pics;
    }

    public OneFloor(String author, String content, String floor, ArrayList<String> pics) {

        this.author = author;
        this.content = content;
        this.floor = floor;
        this.pics = pics;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getAuthor() {

        return author;
    }

    public String getContent() {
        return content;
    }

    public String getFloor() {
        return floor;
    }

    public OneFloor(String author, String content, String floor) {

        this.author = author;
        this.content = content;
        this.floor = floor;
    }
}
