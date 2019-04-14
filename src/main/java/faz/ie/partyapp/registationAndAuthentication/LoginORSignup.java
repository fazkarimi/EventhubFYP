package faz.ie.partyapp.registationAndAuthentication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import faz.ie.partyapp.R;

public class LoginORSignup extends AppCompatActivity {

    private Button loginButton;
    private Button signInButton;
    private Button clickMeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_orsignup);

       /* if(!isConnected(LoginORSignup.this)) buildDialog(LoginORSignup.this).show();
        else

        {
            setContentView(R.layout.activity_login_orsignup);
        }*/
        getSupportActionBar().hide();
        loginButton = (Button)findViewById(R.id.theLoginButton);
        signInButton = (Button)findViewById(R.id.theSignupButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                    Intent intent =new Intent(LoginORSignup.this, LoginInformationEntry.class);
                    startActivity(intent);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {

                Intent intent =new Intent(LoginORSignup.this, SignUpInformationEntry.class);
                startActivity(intent);

            }
        });

    }

    /*public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent =new Intent(LoginORSignup.this, LoginORSignup.class);
                startActivity(intent);
            }
        });

        return builder;
    }*/

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }
}
