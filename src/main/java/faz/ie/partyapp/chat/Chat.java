package faz.ie.partyapp.chat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    public String [] listItems;
    public boolean [] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    public String matchId;
    private EditText mSendEditText;
    private Button mSendButton;
    private NestedScrollView mScrollView;
    private TextView mImageIdTextView;
    private int counter;
    private FirebaseAuth mAuth;
    private ProgressDialog myProgressDialog2;
    DatabaseReference mDatabaseUser, mDatabaseChat, mDatabaseFlaggedUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myProgressDialog2 = new ProgressDialog(this);
        setTitle("Chat Messenger");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mScrollView =  (NestedScrollView) findViewById(R.id.scrollView);
        listItems = getResources().getStringArray(R.array.reasons_item);
        checkedItems = new boolean[listItems.length];
        currentUserID  = FirebaseAuth.getInstance().getCurrentUser().getUid();
        matchId = getIntent().getExtras().getString("matchId");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("Connections").child("Matches").child(matchId).child("ChatId");
        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat");
        mDatabaseFlaggedUsers = FirebaseDatabase.getInstance().getReference().child("Flagged Users");
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
        mSendEditText.setText(null);
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
            { }
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
                        boolean currentUserBoolean = false;

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
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                onBackPressed();
                break;
            case R.id.Unmatch:
                unmatchAlertDialog();
                break;
            case R.id.clearConvo:
                clearChatAlertDialog();
                break;
            case R.id.flagUser:
                flagUserDialog();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void unmatchFlaggedUser()
    {

        //CURRENT USER DB
        DatabaseReference matchedUsersDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("Connections").child("Matches");
        DatabaseReference interestedUsersDB = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("Connections").child("Interested");

        //UNMATCHING USER DB
        DatabaseReference unmatchUsersDB = FirebaseDatabase.getInstance().getReference().child("Users").child(matchId).child("Connections").child("Matches");
        DatabaseReference unmatchUsersDBForInterested = FirebaseDatabase.getInstance().getReference().child("Users").child(matchId).child("Connections").child("Interested");

        //CHAT DB
        DatabaseReference ChatDB = FirebaseDatabase.getInstance().getReference().child("Chat");

        if (matchedUsersDb != null && interestedUsersDB!=null && unmatchUsersDB!=null && unmatchUsersDBForInterested!=null && ChatDB!=null )
        {
            //REMOVING CHATS BETWEEN 2 USERS
             ChatDB.child(chatId).removeValue();

            //REMOVING FLAGGED USER FROM CURRENT USERS MATCHES LIST
            matchedUsersDb.child(matchId).removeValue();
            interestedUsersDB.child(matchId).removeValue();

            //REMOVING CURRENT USER FROM FLAGGED USERS MATCHES LIST AND INTEREST LIST
            unmatchUsersDB.child(currentUserID).removeValue();
            unmatchUsersDBForInterested.child(currentUserID).removeValue();

            Intent intent = new Intent(Chat.this, MainActivity.class);
            startActivity(intent);
            finish();

        }
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

    public void flagUserDialog ()
    {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(Chat.this);
        mBuilder.setTitle(R.string.dialog_title);

        mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked)
            {
                AlertDialog dialog = mBuilder.create();
                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                          //(AlertDialog.BUTTON_NEUTRAL);
                if(isChecked)
                {
                    mUserItems.add(position);
                }else
                    {

                        Toast.makeText(Chat.this, "Please select a reason for the flagging of this user", Toast.LENGTH_LONG).show();
                    }
            }
        });
        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener()
        {
             String item = "";
            @Override
            public void onClick(DialogInterface dialogInterface, int which)
            {
                counter ++;

                for (int i = 0; i < mUserItems.size(); i++)
                {
                    item = item + listItems[mUserItems.get(i)];
                    if (i != mUserItems.size() - 1) {
                        item = item + ", ";
                    }
                }
                    for (int i = 0; i < checkedItems.length; i++) {
                    checkedItems[i] = false;
                    mUserItems.clear();
                }
                DatabaseReference userBeingFlaggedDB = FirebaseDatabase.getInstance().getReference().child("Flagged Users").child(matchId);
                DatabaseReference flaggingUser = FirebaseDatabase.getInstance().getReference().child("Flagged Users").child(matchId).child("FlaggedBy");
                DatabaseReference reasonsDB = FirebaseDatabase.getInstance().getReference().child("Flagged Users").child(matchId).child("Reasons");

                Map userInfo = new HashMap<>();
                userInfo.put("FlagCounter", counter);
                userBeingFlaggedDB.updateChildren(userInfo);

                Map currentUserInfo = new HashMap<>();
                currentUserInfo.put("FlaggedBy", currentUserID);
                flaggingUser.setValue(currentUserInfo);

                Map flagInfo = new HashMap<>();
                flagInfo.put("Reasons", item);
                reasonsDB.setValue(flagInfo);
                Toast.makeText(Chat.this, "Thank you for flagging this user,\nBy default, the will be deleted from you matches list", Toast.LENGTH_LONG).show();
                unmatchFlaggedUser();
            }
        });

        mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }
    public void unmatchAlertDialog ()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Chat.this);
        builder.setTitle("Unmatch this user");
        builder.setMessage("Are you sure you want to Unmatch this user?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                unmatchFlaggedUser();
                Toast.makeText(Chat.this, "User is unmatched", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.dismiss();
            }
        });
        AlertDialog mDialog = builder.create();
        mDialog.show();
    }
    public void clearChatAlertDialog ()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Chat.this);
        builder.setTitle("Delete this conversation");
        builder.setMessage("Are you sure you want to delete this conversation? This action cannot be undone");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                myProgressDialog2.setMessage("Deleting conversation...");
                myProgressDialog2.show();
                clearConversation();
                Intent intent = new Intent(Chat.this, Matches.class);
                startActivity(intent);
                Toast.makeText(Chat.this, "Conversation is deleted successfully", Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = builder.create();
        mDialog.show();
    }
    public void clearConversation ()
    {
        DatabaseReference ChatDB = FirebaseDatabase.getInstance().getReference().child("Chat");
        ChatDB.child(chatId).removeValue();
    }
}