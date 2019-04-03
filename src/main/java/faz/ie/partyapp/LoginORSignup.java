package faz.ie.partyapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginORSignup extends AppCompatActivity {

    private Button loginButton;
    private Button signInButton;
    private Button clickMeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_orsignup);

        getSupportActionBar().hide();
        loginButton = (Button)findViewById(R.id.theLoginButton);
        signInButton = (Button)findViewById(R.id.theSignupButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent =new Intent(LoginORSignup.this,LoginInformationEntry.class);
                startActivity(intent);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {

                Intent intent =new Intent(LoginORSignup.this,SignUpInformationEntry.class);
                startActivity(intent);

            }
        });


    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }
}
