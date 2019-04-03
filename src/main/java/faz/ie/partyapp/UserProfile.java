/*
REFERENCE..

https://www.youtube.com/watch?v=eJ0OFxR4xFw
 */

package faz.ie.partyapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.io.Console;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import faz.ie.partyapp.models.User;


public class UserProfile extends AppCompatActivity {

    ImageView image;
    ProgressDialog progressDialog;
    private EditText mPhoneNumberTextView, mFullNameTextView;

    private Button deleteButton;
    private Button updateButton;

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
                //finish();

            }
        });

    deleteButton.setOnClickListener(new View.OnClickListener()
    {
    @Override
    public void onClick(View view) {
        deleteAccount(view);
    }
});


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserInformation();
                //Toast.makeText(UserProfile.this, "Information Updated", Toast.LENGTH_SHORT).show();
            }
        });

    }

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
                       // Glide.with(getApplication()).load(profileImageUrl).into(mProfileImage);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

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
                            Toast.makeText(UserProfile.this, "Information changed successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            });

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //finish();
                    Toast.makeText(UserProfile.this, "error!!!", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
           finish();

        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void deleteAccount(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(UserProfile.this, "Account Deleted!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UserProfile.this, LoginORSignup.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(UserProfile.this, "Unable to Delete Account", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri ImageUri = data.getData();
            resultURI = ImageUri;
            mProfileImage.setImageURI(resultURI);

        }
    }


}
//




        /*mAgeTextView = (EditText)findViewById(R.id.updateAgeTxt);
        mGenderTextView = (EditText)findViewById(R.id.updateGenderTxt);
        mPhoneNumberTextView = (EditText)findViewById(R.id.updatePhoneTxt);
        mFullNameTextView = (EditText)findViewById(R.id.updateNameTxt);
        mEmailTextView = (EditText)findViewById(R.id.updateEmailTxt);*/


            /*displayUserInformation();

        saveUserInformatio();

    }

    private void displayUserInformation()
    {
      mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot)
          {
                if(dataSnapshot.exists()&& dataSnapshot.getChildrenCount()>0)
                {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("FullName") !=null)
                    {
                        FullName = map.get("FullName").toString();
                        mFullNameTextView.setText(FullName);
                    }

                    if(map.get("Age") !=null)
                    {
                        Age = map.get("Age").toString();
                        mAgeTextView.setText(Age);
                    }

                    if(map.get("Email") !=null)
                    {
                        Email = map.get("Email").toString();
                   }

                    if(map.get("Gender") !=null)
                    {
                        Gender = map.get("Gender").toString();
                        mGenderTextView.setText(Gender);
                    }         mEmailTextView.setText(Email);


                    if(map.get("PhoneNumber") !=null)
                    {
                        PhoneNumber = map.get("PhoneNumber").toString();
                        mPhoneNumberTextView.setText(PhoneNumber);
                    }

                }
          }

          @Override
          public void onCancelled(DatabaseError databaseError)
          {

          }
      });*/




