
/*

REFERENCE..
- https://www.youtube.com/watch?v=JDnaV7f-eNI
 */
package faz.ie.partyapp.registationAndAuthentication;

import android.app.ProgressDialog;
import android.content.Intent;
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
        Info.setText("No of attempts : 5");
        noAcc = findViewById(R.id.noAccText);
        myProgressDialog2 = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                final FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
                if (user != null)
                {

                    Intent intent = new Intent(LoginInformationEntry.this, MainActivity.class);
                    startActivity(intent);

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

                        if(task.isSuccessful())
                        {
                           // Toast.makeText(LoginInformationEntry.this, "Logged In", Toast.LENGTH_SHORT).show();
                            checkEmailVerification();

                        }

                        else {
                            myProgressDialog2.dismiss();
                            counter--;
                            Toast.makeText(LoginInformationEntry.this, "Error Logging In\nYou have " + String.valueOf(counter) + " attempts remaining", Toast.LENGTH_SHORT).show();
                            Info.setText("No of attempts remaining: " + String.valueOf(counter));
                            if (counter == 0) {
                                mGoButton.setEnabled(false);
                                //Toast.makeText(LoginInformationEntry.this, "You have run out of attempts", Toast.LENGTH_SHORT).show();


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
        Boolean emailFlag = firebaseUser.isEmailVerified();

        if(emailFlag)
        {
            Intent intent = new Intent(LoginInformationEntry.this, MainActivity.class);
            startActivity(intent);
        }
        else
        {

            Toast.makeText(LoginInformationEntry.this, "Please check your Email inbox to verify the Email Address you have entered", Toast.LENGTH_LONG).show();
        }
    }

    public void goToSignUpActivity(View view)
    {

    }

   /* @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }*/
}

