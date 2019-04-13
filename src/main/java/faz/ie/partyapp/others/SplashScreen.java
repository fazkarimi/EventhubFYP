package faz.ie.partyapp.others;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import faz.ie.partyapp.R;
import faz.ie.partyapp.walkthrough.IntroActivity;

public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();
        final boolean b = new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashScreen.this, IntroActivity.class);
                SplashScreen.this.startActivity(mainIntent);
               SplashScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
