package com.example.quiz.eduquiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by csaper6 on 3/13/17.
 */
public class WikiAdapter extends ArrayAdapter<Wiki> {

    private TextView name;
    private Wiki person;

    public WikiAdapter(Context context, List<Wiki> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        //if not given a view, we need to make one
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_wiki, null);
        }
        //get the item at the position where we are
         person = getItem(position);

        name = (TextView) convertView.findViewById(R.id.name);

        //put the text of the hero into the appropriate views
        name.setText(person.getUrl());




//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e("asdf",person.getName()+" "+person.getUrl());
//                getContext().startActivity(person.getIntent(getContext()));
//            }
//        });

        //return the view that you had edited
        return convertView;

    }
}