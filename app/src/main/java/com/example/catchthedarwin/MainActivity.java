package com.example.catchthedarwin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.util.DisplayMetrics;

import java.util.Random;

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
    ImageButton darwin;
    int randomX;
    int randomY;

    int screenWidthDP;
    int screenHeightDP;
    float density;
    int maxY = -150;
    int minY = 120;
    int xBorder;
    float darwinNormalY;
    float borderOfScoreboardY;
    int dynamicMaxY;
    int dynamicMinY;

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
        darwin = findViewById(R.id.buttonDarwin);

        scoreLayout = findViewById(R.id.scoreLayout);
        bestLayout = findViewById(R.id.bestLayout);

        sharedPref = this.getSharedPreferences("com.example.catchthedarwin", Context.MODE_PRIVATE);
        bestScore = sharedPref.getInt("best", 0);

        scoreLayout.setVisibility(View.INVISIBLE);

        bestText.setText(getString(R.string.bestScore,bestScore));

         if (bestScore == 0) {
            bestText.setVisibility(View.INVISIBLE);
        }

        DisplayMetrics displayMetrics = new android.util.DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        density = displayMetrics.density;

        screenWidthDP = (int) (displayMetrics.widthPixels / density);
        screenHeightDP = (int) (displayMetrics.heightPixels / density);

        xBorder = (screenWidthDP / 2) - 60;
        if (xBorder <= 0) xBorder = 50;

        darwinNormalY = darwin.getTop();
        borderOfScoreboardY = timeText.getTop();

        dynamicMaxY = -((int) (darwinNormalY / density)) + 40;
        dynamicMinY = ((int) ((borderOfScoreboardY - darwinNormalY) / density)) - 80;

        if (dynamicMaxY >= 0) dynamicMaxY = -150;
        if (dynamicMinY <= 0) dynamicMinY = 100;


        }

    public int dpToPx(int dp) {
        density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }


    public void setScoreandtime(View view){
        if (lever == false){
            scoreValue = 0;
            scoreText.setText(getString(R.string.score,scoreValue));

            scoreLayout.setVisibility(View.VISIBLE);
            bestLayout.setVisibility(View.GONE);

            new CountDownTimer(30000, 1000) {
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
                    randomX = new Random().nextInt(xBorder * 2) - xBorder;
                    randomY = new Random().nextInt(dynamicMinY - dynamicMaxY + 1) + dynamicMaxY;
                    darwin.setTranslationX(dpToPx(randomX));
                    darwin.setTranslationY(dpToPx(randomY));

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