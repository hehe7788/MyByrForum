package byr.byrforum.javabean;

import java.util.ArrayList;

/**
 * Created by L.Y.C on 2016/1/14.
 */
public class OneTopic {
    private String board;
    private String url;
    private String count;
    private String page;
    private ArrayList<OneFloor> items;
    private String title;
    private String totalReply;
    private String err;

    public String getErr() {
        return err;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setPage(String page) {
        this.page = page;
    }



    public void setTitle(String title) {
        this.title = title;
    }

    public void setTotalReply(String totalReply) {
        this.totalReply = totalReply;
    }

    public String getBoard() {

        return board;
    }

    public String getUrl() {
        return url;
    }

    public String getCount() {
        return count;
    }

    public String getPage() {
        return page;
    }

    public OneTopic(String board, String url, String count, String page, ArrayList<OneFloor> items, String title, String totalReply) {
        this.board = board;
        this.url = url;
        this.count = count;
        this.page = page;
        this.items = items;
        this.title = title;
        this.totalReply = totalReply;
    }

    public void setItems(ArrayList<OneFloor> items) {

        this.items = items;
    }

    public ArrayList<OneFloor> getItems() {

        return items;
    }

    public String getTitle() {
        return title;
    }

    public String getTotalReply() {
        return totalReply;
    }


}
