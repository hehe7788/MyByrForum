package byr.byrforum.javabean;

import java.util.ArrayList;

/**
 * Created by L.Y.C on 2016/1/15.
 */
public class AllTopTen {
    private ArrayList<TopTenTopic> topTenTopics;

    public void setTopTenTopics(ArrayList<TopTenTopic> topTenTopics) {
        this.topTenTopics = topTenTopics;
    }

    public ArrayList<TopTenTopic> getTopTenTopics() {

        return topTenTopics;
    }

    public AllTopTen(ArrayList<TopTenTopic> topTenTopics) {
        this.topTenTopics = topTenTopics;

    }
}
