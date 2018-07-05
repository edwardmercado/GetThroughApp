package com.example.user.getthroughapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity{
    private FirebaseAuth firebaseAuth;
    private TextView txtFullname, txtGender, txtStreet, txtCity, txtProvince, txtBirthday;
    private DatabaseReference firebaseDatabaseGender;
    private CircleImageView imgProfilePic;
    private Firebase mRef;
    public String address, street, city, province;
    private ProgressDialog progressDialog;
    private Button btnUpdate;
    private int showed = 0;

    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        progressDialog = new ProgressDialog(this);

        txtFullname = findViewById(R.id.txtFullname);
        txtGender = findViewById(R.id.txtGender);
        txtStreet = findViewById(R.id.txtStreet);
        txtCity = findViewById(R.id.txtCity);
        txtProvince = findViewById(R.id.txtProv);
        txtBirthday = findViewById(R.id.txtBirthday);

        btnUpdate = findViewById(R.id.btnUpdate);
        imgProfilePic = findViewById(R.id.imgProfilePic);


        loadUserInfo();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        Glide.with(this)
                .load(user.getPhotoUrl().toString())
                .into(imgProfilePic);
        String name = user.getDisplayName();
        txtFullname.setText(name);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,UpdateActivity.class));
            }
        });
    }



    public void loadUserInfo(){
        progressDialog.setMessage("Loading user info...");
        progressDialog.show();
        firebaseAuth = FirebaseAuth.getInstance();
        id = firebaseAuth.getCurrentUser().getUid();
        //Toast.makeText(this,id.toString(),Toast.LENGTH_LONG).show();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refGender = database.getReference(id).child("gender");
        refGender.keepSynced(true);
        refGender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user = dataSnapshot.getValue(String.class);
                //txtGender.setText(user.getGender());
                System.out.println(user);
                txtGender.setText(user.toString());
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference refStreet = database.getReference(id).child("street");
        refStreet.keepSynced(true);
        refStreet.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user = dataSnapshot.getValue(String.class);
                //txtGender.setText(user.getGender());
                System.out.println(user);
                txtStreet.setText(user.toString()+", ");

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference refCity = database.getReference(id).child("city");
        refCity.keepSynced(true);
        refCity.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user = dataSnapshot.getValue(String.class);
                //txtGender.setText(user.getGender());
                System.out.println(user);
                txtCity.setText(user.toString()+", ");
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference refProv = database.getReference(id).child("province");
        refProv.keepSynced(true);
        refProv.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user = dataSnapshot.getValue(String.class);
                //txtGender.setText(user.getGender());
                System.out.println(user);
                txtProvince.setText(user.toString());
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference refBday = database.getReference(id).child("birthday");
        refBday.keepSynced(true);
        refBday.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user = dataSnapshot.getValue(String.class);
                //txtGender.setText(user.getGender());
                System.out.println(user);
                txtBirthday.setText(user.toString());
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        progressDialog.dismiss();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(firebaseAuth.getCurrentUser()!=null && firebaseAuth.getCurrentUser().getDisplayName()==null&&firebaseAuth.getCurrentUser().getPhotoUrl()==null){
            finish();
            startActivity(new Intent(HomeActivity.this,RegistrationActivity2.class));
            Toast.makeText(this,"Please finish registraion first", Toast.LENGTH_SHORT).show();
        }

        final FirebaseUser user = firebaseAuth.getCurrentUser();

//        if(!user.isEmailVerified()){
//            final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
//            alertBuilder.setMessage("Verify Email to explore more features on this app")
//                    .setCancelable(false)
//                    .setPositiveButton("Verify", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(final DialogInterface dialog, int which) {
//                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    Toast.makeText(HomeActivity.this,"Email Verification Sent", Toast.LENGTH_SHORT).show();
//                                    dialog.cancel();
//                                }
//                            });
//
//                        }
//                    })
//                    .setNegativeButton("Later", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
//            AlertDialog alert = alertBuilder.create();
//            alert.setTitle("Email Verification");
//            alert.show();
//        }

        testQuote();

    }

    public void Logout(View v){
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Do you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.setTitle("Logout");
        alert.show();
    }

    private void showUpdateDialog(){
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        final View dialogView = inflater.inflate(R.layout.update_dialog,null);
//        dialogBuilder.setView(dialogView);
//
//        final EditText txtFname = dialogView.findViewById(R.id.txtFname);
//        final EditText txtLname = dialogView.findViewById(R.id.txtLname);
//        final EditText txtMI = dialogView.findViewById(R.id.txtMI);
//        final EditText txtBday = dialogView.findViewById(R.id.txtBirthday);
//        final RadioButton rbtnMale = dialogView.findViewById(R.id.rbtnMale);
//        final RadioButton rbtnFemale = dialogView.findViewById(R.id.rbtnFemale);
//        final EditText txtStreet = dialogView.findViewById(R.id.txtStreet);
//        final EditText txtCity = dialogView.findViewById(R.id.txtCity);
//        final EditText txtProvince = dialogView.findViewById(R.id.txtProv);
//        final Button btnUpdate = dialogView.findViewById(R.id.btnUpdate);
//
//
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//        String name = user.getDisplayName();
//        dialogBuilder.setTitle("Updating User" + name);

    }

    private void dailyQuotes(String quote){
//        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
//        alertBuilder.setMessage(quote)
//                .setCancelable(false)
//                .setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alert = alertBuilder.create();
//        alert.setTitle("Daily Motivational Quotes");
//        alert.show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(HomeActivity.this).inflate(R.layout.daily_quotes_layout,null);
        TextView quoteCon = v.findViewById(R.id.dailyQuotes);

        quoteCon.setText(quote);

        if(quote.length()>90)
            quoteCon.setTextSize(15);

        builder.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setView(v);
        builder.show();

        showed=1;
    }

    private void testQuote(){
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        int testTime = calendar.get(Calendar.HOUR_OF_DAY);
        //Toast.makeText(this,testTime+"",Toast.LENGTH_LONG).show();
        String[] arrayOfStrings = this.getResources().getStringArray(R.array.dailyQuotes);
        String rndQuotes = arrayOfStrings[new Random().nextInt(arrayOfStrings.length)];
        if(showed==0){
            dailyQuotes(rndQuotes);
            return;
        }
        if(testTime==0){
            showed=0;
            return;
        }
    }


}
