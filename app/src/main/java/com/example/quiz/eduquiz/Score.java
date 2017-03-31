package com.example.quiz.eduquiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import static com.example.quiz.eduquiz.Quiz.URLS;

public class Score extends AppCompatActivity {
    private Button home, replay, history, resetScore;
    private TextView score;
    public static final int REPLAY = 8802;
    public static final String HISTORY = "history";
    private Set<Wiki> wikis;
    private String pastUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        home = (Button)findViewById(R.id.home);
        //replay = (Button)findViewById(R.id.replay);
        score = (TextView)findViewById(R.id.score);
        history = (Button) findViewById(R.id.history);
        resetScore = (Button) findViewById(R.id.reset) ;
        resetScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSharedPreferences("stuff",Context.MODE_PRIVATE).edit().putInt("score",0).commit();
                score.setText("Score: "+getSharedPreferences("stuff", Context.MODE_PRIVATE).getInt("score",-1));
                Toast.makeText(Score.this, "Score Resetted!", Toast.LENGTH_SHORT).show();

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SharedPreferences pref = getSharedPreferences(URLS, MODE_PRIVATE);
//                String restoredText = pref.getString("url", null);
//
//                if (restoredText != null) {
//                    pastUrl = pref.getString("url", "NO URL FOUND!");
//                }
//                Toast.makeText(Score.this, pastUrl, Toast.LENGTH_SHORT).show();


            }
        });

//        replay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setResult(REPLAY);
//                finish();
//            }
//        });

        score.setText("Score: "+getSharedPreferences("stuff", Context.MODE_PRIVATE).getInt("score",-1));
    }
}
