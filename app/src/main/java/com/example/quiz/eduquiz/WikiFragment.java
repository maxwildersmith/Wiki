package com.example.quiz.eduquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by csaper6 on 3/13/17.
 */
public class WikiFragment extends ListFragment {
    private List<Wiki> wikis;
    private WikiAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        //create our list of heroes
        wikis = new ArrayList<>();
        //populateList();
        Comparator c = null;
        Collections.sort(wikis, c);


        //fill the custom adapter
        adapter = new WikiAdapter(getActivity(), wikis);





        //set the listView's adapter
        setListAdapter(adapter);

        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.e("asdf",id + " "+ position);
        Wiki wiki = wikis.get(position);
        Intent intent = new Intent(getContext(),Quiz.class);
        intent.putExtra(Quiz.DATA_NAME,wiki.getUrl());
        intent.putExtra("name", wiki.getName());
        startActivityForResult(intent,101);
    }

    public void populateList(ArrayList<Wiki> wikis) {
            this.wikis.clear();
            Collections.sort(wikis);
            this.wikis.addAll(wikis);
            adapter.notifyDataSetChanged();

    }
}

