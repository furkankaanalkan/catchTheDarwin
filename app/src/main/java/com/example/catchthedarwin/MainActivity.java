package com.example.catchthedarwin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    TextView timeText;
    TextView scoreText;
    TextView explainText;
    TextView bestText;
    int  scoreValue = 0;
    boolean lever = false;
    SharedPreferences sharedPref;
    int bestScore;
    LinearLayout scoreLayout;
    LinearLayout bestLayout;
    LinearLayout darwinLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        timeText = findViewById(R.id.timeText);
        scoreText = findViewById(R.id.scoreText);
        explainText = findViewById(R.id.explainText);
        bestText = findViewById(R.id.bestText);

        scoreLayout = findViewById(R.id.scoreLayout);
        bestLayout = findViewById(R.id.bestLayout);
        darwinLayout = findViewById(R.id.darwinLayout);

        sharedPref = this.getSharedPreferences("com.example.catchthedarwin", Context.MODE_PRIVATE);
        bestScore = sharedPref.getInt("best", 0);

        scoreLayout.setVisibility(View.INVISIBLE);

        bestText.setText(getString(R.string.bestScore,bestScore));

         if (bestScore == 0) {
            bestText.setVisibility(View.INVISIBLE);
        }


        }



    public void setScoreandtime(View view){
        if (lever == false){
            scoreValue = 0;
            scoreText.setText(getString(R.string.score,scoreValue));

            scoreLayout.setVisibility(View.VISIBLE);
            bestLayout.setVisibility(View.GONE);

            new CountDownTimer(5000, 1000) {
                @Override
                public void onFinish() {
                    Toast.makeText(getApplicationContext(),"Game Over!",Toast.LENGTH_LONG).show();

                    bestLayout.setVisibility(View.VISIBLE);
                    scoreLayout.setVisibility(View.INVISIBLE);

                    if (scoreValue>bestScore){
                        sharedPref.edit().putInt("best",bestScore).apply();
                        bestScore = scoreValue;
                        bestText.setText(getString(R.string.bestScore,bestScore));
                    }
                    else if (bestScore == 0) {
                        bestText.setVisibility(View.INVISIBLE);
                    }
                    lever = false;


                }

                @Override
                public void onTick(long millisUntilFinished) {
                    timeText.setText(getString(R.string.time, millisUntilFinished / 1000));

                }
            }.start();
            lever = true;
        }
        else {

            scoreValue++;
            scoreText.setText(getString(R.string.score,scoreValue));
        }
    }
    public void delete(View view){

        sharedPref.edit().remove("best").apply();
        bestScore = 0;
        bestText.setText(getString(R.string.bestScore,bestScore));


    }






}