package com.example.quiz.eduquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

public class Quiz extends AppCompatActivity {
    private TextView title,question;
    private RadioButton[] options;//opt1,opt2,opt3,opt4;
    private Button submit;
    private RadioGroup radioGroup;
    public static final String DATA_NAME = "data stuffs", URLS = "1234", GET_MAIN = "/api/v1/Mercury/WikiVariables";
    private static final String GET_ARTICLES = "/api/v1/Articles/List?", GET_INFO = "/api/v1/Articles/AsSimpleJson?id=";
    private int correctOpt,score,qsLeft;
    private boolean started = false;
    private ViewGroup viewGroup;
    private ImageView favicon;
    private SharedPreferences.Editor url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        radioGroup = (RadioGroup)findViewById(R.id.RGroup);
        title = (TextView)findViewById(R.id.title);
        question = (TextView)findViewById(R.id.question);
        submit = (Button)findViewById(R.id.next);
        viewGroup = (ViewGroup) findViewById(R.id.transitions_container2);

        favicon = (ImageView)findViewById(R.id.image);
        try{
            String pageURL = new PersonSearch().execute("http://"+getIntent().getStringExtra(DATA_NAME)+GET_MAIN,null,"").get();
            JSONObject imgJSON = new JSONObject(pageURL);
            String imgURL = imgJSON.getJSONObject("data").getJSONObject("appleTouchIcon").getString("url");

            Log.e("asdf","URL = "+imgURL);
            favicon.setImageBitmap(new getImage().execute(imgURL,null,null).get());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        score=0;
        qsLeft=10;

        options = new RadioButton[4];
        options[0] = (RadioButton)findViewById(R.id.opt1);
        options[1] = (RadioButton)findViewById(R.id.opt2);
        options[2] = (RadioButton)findViewById(R.id.opt3);
        options[3] = (RadioButton)findViewById(R.id.opt4);



        submit.setText("Start!");

        url = getSharedPreferences(URLS, MODE_PRIVATE).edit();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionSet set = new TransitionSet()

                        .addTransition(new Fade());

                TransitionManager.beginDelayedTransition(viewGroup, set);
                checkAnswer();
            }
        });
    }

    private void getData() {

        title.setText("Score: "+score+", "+qsLeft+" Questions Left");
        if(qsLeft<=0)
            finish();

        try {
            String s = new PersonSearch().execute("http://"+getIntent().getStringExtra(DATA_NAME)+GET_ARTICLES,null,"").get();
            try {
                JSONArray articleList = new JSONObject(s).getJSONArray("items");
                Log.e("asdf","articles: "+articleList.length()+"");
                JSONObject[] articles = new JSONObject[articleList.length()];
                for(int i=0;i<articles.length;i++)
                    articles[i] = articleList.getJSONObject(i);
                int art = (int)(Math.random()*articles.length);
                 correctOpt = (int)(Math.random()*4);
                int x=-1;
                String[] articleTitles = new String[articles.length];
                for(int i=0;i<articles.length;i++)
                    articleTitles[i] = articles[i].getString("title");

                for(int i=0;i<options.length;i++)
                    if((x=(int)(Math.random()*articles.length))!=art)
                        for(RadioButton r: options)
                            if(r.getText().equals(articles[x].getString("title")))
                                break;
                            else
                                options[i].setText(articles[x].getString("title"));
                    else
                        i--;
                options[correctOpt].setText(articles[art].getString("title"));
                JSONArray body = new JSONObject(new PersonSearch().execute("http://"+getIntent().getStringExtra(DATA_NAME)+GET_INFO+articles[art].getString("id"),null,"").get()).getJSONArray("sections");
//                url.putString("url", new PersonSearch().execute("http://"+getIntent().getStringExtra(DATA_NAME)+GET_INFO+articles[art].getString("id"),null,"").get());
//                url.commit();
                JSONObject content = null;
                Log.e("asdf","longest: "+getLargestSection(body));
                Log.e("asdf"," Object 0 in body "+body.getJSONObject(0));
                Log.e("asdf"," Content from object 0 "+body.getJSONObject(0).getJSONArray("content"));
                question.setText("");

                for(int i =0;i<body.length();i++)
                    if((content = body.getJSONObject(i).getJSONArray("content").getJSONObject(i)).has("text")&&content.getString("text").trim().length()>0){
                        question.setText(blankOut(content.getString("text"),articles[art].getString("title")));
                    }


                if(question.getText().equals("")) {
                    question.setText("No valid articles");
                    getData();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("asdf",""+s);
            }


            started=true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        submit.setText("Next");

    }

    private void checkAnswer() {
        qsLeft--;
        boolean empty=true;

        if(started) {
            for(RadioButton r: options) {
                if (r.isChecked())
                    empty = false;
            }
            if(empty)
                return;
            if(options[correctOpt].isChecked()) {

                score++;
                Toast.makeText(Quiz.this, "Correct!", Toast.LENGTH_SHORT).show();
                getSharedPreferences("stuff", 0).edit().putInt("score", 1 + getSharedPreferences("stuff", Context.MODE_PRIVATE).getInt("score", 0)).commit();
            } else
                Toast.makeText(Quiz.this, "Should be: "+options[correctOpt].getText(), Toast.LENGTH_LONG).show();
        }
        else{
            for(RadioButton r:options)
                r.setVisibility(View.VISIBLE);
            question.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            title.setText(getIntent().getStringExtra("name"));
        }
        radioGroup.clearCheck();


        getData();
    }

    public class PersonSearch extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            Log.e("asdf","async: "+urls[0]);
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
            Log.e("asdf","stuff");
            return null;
        }


    }
    public static JSONObject getLargestSection(JSONArray array) throws JSONException {
        Log.e("asdf",array.toString());
        JSONObject longest = array.getJSONObject(0);
        for(int i =1;i<array.length();i++)
            if(array.getJSONObject(i).getJSONArray("content").getJSONObject(0).optString("text").length()>longest.getJSONArray("content").getJSONObject(0).optString("text").length())
                longest = array.getJSONObject(i);
        return longest;
    }

    public static String blankOut(String target, String answer){
        String[] anserWords = answer.split(" ");
        String out = target;
        String blanks ="";
        for(int i=0;i<anserWords[0].length();i++)
            blanks+="_";
        for(String word: anserWords)
            out = out.replaceAll(word,blanks);
        return out;
    }
    public class getImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
                map = downloadImage(urls[0]);

            return map;
        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            Log.d("asdf555",urlString+"asdf");
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }
    }

    @Override
    public void finish() {
        setResult(101,new Intent().putExtra("out",score+" out of "+(10-qsLeft)+" on "+getIntent().getStringExtra("name")).putExtra("url",getIntent().getStringExtra(DATA_NAME)));
        super.finish();
    }
}
