package faz.ie.partyapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class chooseToHostOrAttend extends AppCompatActivity {

//private  Button mHostBtn, mAttendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_to_host_or_attend);

      /*  mHostBtn = (Button)findViewById(R.id.hostBtn);
        mAttendBtn = (Button)findViewById(R.id.attendBtn);

        mAttendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(chooseToHostOrAttend.this,SignUpInformationEntry.class);
                startActivity(intent);
            }
        });

        mHostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(chooseToHostOrAttend.this,PartyInformation.class);
                startActivity(intent);
            }
        });*/


    }
}
