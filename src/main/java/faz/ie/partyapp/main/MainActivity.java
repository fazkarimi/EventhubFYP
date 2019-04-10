
/*REFERENCE..
- https://www.youtube.com/watch?v=SJW_4UMXbu8
- https://developer.android.com/guide/topics/ui/menus.html
- https://developer.android.com/reference/android/widget/ArrayAdapter.html
*/



package faz.ie.partyapp.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

import faz.ie.partyapp.R;
import faz.ie.partyapp.registationAndAuthentication.LoginORSignup;
import faz.ie.partyapp.matches.Matches;
import faz.ie.partyapp.models.User;
import faz.ie.partyapp.models.arrayAdapter;
import faz.ie.partyapp.settings.SettingsActivity;
import faz.ie.partyapp.settings.UserProfile;

public class MainActivity extends AppCompatActivity {

    Dialog myDialog;
    User user_data[];

    private arrayAdapter mArrayAdapter;
    private arrayAdapter mEventArrayAdapter;
    private int i;

    private String currentUid;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    ListView listView;
    List<User> rowItems;

    private DatabaseReference userTypeDB;
   // private DatabaseReference matchesDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);

        //getSupportActionBar().setDisplayShowHomeEnabled(true);
       // getSupportActionBar().setLogo(R.drawable.logo3);
       // getSupportActionBar().setDisplayUseLogoEnabled(true);
        setTitle("Eventhub");

        myDialog = new Dialog(this);

        userTypeDB = FirebaseDatabase.getInstance().getReference().child("Users");
        //matchesDB = FirebaseDatabase.getInstance().getReference().child("Chat");

        mAuth = FirebaseAuth.getInstance();

        currentUid = mAuth.getCurrentUser().getUid();
        checkUseType();
        rowItems = new ArrayList<User>();

        mArrayAdapter = new arrayAdapter(this, R.layout.item, rowItems);
        //mEventArrayAdapter = new arrayAdapter(this, R.layout.eventitem, rowItems);


        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(mArrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                rowItems.remove(0);
                mArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
            User userInfo =(User) dataObject;
                String userid = userInfo.getUserId();
                userTypeDB.child(userid).child("Connections").child("Not Interested").child(currentUid).setValue(true);
               // Toast.makeText(MainActivity.this, "Not Interested!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRightCardExit(Object dataObject) {
                User userInfo =(User) dataObject;
                String userid = userInfo.getUserId();
                userTypeDB.child(userid).child("Connections").child("Interested").child(currentUid).setValue(true);
                //Toast.makeText(MainActivity.this, "Interested!", Toast.LENGTH_SHORT).show();
                isConnectionMatch(userid);
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

               // Toast.makeText(MainActivity.this,"You are about to run out of swipes!",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override

            // code here for when the user taps on the image, it goes to next image...

            public void onItemClicked(int itemPosition, Object dataObject) {

                //Toast.makeText(MainActivity.this, "Next Picture...", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showPopUpDialog()
    {
        Button messageBtn;
        Button keepSwippingBtn;

        myDialog.setContentView(R.layout.newmatchpopup);

        keepSwippingBtn = (Button) myDialog.findViewById(R.id.btnKeepSwipping);
        messageBtn = (Button) myDialog.findViewById(R.id.btnMessage);

        keepSwippingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }

    private void isConnectionMatch(String userid)
    {
        DatabaseReference currentUserConnection = userTypeDB.child(currentUid).child("Connections").child("Interested").child(userid);

        currentUserConnection.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    showPopUpDialog(); //new match pop up dialog
                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey(); // creates a new child named Users with a unique ID
                    //String key = matchesDB.push().getKey();
                    userTypeDB.child(dataSnapshot.getKey()).child("Connections").child("Matches").child(currentUid).child("ChatId").setValue(key); // adds a child "ChatId" to matches
                    userTypeDB.child(currentUid).child("Connections").child("Matches").child(dataSnapshot.getKey()).child("ChatId").setValue(key);

                    //trigger email to be sent


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    private String mUserType;
    private String otherUsertype;

    public void checkUseType()//need to reference sim coder here
    {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference userDb = userTypeDB.child(user.getUid());

        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists()) {
                    if (dataSnapshot.child("userType").getValue() != null) {
                        mUserType = dataSnapshot.child("userType").getValue().toString();

                        switch (mUserType) {
                            case "Attend an event":
                                otherUsertype = "Host an event";
                                break;

                            case "Host an event":
                                otherUsertype = "Attend an event";
                                break;

                        }
                        getOppositeUsertypeUsers();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    public void getOppositeUsertypeUsers() {
        userTypeDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
                {
                    if (dataSnapshot.exists() && !dataSnapshot.child("Connections").child("Not Interested").hasChild(currentUid) && !dataSnapshot.child("Connections").child("Interested").hasChild(currentUid) && dataSnapshot.child("userType").getValue().toString().equals(otherUsertype))
                    {
                        String profileImageUrl = "defaultUserImage";
                        if(!dataSnapshot.child("profileImageUrl").getValue().equals("defaultUserImage")) //if the profile imagfe url doesnt equal to "defaultUserImage"
                        {
                            profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString(); //set this profile picture
                        }

                            if (dataSnapshot.child("FullName").getValue() != null && dataSnapshot.child("Gender").getValue() != null && dataSnapshot.child("Age").getValue() != null && dataSnapshot.child("profileImageUrl").getValue() != null) {
                                User item = new User(dataSnapshot.getKey(), dataSnapshot.child("FullName").getValue().toString(), dataSnapshot.child("Gender").getValue().toString(), dataSnapshot.child("Age").getValue().toString(), profileImageUrl);
                                rowItems.add(item);
                                mArrayAdapter.notifyDataSetChanged();
                            }



                    }
                }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.home:
                onBackPressed();
                break;
            case R.id.menuSignOut:
                FirebaseAuth.getInstance().signOut();
                Intent intent3 = new Intent(MainActivity.this, LoginORSignup.class);
                startActivity(intent3);
                Toast.makeText(MainActivity.this, "Signed out successfully", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            case R.id.menuSettings:
                Intent intent2 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent2);
                break;

            case R.id.action_matches:
                Intent intent4 = new Intent(MainActivity.this, Matches.class);
                startActivity(intent4);
                    break;

            case R.id.action_profile :
                Intent intent5 = new Intent(MainActivity.this, UserProfile.class);
                startActivity(intent5);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_party_app, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }
}

   /* private void showPopUpDialog()
    {
            Button messageBtn;
            Button keepSwippingBtn;

            myDialog.addContentView(R.layout.newmatchpopup);

            keepSwippingBtn = (Button) myDialog.findViewById(R.id.btnKeepSwipping);
            messageBtn = (Button) myDialog.findViewById(R.id.btnMessage);

            keepSwippingBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myDialog.dismiss();
                }
            });

            messageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myDialog.dismiss();
                }
            });
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();


    }*/