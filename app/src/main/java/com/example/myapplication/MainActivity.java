package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.ClosedSubscriberGroupInfo;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.widget.EditText username = (android.widget.EditText) findViewById(R.id.editTextText);
        android.widget.EditText password = (android.widget.EditText) findViewById(R.id.editTextTextPassword);
        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.button);
        loginbtn.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View V){
                Openloginpage();
            }
        });


    }
    public void Openloginpage(){
        Intent intent;
        intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }
}