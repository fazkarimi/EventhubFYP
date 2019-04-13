package faz.ie.partyapp.chat;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import faz.ie.partyapp.R;
import faz.ie.partyapp.main.MainActivity;
import faz.ie.partyapp.matches.Matches;
import faz.ie.partyapp.matches.MatchesObject;
import faz.ie.partyapp.registationAndAuthentication.LoginORSignup;
import faz.ie.partyapp.settings.SettingsActivity;
import faz.ie.partyapp.settings.UserProfile;

/*
Author Faz
 */
public class Chat extends AppCompatActivity {

    public RecyclerView mRecyclerView;
    public RecyclerView.Adapter mChatAdapter;
    public RecyclerView.LayoutManager mChatLayoutManager;
    public TextView nameInActionBarTextView;
    private String currentUserID, chatId;
    public String matchId;
    private EditText mSendEditText;
    private Button mSendButton;
    private NestedScrollView mScrollView;
    private TextView mImageIdTextView;

    DatabaseReference mDatabaseUser, mDatabaseChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle("Chat Messenger");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mScrollView =  (NestedScrollView) findViewById(R.id.scrollView);
        //mImageIdTextView = (TextView) findViewById(R.id.matchId);

        mScrollView.fullScroll(NestedScrollView.FOCUS_DOWN);

        currentUserID  = FirebaseAuth.getInstance().getCurrentUser().getUid();

        matchId = getIntent().getExtras().getString("matchId");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("Connections").child("Matches").child(matchId).child("ChatId");
        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat");

        getChatId();

        mSendButton =  findViewById(R.id.sendButton);
        mSendEditText = findViewById(R.id.message);


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
        mChatLayoutManager = new LinearLayoutManager(Chat.this);
        mRecyclerView.setLayoutManager(mChatLayoutManager);
        mChatAdapter = new ChatAdapter(getdataSetChat(), Chat.this);
        mRecyclerView.setAdapter(mChatAdapter);

        mSendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                sendMessage();
                //mScrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
            }
        });

    }

    private void sendMessage()
    {
        String sendMessageText = mSendEditText.getText().toString();

        if(!sendMessageText.isEmpty()){
            DatabaseReference newMessageDb = mDatabaseChat.push();
            Map newMessage = new HashMap();
            newMessage.put("createdByUser", currentUserID);
            newMessage.put("text", sendMessageText);
            newMessageDb.setValue(newMessage);
        }
        else
        {
            Toast.makeText(Chat.this, "Enter a message before sending", Toast.LENGTH_SHORT).show();
        }
        mSendEditText.setText(null); //emptying textfield after a message is sent
    }

    private void getChatId()
    {
        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists()){
                    chatId = dataSnapshot.getValue().toString();
                    mDatabaseChat = mDatabaseChat.child(chatId);
                    getChatMessages();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    private void getChatMessages()
    {
        mDatabaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    String message = null;
                    String createdByUser = null;

                    if(dataSnapshot.child("text").getValue()!=null){
                        message = dataSnapshot.child("text").getValue().toString();
                    }
                    if(dataSnapshot.child("createdByUser").getValue()!=null){
                        createdByUser = dataSnapshot.child("createdByUser").getValue().toString();
                    }


                    mScrollView.fullScroll(NestedScrollView.FOCUS_DOWN);

                    if(message!=null && createdByUser!=null)
                    {
                        Boolean currentUserBoolean = false;

                        if(createdByUser.equals(currentUserID))
                        {
                            currentUserBoolean = true;
                        }
                        ChatObject newMessage = new ChatObject(message, currentUserBoolean);
                        resultsChat.add(newMessage);
                        mChatAdapter.notifyDataSetChanged();
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
            case R.id.action_flag:
                Intent intent3 = new Intent(Chat.this, Chat.class);
                startActivity(intent3);
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_activity_menu, menu);
        return true;
    }


    private ArrayList<ChatObject> resultsChat = new ArrayList<ChatObject>();
    private List<ChatObject> getdataSetChat()
    {
        return resultsChat;
    }

}
