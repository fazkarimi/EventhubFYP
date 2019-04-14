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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import faz.ie.partyapp.main.MainActivity;
import faz.ie.partyapp.R;

public class SignUpInformationEntry extends AppCompatActivity {

    private Button mSignUpButton;
    private EditText mEmail, mPassword, mFullName, mPhoneNumber, mGender, mAge;

    private RadioGroup mRadioButtonGroup;

    private RadioButton lookForAPartyRadioButton;
    private TextView alreadyHaveAnAcc;

    private ProgressDialog myProgressDialog;

    private FirebaseAuth mAuth;


    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_information_entry);

        myProgressDialog = new ProgressDialog(this);
        getSupportActionBar().hide();  // to hide the action bar in the specific activity
        /*LookRadioButton.setChecked(true);*/

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    /*Intent intent = new Intent(SignUpInformationEntry.this, MainActivity.class);
                    startActivity(intent);*/
                }
            }
        };

        mSignUpButton = (Button) findViewById(R.id.signUpButton);
        mEmail = (EditText) findViewById(R.id.emailField);
        mPassword = (EditText) findViewById(R.id.passwordTxt);
        mPhoneNumber = (EditText) findViewById(R.id.phoneNoTxt);
        mFullName = (EditText) findViewById(R.id.nameTxtField);
        mGender = (EditText) findViewById(R.id.genderTxtField);
        mAge = (EditText) findViewById(R.id.ageTxtField);
        alreadyHaveAnAcc = findViewById(R.id.textView3);
        lookForAPartyRadioButton = (RadioButton) findViewById(R.id.look);

        mRadioButtonGroup = (RadioGroup) findViewById(R.id.radioButtonGroup);

        mRadioButtonGroup.check(lookForAPartyRadioButton.getId());


        alreadyHaveAnAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(SignUpInformationEntry.this, LoginInformationEntry.class);
                startActivity(intent);

                Toast.makeText(SignUpInformationEntry.this, "Enter the Email Address and Password you Signed up with", Toast.LENGTH_LONG).show();
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // GETTING DETAILS FROM USER
                final String Email = mEmail.getText().toString();
                final String Password = mPassword.getText().toString();
                final String FullName = mFullName.getText().toString();
                final String PhoneNumber = mPhoneNumber.getText().toString();
                final String Gender = mGender.getText().toString();
                final String Age = mAge.getText().toString();
                //checkIfEmailAlreadyExists();

                final String validatedGender1 = "male";
                final String validatedGender2 = "Male";
                final String validatedGender3 = "female";
                final String validatedGender4 = "Female";

                final int PhoneNumberLength = PhoneNumber.length(); //length of phone number entered

                final int PasswordLength = Password.length(); //length of phone number entered

                //VALIDATION

                if (Email.isEmpty() || Password.isEmpty() || FullName.isEmpty() || PhoneNumber.isEmpty() || Gender.isEmpty() || Age.isEmpty())
                {
                    Toast.makeText(SignUpInformationEntry.this, "Fill in all the fields please", Toast.LENGTH_SHORT).show();

                }
                else if(PasswordLength <= 5 )
                {
                    Toast.makeText(SignUpInformationEntry.this, "Password needs to be at least 6 characters long! ", Toast.LENGTH_SHORT).show();
                }

               else if (PhoneNumberLength != 10 )
                {
                    Toast.makeText(SignUpInformationEntry.this, "Enter a valid 10 digits Phone Number! ", Toast.LENGTH_SHORT).show();
                }

                else if (Age.equals("15") || Age.equals("14") || Age.equals("13") || Age.equals("12") || Age.equals("11") || Age.equals("10") || Age.equals("9") || Age.equals("8") || Age.equals("7") || Age.equals("6") || Age.equals("5") || Age.equals("4") || Age.equals("3") || Age.equals("2") || Age.equals("1") || Age.equals("0"))
                {
                    Toast.makeText(SignUpInformationEntry.this, "You must be 16 or over to use Eventhub.\nPlease enter valid age", Toast.LENGTH_SHORT).show();
                } else

                {
                    //DO EVERYTHING IN HERE
                    checkIfEmailAlreadyExists();

                    final int selectID = mRadioButtonGroup.getCheckedRadioButtonId();

                    final RadioButton radioButton = (RadioButton) findViewById(selectID);

                    mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(SignUpInformationEntry.this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                if (selectID == R.id.look) {

                                    String userID = mAuth.getCurrentUser().getUid();

                                    DatabaseReference currentUserDB = FirebaseDatabase.getInstance().getReference().child("Users")
                                            .child(userID);

                                    Map userInfo = new HashMap<>();
                                    userInfo.put("FullName", FullName);
                                    userInfo.put("PhoneNumber", PhoneNumber);
                                    userInfo.put("Email", Email);
                                    userInfo.put("Gender", Gender);
                                    userInfo.put("Age", Age);
                                    userInfo.put("Password", Password);
                                    userInfo.put("userType", radioButton.getText().toString());
                                    userInfo.put("profileImageUrl", "defaultUserImage");
                                    currentUserDB.updateChildren(userInfo);

                                    checkIfEmailIsVerifiedForAttendingUsers();

                                }

                                else if (selectID == R.id.host) {
                                    String userID = mAuth.getCurrentUser().getUid();

                                    DatabaseReference currentUserDB = FirebaseDatabase.getInstance().getReference().child("Users")
                                            .child(userID);


                                    Map userInfo = new HashMap<>();
                                    userInfo.put("FullName", FullName);
                                    userInfo.put("PhoneNumber", PhoneNumber);
                                    userInfo.put("Email", Email);
                                    userInfo.put("Gender", Gender);
                                    userInfo.put("Age", Age);
                                    userInfo.put("Password", Password);
                                    userInfo.put("userType", radioButton.getText().toString());
                                    userInfo.put("profileImageUrl", "defaultUserImage");

                                    currentUserDB.updateChildren(userInfo);

                                    Toast.makeText(SignUpInformationEntry.this, "Enter your events information", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignUpInformationEntry.this, PartyInformation.class);
                                    intent.putExtra("Email", Email);
                                    startActivity(intent);


                                }

                            }

                        }
                    });

                    myProgressDialog.setMessage("Signing Up...");
                    myProgressDialog.show();
                }
            }

        });


    }

    public void checkIfEmailAlreadyExists ()
    {
        mAuth.fetchProvidersForEmail(mEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>()
        {
            @Override
            public void onComplete(@NonNull Task<ProviderQueryResult> task)
            {

                final String Email = mEmail.getText().toString();
                boolean check = !task.getResult().getProviders().isEmpty(); // id the email doesnt exist

                if(!check)
                {

                }
                else
                {
                    myProgressDialog.dismiss();
                    Toast.makeText(SignUpInformationEntry.this, "The Email Address "+ Email+" already exists", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void checkIfEmailIsVerifiedForAttendingUsers()
    {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if(firebaseUser != null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if(task.isSuccessful())
                    {
                        final String Email = mEmail.getText().toString();

                        Intent intent = new Intent(SignUpInformationEntry.this, LoginInformationEntry.class);
                        startActivity(intent);
                        Toast.makeText(SignUpInformationEntry.this, "Sign Up was successful!\nA verification link has been sent to "+Email+"\nPlease verify your Email Address and then Log in", Toast.LENGTH_LONG).show();

                        mAuth.signOut();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(SignUpInformationEntry.this, "Email Verification was not sent!", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

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


