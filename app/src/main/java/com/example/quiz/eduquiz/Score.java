package com.example.quiz.eduquiz;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Set;

public class Score extends AppCompatActivity {
    private Button home, replay, history;
    private TextView score;
    public static final int REPLAY = 8802;
    public static final String HISTORY = "history";
    private Set<Wiki> wikis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        home = (Button)findViewById(R.id.home);
        //replay = (Button)findViewById(R.id.replay);
        score = (TextView)findViewById(R.id.score);
        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSharedPreferences("stuff",Context.MODE_PRIVATE).edit().putInt("score",0).commit();
                score.setText("Score: "+getSharedPreferences("stuff", Context.MODE_PRIVATE).getInt("score",-1));

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
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
