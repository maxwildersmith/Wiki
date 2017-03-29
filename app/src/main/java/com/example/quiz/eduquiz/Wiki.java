package com.example.quiz.eduquiz;

import android.content.Context;
import android.content.Intent;

import java.util.Set;

/**
 * Created by csaper6 on 3/13/17.
 */
public class Wiki implements Comparable<Wiki>{
    String name,url;

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

    public void setUrl(String url) {
        this.url = url;
    }

    public Intent getIntent(Context context){
        Intent in = new Intent(context,Quiz.class);
        in.putExtra(Quiz.DATA_NAME,getUrl());
        in.putExtra("name", getName());
        return in;
    }

    @Override
    public int compareTo(Wiki wiki) {
        return name.compareTo(wiki.getName());
    }
    public static Set<Wiki> stringToWikis(Set<String> set){

        return null;
    }
}
