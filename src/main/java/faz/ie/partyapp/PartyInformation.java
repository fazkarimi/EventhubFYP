/*
REFERENCE..
- https://firebase.google.com/docs/database/android/read-and-write
 */

package faz.ie.partyapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import faz.ie.partyapp.models.Party;

public class PartyInformation extends AppCompatActivity {

    private Button mNextButton;
    private EditText mPartyName,mHostName, mCounty,mPartyDescription, mEventLocationPostcode;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    String currentUser;

    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_information);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Log.v("partyApp",firebaseAuth.toString());
                final FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
                if (user != null)
                {
                    Intent intent = new Intent(PartyInformation.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };

        currentUser = mAuth.getCurrentUser().getUid();
        mNextButton = (Button)findViewById(R.id.nextButton);
        mHostName = (EditText) findViewById(R.id.hostNameTxtField);
        mCounty = (EditText) findViewById(R.id.countyTxtField);
        mPartyDescription = (EditText) findViewById(R.id.partyDescriptionTxtField);
        mPartyName = (EditText) findViewById(R.id.partyNameTxtField);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String PartyName = mPartyName.getText().toString().trim();
                final String HostName = mHostName.getText().toString().trim();
                final String County = mCounty.getText().toString().trim();
                final String PartyDescription = mPartyDescription.getText().toString().trim();
                //int selectID = mRadioButtonGroup.getCheckedRadioButtonId();

                if(PartyName.isEmpty()||HostName.isEmpty()||County.isEmpty()||PartyDescription.isEmpty())
                {
                    Toast.makeText(PartyInformation.this, "Not all Fields are filled!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Party partyInfo = new Party(PartyName,HostName,County,PartyDescription);

                String userID = mAuth.getCurrentUser().getUid();

                DatabaseReference currentUserDB1 = FirebaseDatabase.getInstance().getReference().child("Users")
                   .child(userID).child("Event Information");

                currentUserDB1.setValue(partyInfo);

                Intent intent = new Intent(PartyInformation.this, MainActivity.class);
                startActivity(intent);



            }
        });




    }
}
