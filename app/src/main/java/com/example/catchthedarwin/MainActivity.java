package com.example.catchthedarwin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
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
    boolean lever = true;
    SharedPreferences sharedPref;
    int bestScore;
    LinearLayout scoreLayout;
    LinearLayout bestLayout;
    View mainLayout;
    ImageButton darwin;
    Random random = new Random();
    int randomX;
    int randomY;
    DisplayMetrics displayMetrics = new android.util.DisplayMetrics();
    int darwinWidth;
    int darwinHeight;
    float density;
    int xBorder;
    int layoutHeight;
    float darwinNormalY;
    int scoreboardHeight;
    int dynamicMaxY;
    int dynamicMinY;
    Handler handler = new Handler(Looper.getMainLooper());
    Runnable runnable;
    boolean darwinIsHiding = false;


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
        mainLayout = findViewById(R.id.main);

        scoreLayout = findViewById(R.id.scoreLayout);
        bestLayout = findViewById(R.id.bestLayout);

        sharedPref = this.getSharedPreferences("com.example.catchthedarwin", Context.MODE_PRIVATE);
        bestScore = sharedPref.getInt("best", 0);

        scoreLayout.setVisibility(View.INVISIBLE);

        bestText.setText(getString(R.string.bestScore,bestScore));

         if (bestScore == 0) {
            bestText.setVisibility(View.INVISIBLE);
        }

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        density = displayMetrics.density;


        runnable = new Runnable() {
            @Override
            public void run() {
                if(darwinIsHiding){
                    randomX = random.nextInt(xBorder * 2) - xBorder;
                    randomY = dynamicMinY - random.nextInt(dynamicMinY - dynamicMaxY + 1);
                    darwin.setTranslationX(dpToPx(randomX));
                    darwin.setTranslationY(dpToPx(randomY));
                    darwin.setVisibility(View.VISIBLE);
                    handler.postDelayed(this, 1000);
                    darwinIsHiding = false;
                }
                else {
                    handler.postDelayed(this, 1000);
                    darwin.setVisibility(View.GONE);
                    darwinIsHiding = true;
                }
            }
        };

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRunnable();
    }

    public int dpToPx(int dp) {
        return Math.round((float) dp * density);
    }


    public void setScoreAndTime(View view){
        if (lever){
            scoreValue = 0;
            scoreText.setText(getString(R.string.score,scoreValue));
            scoreLayout.setVisibility(View.VISIBLE);
            bestLayout.setVisibility(View.GONE);

            startRunnable();

            new CountDownTimer(30000, 1000) {
                @Override
                public void onFinish() {
                    Toast.makeText(getApplicationContext(),"Game Over!",Toast.LENGTH_LONG).show();

                    bestLayout.setVisibility(View.VISIBLE);
                    scoreLayout.setVisibility(View.INVISIBLE);

                    if (scoreValue>bestScore){
                        sharedPref.edit().putInt("best",scoreValue).apply();
                        bestScore = scoreValue;
                        bestText.setText(getString(R.string.bestScore,bestScore));
                        bestText.setVisibility(View.VISIBLE);
                    }
                    else if (bestScore == 0) {
                        bestText.setVisibility(View.INVISIBLE);
                    }
                    stopRunnable();
                    darwin.setTranslationX(0);
                    darwin.setTranslationY(0);
                    lever = true;


                }

                @Override
                public void onTick(long millisUntilFinished) {
                    timeText.setText(getString(R.string.time, millisUntilFinished / 1000));

                }
            }.start();
            lever = false;
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

    public void startRunnable() {
        darwinNormalY = darwin.getTop();
        darwinWidth = darwin.getWidth();
        darwinHeight = darwin.getHeight();
        scoreboardHeight = scoreLayout.getTop() + scoreLayout.getHeight();
        layoutHeight = mainLayout.getHeight();

        xBorder = (int) (((float) (displayMetrics.widthPixels - darwinWidth) / 2 ) /density) - 45;
        if (xBorder <= 0) xBorder = 50;

        dynamicMaxY = (int) ((scoreboardHeight - darwinNormalY) / density) + 10;
        dynamicMinY = (int) ((layoutHeight - darwinNormalY - darwinHeight) / density) -50 ;

        if (dynamicMaxY >= 0) dynamicMaxY = -100;
        if (dynamicMinY <= 0) dynamicMinY = 150;


        handler.post(runnable);
    }
    public void stopRunnable() {
        handler.removeCallbacks(runnable);
        darwin.setVisibility(View.VISIBLE);
    }



}