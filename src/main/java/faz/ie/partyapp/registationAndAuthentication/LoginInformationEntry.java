
/*

REFERENCE..
- https://www.youtube.com/watch?v=JDnaV7f-eNI
 */
package faz.ie.partyapp.registationAndAuthentication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;

import faz.ie.partyapp.main.MainActivity;
import faz.ie.partyapp.R;

public class LoginInformationEntry extends AppCompatActivity {



    private Button mGoButton;
    private EditText mEmail, mPassword;

    private int counter = 5;


    private TextView Info, noAcc;

    private ProgressDialog myProgressDialog2;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_information_entry);


        getSupportActionBar().hide();

        Info = (TextView)findViewById(R.id.tvInfo);

        Info.setText(getResources().getString(R.string.numberOfAttempts));
        noAcc = (TextView) findViewById(R.id.noAccText);
        myProgressDialog2 = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

       firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                final FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
                if (user != null)
                {

                }

            }
        };

        noAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(LoginInformationEntry.this, SignUpInformationEntry.class);
                startActivity(intent);
                Toast.makeText(LoginInformationEntry.this, "Please choose whether you want to Host or Attend an event and enter your details", Toast.LENGTH_LONG).show();
            }
        });
        mGoButton = (Button)findViewById(R.id.goButton);
        //mFullName = (EditText) findViewById(R.id.fullNameField);
        mEmail = (EditText) (EditText) findViewById(R.id.emailTxt);
        mPassword = (EditText) findViewById(R.id.passwordTxtField);


        mGoButton.setOnClickListener(new View.OnClickListener() {
              @Override
            public void onClick(View view) {

                  isConnected(LoginInformationEntry.this);

                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();

                  if(email.isEmpty()||password.isEmpty())
                  {
                      Toast.makeText(LoginInformationEntry.this, "Please enter your Email Address and Password", Toast.LENGTH_SHORT).show();
                      return;
                  }

                  mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginInformationEntry.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.v("partyApp",task.getException().getMessage());
                        ;
                        if(task.isSuccessful())
                        {

                            checkEmailVerification();
                        }

                      else if (!isConnected(LoginInformationEntry.this))
                        {
                            buildDialog(LoginInformationEntry.this).show();
                        }

                        else {
                            myProgressDialog2.dismiss();
                            counter--;
                           Toast.makeText(LoginInformationEntry.this, "Incorrect Email Address or Password\nYou have " + String.valueOf(counter) + " attempts remaining", Toast.LENGTH_SHORT).show();
                            Info.setText("No of attempts remaining: " + String.valueOf(counter));
                            if (counter == 0) {
                                mGoButton.setEnabled(false);

                            }
                        }
                    }
                });
                myProgressDialog2.setMessage("Logging In...");
                myProgressDialog2.show();

            }
        });

    }

    private void checkEmailVerification()
    {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        boolean emailFlag = firebaseUser.isEmailVerified();

        if(emailFlag)
        {
            Intent intent = new Intent(LoginInformationEntry.this, MainActivity.class);
            startActivity(intent);
        }
        else
        {
            myProgressDialog2.dismiss();
            Toast.makeText(LoginInformationEntry.this, "Verify your Email Address", Toast.LENGTH_LONG).show();
            mAuth.signOut();

        }
    }


    public boolean isConnected(Context context) {

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

                finish();
            }
        });


        return builder;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }


}

