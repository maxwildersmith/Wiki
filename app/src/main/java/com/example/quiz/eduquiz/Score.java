package com.example.quiz.eduquiz;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import static com.example.quiz.eduquiz.Quiz.URLS;

public class Score extends AppCompatActivity {
    private Button home, replay, history, resetScore;
    private TextView score;
    public static final int REPLAY = 8802;
    public static final String HISTORY = "history";
    private ArrayList<Wiki> wikis;
    private String pastUrl;
    private WikiFragment frag;
    private boolean historyDisp;
    private Context mContext;
    private Activity mActivity;

    private RelativeLayout mRelativeLayout;
    private Button mButton;

    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        home = (Button)findViewById(R.id.home);

        historyDisp = false;
        score = (TextView)findViewById(R.id.score);
        history = (Button) findViewById(R.id.history);
        replay = (Button) findViewById(R.id.replay) ;
        replay.setVisibility(View.GONE);
        frag = new WikiFragment();

        FragmentManager fm = getSupportFragmentManager();
        if(fm.findFragmentByTag("FamilyFragment")== null)
            fm.beginTransaction()
                    .add(R.id.frame, frag, "FamilyFragment")
                    .commit();

        resetScore = (Button) findViewById(R.id.reset) ;
        resetScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSharedPreferences("stuff",Context.MODE_PRIVATE).edit().putInt("score",0).commit();
                score.setText("Score: "+getSharedPreferences("stuff", Context.MODE_PRIVATE).getInt("score",-1));

                Toast.makeText(Score.this, "Data Resetted!", Toast.LENGTH_SHORT).show();
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
                wikis = getIntent().getParcelableArrayListExtra("history");
                historyDisp = !historyDisp;
                if(historyDisp)
                    frag.populateListNoSort(wikis);
                else
                    frag.populateListNoSort(new ArrayList<Wiki>());

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
