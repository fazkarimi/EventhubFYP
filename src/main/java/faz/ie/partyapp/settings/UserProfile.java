/*
REFERENCE..

https://www.youtube.com/watch?v=eJ0OFxR4xFw
 */

package faz.ie.partyapp.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import faz.ie.partyapp.R;
import faz.ie.partyapp.chat.Chat;
import faz.ie.partyapp.main.MainActivity;
import faz.ie.partyapp.matches.Matches;
import faz.ie.partyapp.registationAndAuthentication.LoginORSignup;
import faz.ie.partyapp.models.User;


public class UserProfile extends AppCompatActivity {

    ImageView image;
    ProgressDialog progressDialog;
    private EditText mPhoneNumberTextView, mFullNameTextView;

    private Button deleteButton;
    private Button updateButton;
    private ProgressDialog myProgressDialog2;
    private ImageView mProfileImage;

    //String mUserType = getIntent().getExtras().getString("userType");

    private String userID, FullName, profileImageUrl, PhoneNumber, mUserType;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser myUser;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserDatabase;


    private Uri resultURI;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        myProgressDialog2 = new ProgressDialog(this);
        setTitle("My Profile");

        progressDialog = new ProgressDialog(this);

        mProfileImage = (ImageView) findViewById(R.id.profileImage);

        deleteButton = (Button) findViewById(R.id.deleteButton);
        updateButton = (Button) findViewById(R.id.updateButton);

        mPhoneNumberTextView = (EditText) findViewById(R.id.updatePhoneTxt);
        mFullNameTextView = (EditText) findViewById(R.id.updateNameTxt);

        mAuth = FirebaseAuth.getInstance();

        userID = mAuth.getCurrentUser().getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        displayUserInformation();

         mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);


            }
        });

    deleteButton.setOnClickListener(new View.OnClickListener()
    {
    @Override
    public void onClick(View view) {

        deletAccountAlertDialog(view);
    }
});


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myProgressDialog2.setMessage("Updating Information...");
                myProgressDialog2.show();
                updateUserInformation();
            }
        });

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void displayUserInformation() {
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("FullName") != null) {
                        FullName = map.get("FullName").toString();
                        mFullNameTextView.setText(FullName);
                    }
                    if (map.get("PhoneNumber") != null) {
                        PhoneNumber = map.get("PhoneNumber").toString();
                        mPhoneNumberTextView.setText(PhoneNumber);
                    }
                    Glide.clear(mProfileImage);
                    if (map.get("profileImageUrl") != null)
                    {
                        profileImageUrl = map.get("profileImageUrl").toString();

                        switch (profileImageUrl)
                        {
                            case "defaultUserImage":
                                Glide.with(getApplication()).load(R.mipmap.user).into(mProfileImage);
                                break;
                            default:

                                Glide.with(getApplication()).load(profileImageUrl).into(mProfileImage);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void updateUserInformation() {
        FullName = mFullNameTextView.getText().toString();
        PhoneNumber = mPhoneNumberTextView.getText().toString();



            Map userinfo = new HashMap();
            userinfo.put("FullName", FullName);
            userinfo.put("PhoneNumber", PhoneNumber);
            mUserDatabase.updateChildren(userinfo);

        if (resultURI != null)
        {
            final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userID);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultURI);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = filePath.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            Map userinfo = new HashMap();
                            userinfo.put("profileImageUrl",  uri.toString());
                            mUserDatabase.updateChildren(userinfo);
                            Toast.makeText(UserProfile.this, "Information Updated", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            });

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(UserProfile.this, "error!!!", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
           finish();

        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void deleteAccount(final View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {

                        Toast.makeText(UserProfile.this, "Account Deleted", Toast.LENGTH_SHORT).show();


                    } else
                    {
                        Toast.makeText(UserProfile.this, "Unable to Delete Account", Toast.LENGTH_SHORT).show();
                        myProgressDialog2.dismiss();
                    }
                }
            });
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri ImageUri = data.getData();
            resultURI = ImageUri;
            mProfileImage.setImageURI(resultURI);

        }
    }

    public void deletAccountAlertDialog (final View view)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
        builder.setTitle("Delete this Account");
        builder.setMessage("Are you sure you want to delete this account?All saved data will be lost. This action cannot be undone");
        builder.setCancelable(false);

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                deleteAccount(view);
                myProgressDialog2.setMessage("Deleting Account...");
                myProgressDialog2.show();
                Intent intent = new Intent(UserProfile.this, LoginORSignup.class);
                startActivity(intent);
                finish();
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
    public void signOutAlertDialog ()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
        builder.setTitle("Sign Out");
        builder.setMessage("Are you sure you want to Sign out?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {

                FirebaseAuth.getInstance().signOut();
                Intent intent3 = new Intent(UserProfile.this, LoginORSignup.class);
                startActivity(intent3);
                Toast.makeText(UserProfile.this, "Signed out successfully", Toast.LENGTH_SHORT).show();
                finish();

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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.home:
                onBackPressed();
                break;
            case R.id.menuSignOut:
                signOutAlertDialog();
                return true;
            case R.id.menuSettings:
                Intent intent2 = new Intent(UserProfile.this, SettingsActivity.class);
                startActivity(intent2);
                break;

            case R.id.action_main:
                Intent intent4 = new Intent(UserProfile.this, MainActivity.class);
                startActivity(intent4);
                break;
            case R.id.action_matches:
                Intent intent5 = new Intent(UserProfile.this, Matches.class);
                startActivity(intent5);
                break;

            default:
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_activity_menu, menu);
        return true;
    }

}


