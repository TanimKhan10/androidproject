package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.checkerframework.checker.nullness.qual.NonNull;

public class LoginActivity extends AppCompatActivity {

    android.widget.EditText emaillog;
    android.widget.EditText passwordlog;
    MaterialButton loginbtn;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView signn;

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser= mAuth.getCurrentUser();
        if(currentUser!=null){
            Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signn = (TextView) findViewById(R.id.textView5);
        mAuth=FirebaseAuth.getInstance();
        emaillog= findViewById(R.id.maillet);
        passwordlog= findViewById(R.id.passwordletlet);
        loginbtn = findViewById(R.id.button);
        progressBar= findViewById(R.id.progressbarlog);


       signn.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View V){
                Opensignpage();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email,password;
                email= String.valueOf(emaillog.getText());
                password=String.valueOf(passwordlog.getText());

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(LoginActivity.this, "Enter mail", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> Task){
                                progressBar.setVisibility(View.GONE);
                                if (Task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                                startActivity(intent);
                                finish();
                                }else {
                                    Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });



    }
    public void Opensignpage(){
        Intent intent;
        intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
}

