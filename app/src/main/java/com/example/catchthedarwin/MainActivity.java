package com.example.catchthedarwin;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.View;
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

        timeText.setVisibility(View.INVISIBLE);
        scoreText.setVisibility(View.INVISIBLE);


        }



    public void setScore(View view){
        if (lever == false){
            scoreValue = 0;
            scoreText.setText(getString(R.string.score,scoreValue));

            timeText.setVisibility(View.VISIBLE);
            scoreText.setVisibility(View.VISIBLE);
            explainText.setVisibility(View.INVISIBLE);
            bestText.setVisibility(View.INVISIBLE);

            new CountDownTimer(10000, 1000) {
                @Override
                public void onFinish() {
                    Toast.makeText(getApplicationContext(),"Game Over!",Toast.LENGTH_LONG).show();
                    timeText.setVisibility(View.INVISIBLE);
                    scoreText.setVisibility(View.INVISIBLE);
                    explainText.setVisibility(View.VISIBLE);
                    bestText.setVisibility(View.VISIBLE);
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
            ++scoreValue;
            scoreText.setText(getString(R.string.score,scoreValue));
        }
    }





}