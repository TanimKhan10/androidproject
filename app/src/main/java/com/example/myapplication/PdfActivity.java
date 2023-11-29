package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class PdfActivity extends AppCompatActivity {

    ImageButton back;
    ImageButton attach;
    Button upbut;
    EditText cat;

    EditText booktitle;
    EditText decrip;

    private ArrayList<String> categoryTitleArrayList,categoryIdArrayList;


    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    private static final String TAG = "ADD_PDF_TAG";

    private static final int PDF_PICK_CODE=1000;

    private Uri pdfUri=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        firebaseAuth =FirebaseAuth.getInstance();

        back=findViewById(R.id.backinhome);
        attach=findViewById(R.id.attachbutton);
        upbut=findViewById(R.id.bookupbutton);

        booktitle=findViewById(R.id.titleenter);
        decrip=findViewById(R.id.Descriptionenter);
        cat=findViewById(R.id.categoryenter);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        upbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateothers();
            }
        });


        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdfpickintent();
            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
            }
        });
    }
    private String title="",description="",category="";
    private void validateothers() {
        Log.d(TAG,"validate: validate data..");
        title=booktitle.getText().toString().trim();
        description=decrip.getText().toString().trim();
        category=cat.getText().toString().trim();

        if(TextUtils.isEmpty(title)){
            Toast.makeText(this, "Enter title", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(description)){
            Toast.makeText(this, "Enter Description", Toast.LENGTH_SHORT).show();
        } else if (pdfUri==null) {
            Toast.makeText(this, "pick pdf", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(category)){
            Toast.makeText(this, "Enter category", Toast.LENGTH_SHORT).show();
        }
        else{
            uploadall();
        }

    }

    private void uploadall() {
        Log.d(TAG,"uploadpdfsorage: uploading to storage");
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        long timestamp=System.currentTimeMillis();

        String filepathAndName="Book/" +timestamp;

        StorageReference storageReference= FirebaseStorage.getInstance().getReference(filepathAndName);
        storageReference.putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG,"onSuccess:pdf uploaded to storage");
                        Log.d(TAG,"onsuccess;getting pdf url");

                        Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        String uploadedpdfUrl=""+uriTask.getResult();
                        uppdftobase(uploadedpdfUrl,timestamp);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.d(TAG,"onfailure:pdf upload failed due to"+e.getMessage());
                        Toast.makeText(PdfActivity.this, "pdf upload failed due to"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void uppdftobase(String uploadedpdfUrl, long timestamp) {
        Log.d(TAG,"uploadpdftostorage: uploading pdf into to firebase db...");
        progressDialog.setMessage("Uploading pdf info..");
        String uid =firebaseAuth.getUid();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("uid",""+uid);
        hashMap.put("id",""+timestamp);
        hashMap.put("title",""+title);
        hashMap.put("description",""+description);
        hashMap.put("category",""+category);
        hashMap.put("url",""+uploadedpdfUrl);
        hashMap.put("timestamp",timestamp);

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Book");
        ref.child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Log.d(TAG,"onSuccess:successfully uploaded");
                        Toast.makeText(PdfActivity.this, "successfully uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.d(TAG,"onfailure: Failed to upload to db due to"+e.getMessage());
                        Toast.makeText(PdfActivity.this, "Failed to upload to db due to"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void pdfpickintent() {
        Log.d(TAG, "pdfPickIntent: starting pdf pick intent");
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select pdf"), PDF_PICK_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode==PDF_PICK_CODE){
                Log.d(TAG,"onActivityResult:PDF PICKED");
                    pdfUri=data.getData();
                    Log.d(TAG,"onActivityResult: URI: "+pdfUri);
            }
        }else{
                Log.d(TAG,"onActivityResult:cancelled picking pdf");
            Toast.makeText(this, "cancelled picking pdf", Toast.LENGTH_SHORT).show();

        }
    }
}