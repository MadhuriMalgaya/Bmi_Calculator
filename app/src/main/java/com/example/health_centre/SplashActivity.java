package com.example.health_centre;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    ImageView leftArc, rightArc, needle;
    TextView appName, thought;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        leftArc = findViewById(R.id.leftArc);
        rightArc = findViewById(R.id.rightArc);
        needle = findViewById(R.id.needle);
        appName = findViewById(R.id.appName);
        thought = findViewById(R.id.thought);
        String message = "Stay healthy and happy";
        int delay = 100; // milliseconds between each character

        thought.setText(""); // Clear initially
        thought.setAlpha(0f); // Make it fully transparent

// Fade-in animation
        thought.animate().alpha(1f).setDuration(1000).start();

        Handler handler = new Handler();
        for (int i = 0; i <= message.length(); i++) {
            final int index = i;
            handler.postDelayed(() -> {
                thought.setText(message.substring(0, index));
            }, delay * i);
        }


        startFruitRain();
        repeatFruitRain();


        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        leftArc.setTranslationX(-screenWidth);
        rightArc.setTranslationX(screenWidth);
        needle.setTranslationY(-screenHeight);


        // Animate left and right arcs
        leftArc.animate().translationX(0).setDuration(3000).start();
        rightArc.animate().translationX(0).setDuration(3000).start();

        // Animate needle dropping and rotating
        new Handler().postDelayed(() -> {
            needle.animate().translationY(0).setDuration(4000).withEndAction(() -> {
                needle.animate().rotation(180f).setDuration(1000).start();
            }).start();
        }, 500);

        // Show App name
        new Handler().postDelayed(() -> {
            appName.setVisibility(View.VISIBLE);
            appName.setAlpha(0f);
            appName.animate().alpha(1f).setDuration(1000).start();
            appName.animate().translationY(0).setDuration(3000).start();
        }, 1000);

        // Move to next screen
        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }, 9000);
    }

    private void startFruitRain() {
        int[] fruitIds = {
                R.id.apple, R.id.orange, R.id.banana, R.id.pomegrante,
                R.id.mango, R.id.cucumber, R.id.grapes, R.id.onion, R.id.bitter
        };
        Handler handler = new Handler();

        for (int i = 0; i < fruitIds.length; i++) {
            int delay = i * 400; // stagger fruit drop
            int fruitId = fruitIds[i];

            handler.postDelayed(() -> {
                ImageView fruit = findViewById(fruitId);
                fruit.setVisibility(View.VISIBLE);
                int screenWidth = getResources().getDisplayMetrics().widthPixels;
                float randomX = (float) (Math.random() * (screenWidth - fruit.getWidth()));
                fruit.setX(randomX);
                fruit.setY(-100); // start off-screen

                fruit.animate()
                        .translationY(2000f) // fall to bottom
                        .setDuration(3000)
                        .withEndAction(() -> fruit.setVisibility(View.INVISIBLE)) // hide after fall
                        .start();
            }, delay);
        }
    }

    private void repeatFruitRain() {
        Handler handler = new Handler();
        Runnable fruitRainRunnable = new Runnable() {
            @Override
            public void run() {
                startFruitRain();
                handler.postDelayed(this, 3000); // repeat every 3 seconds
            }
        };
        handler.post(fruitRainRunnable);
    }


}
