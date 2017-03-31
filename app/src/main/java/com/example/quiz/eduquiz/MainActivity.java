package com.example.quiz.eduquiz;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private EditText input;
    private Button button,score;
    private TextView output;
    private String search;
    private ArrayList<Wiki> wikis;
    private WikiFragment frag;
    public static final String BASE_URL="http://www.wikia.com/api/v1/Wikis/ByString/?string=" ;
    private TextView lastScore;
    private ViewGroup viewGroup;

    private ViewGroup container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = (ViewGroup)findViewById(R.id.transition_container);

        lastScore = (TextView)findViewById(R.id.lastScore);

        score = (Button)findViewById(R.id.score);
        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Score.class));
            }
        });

        wikis = new ArrayList<Wiki>();
        frag = new WikiFragment();

        FragmentManager fm = getSupportFragmentManager();
        if(fm.findFragmentByTag("FamilyFragment")== null)
            fm.beginTransaction()
                    .add(R.id.frame, frag, "FamilyFragment")
                    .commit();

        input = (EditText)findViewById(R.id.input);
        button = (Button)findViewById(R.id.go);
        output = ((TextView)findViewById(R.id.data));

        viewGroup = (ViewGroup) findViewById(R.id.transitions_container);

        String s = "";
        String out = "";


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                wikis.clear();
                TransitionManager.beginDelayedTransition(container,new TransitionSet().addTransition(new AutoTransition()).setDuration(300).setStartDelay(50));

                String in = "";
                if(input.getText().toString().trim().length()==0){
                    Toast.makeText(MainActivity.this, "Enter Text.", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    in = URLEncoder.encode(input.getText().toString(), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String s="";
                try {
                    String out=new PersonSearch().execute(BASE_URL+in, null,"").get();
                    JSONObject jsonO;
                    try {
                        jsonO = new JSONObject(out);
                        s="";
                        JSONArray json = jsonO.getJSONArray("items");
                        for(int i=0;i<json.length();i++)
                            if(json.getJSONObject(i).optString("language").equals("en")&&json.getJSONObject(i).optString("topic")!=null){
                                TransitionManager.beginDelayedTransition(viewGroup, new TransitionSet()
                                        .addTransition(new AutoTransition()));
                                wikis.add(new Wiki(json.getJSONObject(i).optString("name"),json.getJSONObject(i).optString("domain")));
                        }
                        //s+=json.getJSONObject(i).optString("name")+"\n";
                        frag.populateList(wikis);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    output.setText(s);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }





            }});



//        try {
//            InputStream in = getAssets().open("data.json");
//            BufferedReader read =  new BufferedReader(new InputStreamReader(in));
//            String line;
//            while((line=read.readLine())!= null)
//                s+=line;
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e(TAG, "onCreate: blah blah");
//            Toast.makeText(MainActivity.this, "ARGGHHHH!!!!", Toast.LENGTH_SHORT).show();
//        }




    }

    public class PersonSearch extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                URLConnection connection = url.openConnection();

                String out="";

                InputStream in = connection.getInputStream();
                BufferedReader read =  new BufferedReader(new InputStreamReader(in));
                String line;
                while((line=read.readLine())!= null)
                    out+=line;
                Log.e("asdf",out);
                return out;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==101) {
            lastScore.setText(data.getStringExtra("out"));

        }
    }
}
