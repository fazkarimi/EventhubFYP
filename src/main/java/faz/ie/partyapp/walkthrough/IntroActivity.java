package faz.ie.partyapp.walkthrough;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import faz.ie.partyapp.R;
import faz.ie.partyapp.registationAndAuthentication.LoginORSignup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter ;
    TabLayout tabIndicator;
    Button btnNext;
    int position = 0 ;
    Button btnGetStarted;
    Animation btnAnim ;
    TextView tvSkip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // make the activity on full screen

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // when this activity is about to be launch we need to check if its openened before or not

        if (restorePrefData()) {

            Intent intent = new Intent(getApplicationContext(),LoginORSignup.class );
            startActivity(intent);
            finish();


        }

        setContentView(R.layout.activity_intro);

        // hide the action bar

        getSupportActionBar().hide();

        // ini views
        btnNext = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);
        tvSkip = findViewById(R.id.tv_skip);

        // fill list screen

        final List<ScreenItem> mList = new ArrayList<>();

        mList.add(new ScreenItem("What is Eventhub?","Eventhub is an android mobile application platform that allow users to host and/or attend an event in one place.\n" + "It works by using a swiping functionality which allow users to make a quick decision whether they’re interested or not.\n",R.drawable.eventhub_logo_circle5));
        mList.add(new ScreenItem("Swipe","When you see an event or user profile, you can swipe left or right. Swiping LEFT indicates that you are not interested and swiping RIGHT indicates that you are interested",R.drawable.swiping));
        mList.add(new ScreenItem("Match","If 2 users swipe RIGHT on each other, it will produce a match, meaning the 2 users can communicate via our instant messaging, it will also mean, the user looking to attend events can now attend the event they swiped right on",R.drawable.match ));
        mList.add(new ScreenItem("Attend","Once 2 users have matched, the user looking to attend the event he swiped can now do so and it is up to the event hoste to give the event information via the messaging.",R.drawable.attend));
        mList.add(new ScreenItem("Messaging","Eventhub has an instant messaging system for you to communicate with the users you have matched with",R.drawable.messaging));
        mList.add(new ScreenItem("Settings","You can configure your profile, delete account, sign out and more in settings",R.drawable.settings ));

        // setup viewpager
        screenPager =findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this,mList);
        screenPager.setAdapter(introViewPagerAdapter);

        // setup tablayout with viewpager

        tabIndicator.setupWithViewPager(screenPager);

        // next button click Listner

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = screenPager.getCurrentItem();
                if (position < mList.size()) {

                    position++;
                    screenPager.setCurrentItem(position);


                }

                if (position == mList.size()-1) { // when we rech to the last screen

                    // TODO : show the GETSTARTED Button and hide the indicator and the next button

                    loaddLastScreen();


                }



            }
        });

        // tablayout add change listener

       tabIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
           @Override
           public void onTabSelected(TabLayout.Tab tab)
           {

               if (tab.getPosition() == mList.size()-1) {

                   loaddLastScreen();

               }
           }

           @Override
           public void onTabUnselected(TabLayout.Tab tab) {

           }

           @Override
           public void onTabReselected(TabLayout.Tab tab) {

           }
       });




        // Get Started button click listener

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //open main activity

                Intent intent = new Intent(getApplicationContext(), LoginORSignup.class);
                startActivity(intent);
                // also we need to save a boolean value to storage so next time when the user run the app
                // we could know that he is already checked the intro screen activity
                // i'm going to use shared preferences to that process
                savePrefsData();
                finish();



            }
        });

        // skip button click listener

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenPager.setCurrentItem(mList.size());
            }
        });



    }

    private boolean restorePrefData() {


        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend",false);
        return  isIntroActivityOpnendBefore;



    }

    private void savePrefsData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend",true);
        editor.commit();


    }

    // show the GETSTARTED Button and hide the indicator and the next button
    private void loaddLastScreen() {

        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        // TODO : ADD an animation the getstarted button
        // setup animation
        btnGetStarted.setAnimation(btnAnim);



    }
}
