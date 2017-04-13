package com.example.quiz.eduquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Set;

/**
 * Created by csaper6 on 3/13/17.
 */
public class Wiki implements Comparable<Wiki>, Parcelable {
    String name,url;

    public Wiki(String url){
        this.url = url;
        name = "history wiki";
    }

    public Wiki(String name, String url) {
        this.name = name;
        this.url = url;
    }

    protected Wiki(Parcel in) {
        name = in.readString();
        url = in.readString();
    }

    public static final Creator<Wiki> CREATOR = new Creator<Wiki>() {
        @Override
        public Wiki createFromParcel(Parcel in) {
            return new Wiki(in);
        }

        @Override
        public Wiki[] newArray(int size) {
            return new Wiki[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(url);
    }
}
