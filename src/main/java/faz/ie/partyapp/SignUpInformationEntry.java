/*
REFERENCE..
- https://www.youtube.com/watch?v=JDnaV7f-eNI
 */

package faz.ie.partyapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpInformationEntry extends AppCompatActivity {

    private Button mSignUpButton;
    private EditText mEmail, mPassword, mFullName, mPhoneNumber, mGender, mAge;

    private RadioGroup mRadioButtonGroup;

    private RadioButton lookForAPartyRadioButton;


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
                    Intent intent = new Intent(SignUpInformationEntry.this, MainActivity.class);
                    startActivity(intent);
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
        lookForAPartyRadioButton = (RadioButton) findViewById(R.id.look);

        mRadioButtonGroup = (RadioGroup) findViewById(R.id.radioButtonGroup);

        mRadioButtonGroup.check(lookForAPartyRadioButton.getId());


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

                if(Email.isEmpty()||Password.isEmpty()||FullName.isEmpty()||PhoneNumber.isEmpty()||Gender.isEmpty()||Age.isEmpty())
                {
                    Toast.makeText(SignUpInformationEntry.this, "Not all Fields are filled!", Toast.LENGTH_SHORT).show();
                    return;
                }

                final int selectID = mRadioButtonGroup.getCheckedRadioButtonId();

                final RadioButton radioButton = (RadioButton) findViewById(selectID);

                mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(SignUpInformationEntry.this, new OnCompleteListener<AuthResult>()
                {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {

                            if(selectID==R.id.look) {
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
                                userInfo.put("profileImageUrl", "default");
                                currentUserDB.updateChildren(userInfo);

                               /* if(!Gender.equals("male") || !Gender.equals("Male") || !Gender.equals("female") ||!Gender.equals("Female"))
                                {

                                    Toast.makeText(SignUpInformationEntry.this, "Please enter a valid gender", Toast.LENGTH_SHORT).show();
                                }
                                else if(Age.equals("15") || Age.equals("14") || Age.equals("13") || Age.equals("12") || Age.equals("11") || Age.equals("10") || Age.equals("9") || Age.equals("8") || Age.equals("7") || Age.equals("6") || Age.equals("5") || Age.equals("4") || Age.equals("3") || Age.equals("2") || Age.equals("1") || Age.equals("0"))
                                {

                                    Toast.makeText(SignUpInformationEntry.this, "You must be 16 or over", Toast.LENGTH_SHORT).show();
                                }*/

                                Toast.makeText(SignUpInformationEntry.this, "Signed Up Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpInformationEntry.this, MainActivity.class);
                                startActivity(intent);
                            }

                            else if(selectID==R.id.host)
                            {
                                String userID = mAuth.getCurrentUser().getUid();

                                DatabaseReference currentUserDB = FirebaseDatabase.getInstance().getReference().child("Users")
                                        .child(userID);

                                   /* DatabaseReference currentUserDB2 = FirebaseDatabase.getInstance().getReference().child("Users")
                                             .child(userID);

                                    DatabaseReference currentUserDB3 = FirebaseDatabase.getInstance().getReference().child("Users")
                                            .child(userID);

                                    DatabaseReference currentUserDB4 = FirebaseDatabase.getInstance().getReference().child("Users")
                                             .child(userID);

                                    DatabaseReference currentUserDB5 = FirebaseDatabase.getInstance().getReference().child("Users")
                                           .child(userID);

                                    DatabaseReference currentUserDB6 = FirebaseDatabase.getInstance().getReference().child("Users")
                                           .child(userID);*/

                                Map userInfo = new HashMap<>();
                                userInfo.put("FullName", FullName);
                                userInfo.put("PhoneNumber", PhoneNumber);
                                userInfo.put("Email", Email);
                                userInfo.put("Gender", Gender);
                                userInfo.put("Age", Age);
                                userInfo.put("Password", Password);
                                userInfo.put("userType", radioButton.getText().toString());
                                userInfo.put("profileImageUrl", "default");


                                currentUserDB.updateChildren(userInfo);

                              /*  if(!Gender.equals("male") || !Gender.equals("Male") || !Gender.equals("female") ||!Gender.equals("Female"))
                                {
                                    Toast.makeText(SignUpInformationEntry.this, "Please enter a valid gender", Toast.LENGTH_SHORT).show();
                                }
                                else if(Age.equals("15") || Age.equals("14") || Age.equals("13") || Age.equals("12") || Age.equals("11") || Age.equals("10") || Age.equals("9") || Age.equals("8") || Age.equals("7") || Age.equals("6") || Age.equals("5") || Age.equals("4") || Age.equals("3") || Age.equals("2") || Age.equals("1") || Age.equals("0"))
                                {
                                    Toast.makeText(SignUpInformationEntry.this, "You must be 16 or over \n Please enter valid age", Toast.LENGTH_SHORT).show();
                                }
                                  /*  currentUserDB2.updateChildren(userInfo);
                                    currentUserDB3.updateChildren(userInfo);
                                    currentUserDB4.updateChildren(userInfo);
                                    currentUserDB5.updateChildren(userInfo);
                                    currentUserDB6.updateChildren(userInfo);*/

                                Toast.makeText(SignUpInformationEntry.this, "Enter your events information", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpInformationEntry.this, PartyInformation.class);
                                startActivity(intent);
                            }

                        }
                        else
                        {
                            Toast.makeText(SignUpInformationEntry.this, "Sign Up was not successful!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                myProgressDialog.setMessage("Signing Up...");
                myProgressDialog.show();
            }

        });


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


