package faz.ie.partyapp.matches;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import faz.ie.partyapp.LoginORSignup;
import faz.ie.partyapp.MainActivity;
import faz.ie.partyapp.R;
import faz.ie.partyapp.SettingsActivity;
import faz.ie.partyapp.UserProfile;

public class Matches extends AppCompatActivity {
    public RecyclerView mRecyclerView;
    public RecyclerView.Adapter mMatchesAdapter;
    public RecyclerView.LayoutManager mMatchesLayoutManager;

    private String currentUserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);
        setTitle("My Matches");

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mMatchesLayoutManager = new LinearLayoutManager(Matches.this);
        mRecyclerView.setLayoutManager(mMatchesLayoutManager);
        mMatchesAdapter = new MatchesAdapter(getdataSetMatches(), Matches.this);
        mRecyclerView.setAdapter(mMatchesAdapter);

        getUserMatchID();



    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.home:
                onBackPressed();
                break;
            case R.id.menuSignOut:
                FirebaseAuth.getInstance().signOut();
                Intent intent3 = new Intent(Matches.this, LoginORSignup.class);
                startActivity(intent3);
                Toast.makeText(Matches.this, "Signed out successfully", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            case R.id.menuSettings:
                Intent intent2 = new Intent(Matches.this, SettingsActivity.class);
                startActivity(intent2);
                break;

            case R.id.action_main:
                Intent intent4 = new Intent(Matches.this, MainActivity.class);
                startActivity(intent4);
                break;

            case R.id.action_profile:
                Intent intent5 = new Intent(Matches.this, UserProfile.class);
                startActivity(intent5);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.matches_activity_menu, menu);
        return true;
    }

    private void getUserMatchID()
    {
        DatabaseReference matchDB = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID)
                .child("Connections").child("Matches");

        matchDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot match : dataSnapshot.getChildren())
                    {
                        fetchMatchInformation(match.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void fetchMatchInformation(String key)
    {
        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        userDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String userId = dataSnapshot.getKey();
                    String FullName = "";
                    String profileImageUrl = "";

                    if(dataSnapshot.child("FullName").getValue() != null)
                    {
                        FullName = dataSnapshot.child("FullName").getValue().toString();
                        profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                    }
                    MatchesObject obj = new MatchesObject(FullName,profileImageUrl);
                    resultMatches.add(obj);
                    mMatchesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<MatchesObject> resultMatches = new ArrayList<MatchesObject>();
    private List<MatchesObject> getdataSetMatches()
    {
        return resultMatches;
    }

}
