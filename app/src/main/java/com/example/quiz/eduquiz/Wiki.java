package com.example.quiz.eduquiz;

import android.content.Context;
import android.content.Intent;

import java.util.Set;

/**
 * Created by csaper6 on 3/13/17.
 */
public class Wiki implements Comparable<Wiki>{
    String name,url;

    public Wiki(String url){
        this.url = url;
        name = "history wiki";
    }

    public Wiki(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }



    @Override
    public int compareTo(Wiki wiki) {
        return name.compareTo(wiki.getName());
    }
    public static Set<Wiki> stringToWikis(Set<String> set){

        return null;
    }
}
