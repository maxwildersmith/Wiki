package com.example.quiz.eduquiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private EditText input;
    private Button button,score;
    private ArrayList<Wiki> wikis, history;
    private WikiFragment frag;
    public static final String BASE_URL="http://www.wikia.com/api/v1/Wikis/ByString/?string=" ;
    private TextView lastScore;
    private ViewGroup viewGroup;
    private boolean imagesEnabled = false;
    private String locale = "en";
    private AlertDialog.Builder builder;

    private ViewGroup container;
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.images:
                imagesEnabled = !imagesEnabled;
                item.setTitle("Experimnet: "+imagesEnabled);
                return true;
            case R.id.locale:
                item.setTitle("Locale: "+locale);
                builder = new AlertDialog.Builder(this);
                builder.setItems(new String[]{"English", "Español", "Norsk", "日本語 ","中文"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                locale="en";
                                break;
                            case 1:
                                locale="es";
                                break;
                            case 2:
                                locale="no";
                                break;
                            case 3:
                                locale="ja";
                                break;
                            case 4:
                                locale="zh";
                                break;
                        }
                    }
                });
                builder.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private Typeface tf1, tf2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = (ViewGroup)findViewById(R.id.transition_container);

        lastScore = (TextView)findViewById(R.id.lastScore);

        tf1 = Typeface.createFromAsset(getAssets(), "Cornerstone.ttf");
                score = (Button)findViewById(R.id.score);
        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Score.class).putParcelableArrayListExtra("history",history));
            }
        });

        wikis = new ArrayList<Wiki>();
        history = new ArrayList<Wiki>();
        frag = new WikiFragment();

        FragmentManager fm = getSupportFragmentManager();
        if(fm.findFragmentByTag("FamilyFragment")== null)
            fm.beginTransaction()
                    .add(R.id.frame, frag, "FamilyFragment")
                    .commit();

        input = (EditText)findViewById(R.id.input);
        button = (Button)findViewById(R.id.go);

        viewGroup = (ViewGroup) findViewById(R.id.transition_container);
        input.setTypeface(tf1);
        button.setTypeface(tf1);
        score.setTypeface(tf1);
        lastScore.setTypeface(tf1);


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
                try {
                    String out=new PersonSearch().execute(BASE_URL+in, null,"").get();
                    JSONObject jsonO;
                    try {
                        jsonO = new JSONObject(out);
                        JSONArray json = jsonO.getJSONArray("items");
                        for(int i=0;i<json.length();i++)
                            if(json.getJSONObject(i).optString("language").equals(locale)&&json.getJSONObject(i).optString("topic")!=null){
                                TransitionManager.beginDelayedTransition(viewGroup, new TransitionSet()
                                        .addTransition(new AutoTransition()));
                                if(imagesEnabled)
                                     wikis.add(new Wiki(json.getJSONObject(i).optString("name"),json.getJSONObject(i).optString("domain"),new JSONObject(new PersonSearch().execute("https://"+json.getJSONObject(i).optString("domain")+Quiz.GET_MAIN,null,"").get()).getJSONObject("data").optString("favicon")));
                                else
                                    wikis.add(new Wiki(json.getJSONObject(i).optString("name"),json.getJSONObject(i).optString("domain")));
                        }
                        frag.populateList(wikis);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }





            }});

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
                Log.d("WikiSearcch",out);
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
            history.add(0,new Wiki(data.getStringExtra("url")));
        }
    }
}