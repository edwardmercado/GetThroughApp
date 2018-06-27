package com.example.user.getthroughapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnRegister;
    private EditText txtemail, txtpass, txtconfirmpassw;
    private TextView linkSignin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();


        btnRegister = (Button)findViewById(R.id.btnReg);
        txtemail = findViewById(R.id.txtemail);
        txtpass = findViewById(R.id.txtpassw);
        txtconfirmpassw = findViewById(R.id.txtconfirmpassw);
        linkSignin = findViewById(R.id.linkSign);

        btnRegister.setOnClickListener(this);

        txtpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtconfirmpassw.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        linkSignin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnRegister){
            registerUser();
        }
        if(v == linkSignin){
            Login();
        }
    }
    private void registerUser(){
        String email = txtemail.getText().toString().trim();
        String passw = txtpass.getText().toString().trim();
        String confirmpassw = txtconfirmpassw.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_SHORT).show();
            txtemail.requestFocus();
            return;
        }
        if(passw.length() < 6){
            Toast.makeText(this,"Password: Minimum of 6 characters",Toast.LENGTH_SHORT).show();
            txtpass.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(passw)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            txtpass.requestFocus();
            //Stopping the function executing more
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Input valid email",Toast.LENGTH_SHORT).show();
            txtemail.requestFocus();
            return;
        }
        if(!passw.equals(confirmpassw)){
            Toast.makeText(this,"Password does not match",Toast.LENGTH_SHORT).show();
            txtconfirmpassw.requestFocus();
            return;
        }

        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,passw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(RegistrationActivity.this,"Registration Succesfull",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(RegistrationActivity.this,RegistrationActivity2.class);
                            finish();
                            startActivity(i);
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(RegistrationActivity.this,"Registration Failed, Please try again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }
    public void Login(){
        Intent i = new Intent(RegistrationActivity.this,LoginActivity.class);
        finish();
        startActivity(i);
    }

}
