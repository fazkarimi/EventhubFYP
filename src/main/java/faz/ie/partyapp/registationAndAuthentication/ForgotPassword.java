package faz.ie.partyapp.registationAndAuthentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import faz.ie.partyapp.R;
import faz.ie.partyapp.chat.Chat;
import faz.ie.partyapp.matches.Matches;

public class ForgotPassword extends AppCompatActivity
{
    public Button sendEmailButton;
    public EditText emailAddressEditText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setTitle("Forgot password");
        sendEmailButton = (Button) findViewById(R.id.sendMailButton);
        emailAddressEditText = (EditText) findViewById(R.id.forgotPassEmail);

        mAuth = FirebaseAuth.getInstance();

        sendEmailButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final String userEmailAddressEntered;
                userEmailAddressEntered = emailAddressEditText.getText().toString();

                if(TextUtils.isEmpty(userEmailAddressEntered))
                {
                    Toast.makeText(ForgotPassword.this, "Enter your email address before requesting a password change", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mAuth.sendPasswordResetEmail(userEmailAddressEntered).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(task.isSuccessful())
                            {
                                Intent intent = new Intent(ForgotPassword.this, LoginORSignup.class);
                                startActivity(intent);
                                Toast.makeText(ForgotPassword.this, "Email has been sent to " + userEmailAddressEntered, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                String message;
                                message = task.getException().getMessage();
                                Toast.makeText(ForgotPassword.this, "Email could not be sent! " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
