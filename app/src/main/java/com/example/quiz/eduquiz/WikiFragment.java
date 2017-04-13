package com.example.quiz.eduquiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WikiFragment extends ListFragment {
    private List<Wiki> wikis;
    private WikiAdapter adapter;
    private SharedPreferences.Editor sharedUrl;


    public static final String URL_SENDER = "12345";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        wikis = new ArrayList<>();
        Comparator c = null;
        Collections.sort(wikis, c);


        adapter = new WikiAdapter(getActivity(), wikis);


        setListAdapter(adapter);

        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.e("asdf",id + " "+ position);
        Wiki wiki = wikis.get(position);
        Intent intent = new Intent(getContext(),Quiz.class);
        intent.putExtra(Quiz.DATA_NAME,wiki.getUrl());
        sharedUrl = this.getActivity().getSharedPreferences(URL_SENDER, Context.MODE_PRIVATE).edit();
        sharedUrl.putString("URL", wiki.getUrl());
        sharedUrl.commit();
        intent.putExtra("name", wiki.getName());
        startActivityForResult(intent,101);
    }

    public void populateList(ArrayList<Wiki> wikis) {
            this.wikis.clear();
            Collections.sort(wikis);
            this.wikis.addAll(wikis);
            adapter.notifyDataSetChanged();

    }
    public void populateListNoSort(ArrayList<Wiki> wikis) {
        this.wikis.clear();
        this.wikis.addAll(wikis);
        adapter.notifyDataSetChanged();

    }
}

