package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button loginbttn = (Button) findViewById(R.id.loginbuton);
        Button continuebtn = (Button) findViewById(R.id.continuebtn);
        loginbttn.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View V){
                Openloginpage();
            }
        });

        continuebtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Opencontinuepage();
            }
        });


    }

    public void Opencontinuepage(){
        Intent intent;
        intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
    public void Openloginpage(){
        Intent intent;
        intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}