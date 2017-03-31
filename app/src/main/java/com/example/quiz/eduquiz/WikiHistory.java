package com.example.quiz.eduquiz;

/**
 * Created by Max on 3/31/2017.
 */

public class WikiHistory extends Wiki {
    private int location;
    public WikiHistory(String name, String url, int location) {
        super(name, url);
        this.location++;
    }


    public int getLocation() {
        return location;
    }

    public int compareTo(WikiHistory wiki) {
        return location - wiki.getLocation();
    }
}
