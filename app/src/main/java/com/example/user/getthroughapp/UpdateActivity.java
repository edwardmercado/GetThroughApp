package com.example.user.getthroughapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 234;
    private Button btnLogout, btnBack, btnChoose, btnUpload;
    private TextView useremail, btnUpdate;
    private FirebaseAuth firebaseAuth;
    private TextInputLayout txtusern, txtpassw, txtFname, txtLname, txtMI, txtemail, txtScrtAns, txtStreet, txtCity, txtProv, txtBirthday;
    private Spinner spnScrtQues;
    private RadioButton rbtnMale, rbtnFemale;
    private ProgressDialog progressDialog;
    private String gender;
    private ImageView imgView2;
    private CircleImageView imgView;
    private Uri filepath;
    private StorageReference storageReference;
    String profileImgUrl;
    private CircleImageView imgProfilePic;
    private String id;
    String selectedGen;
    private int year_x,month_x,day_x;
    private String[] monthStr = {"January","February","March","April","May","June","July","August","September","October","November","December"};
    private static final int DIALOG_ID = 0;
    private String sharedUri;
    private Uri uriHolder;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        showDialogOnClick();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        btnChoose = findViewById(R.id.btnChoose);
        imgProfilePic = findViewById(R.id.imgView);



        txtLname = findViewById(R.id.txtLname);
        txtFname = findViewById(R.id.txtFname);
        txtMI = findViewById(R.id.txtMI);
        txtStreet = findViewById(R.id.txtStreet);
        txtCity = findViewById(R.id.txtCity);
        txtProv = findViewById(R.id.txtProv);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnChoose = findViewById(R.id.btnChoose);

        txtBirthday = findViewById(R.id.txtBirthday);

        rbtnMale = findViewById(R.id.rbtnMale);
        rbtnFemale = findViewById(R.id.rbtnFemale);

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mEditor = mPreferences.edit();

        String sharedUri = mPreferences.getString("keyUri","");
        uriHolder = Uri.parse(sharedUri);
        Toast.makeText(this,sharedUri,Toast.LENGTH_SHORT).show();

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });


//        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, dayOfMonth);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                String myFormat = "MMMM/dd/yyyy"; //In which you need put here
//                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//                txtBirthday.getEditText().setText(sdf.format(myCalendar.getTime()));
//            }
//        };
//
//        txtBirthday.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new DatePickerDialog(UpdateActivity.this, date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//            }
//        });


        if(rbtnFemale.isSelected()){
            selectedGen = "Female";
        }
        if(rbtnMale.isSelected()){
            selectedGen = "Male";
        }

        loadUserInfo();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String lname = txtLname.getEditText().getText().toString().trim();
                final String fname = txtFname.getEditText().getText().toString().trim();
                final String mname = txtMI.getEditText().getText().toString().trim();
                final String bday = txtBirthday.getEditText().getText().toString().trim();
                final String street = txtStreet.getEditText().getText().toString().trim();
                final String city = txtCity.getEditText().getText().toString().trim();
                final String prov = txtProv.getEditText().getText().toString().trim();

                if (TextUtils.isEmpty(lname)) {
                    txtLname.setError("Please enter first name");
                    txtLname.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(fname)) {
                    //Toast.makeText(UpdateActivity.this,"Please enter first name",Toast.LENGTH_SHORT).show();
                    txtFname.setError("Please enter last name");
                    txtFname.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(mname)) {
                    //Toast.makeText(UpdateActivity.this,"Please enter middle initial",Toast.LENGTH_SHORT).show();
                    txtMI.setError("Please enter middle name");
                    txtMI.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(street)) {
                    //Toast.makeText(UpdateActivity.this,"Please enter street",Toast.LENGTH_SHORT).show();
                    txtStreet.setError("Please enter street address");
                    txtStreet.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(city)) {
                    //Toast.makeText(UpdateActivity.this,"Please enter city",Toast.LENGTH_SHORT).show();
                    txtStreet.setError("Please enter city address");
                    txtCity.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(prov)) {
                    //Toast.makeText(UpdateActivity.this,"Please enter province",Toast.LENGTH_SHORT).show();
                    txtProv.setError("Please enter province address");
                    txtProv.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(bday)) {
                    //Toast.makeText(UpdateActivity.this,"Please enter birthdate",Toast.LENGTH_SHORT).show();
                    txtBirthday.setError("Please enter birthday");
                    txtBirthday.requestFocus();
                    return;
                }
                if(filepath==null){
                    //imgProfilePic.setImageURI();
                    Toast.makeText(UpdateActivity.this,"Please enter profile image",Toast.LENGTH_SHORT).show();
                    return;
                }


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                progressDialog.setTitle("Updating Information...");
                progressDialog.show();
                progressDialog.setCancelable(false);
                StorageReference riversRef = storageReference.child("images/" + System.currentTimeMillis() + ".jpg");

                riversRef.putFile(filepath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                profileImgUrl = taskSnapshot.getDownloadUrl().toString();

                                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(lname + ", " + fname + " " + mname + ".")
                                        .setPhotoUri(Uri.parse(profileImgUrl))
                                        .build();
                                currentUser.updateProfile(profileChangeRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        updateUser(lname, fname, mname, bday, selectedGen, street, city, prov);
                                        progressDialog.dismiss();
                                        finish();
                                        startActivity(new Intent(UpdateActivity.this, HomeActivity.class));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(UpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                progressDialog.dismiss();
                                Toast.makeText(UpdateActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                progressDialog.setMessage(((int) progress) + "% Updated");
                            }
                        });
            }
        });

    }

    public void showDialogOnClick(){
        txtBirthday = findViewById(R.id.txtBirthday);
        txtBirthday.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if(id == DIALOG_ID){
            return new DatePickerDialog(this, dpickerListener, year_x,month_x,day_x);
        }else{
            return null;
        }
    }

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x = year;
            month_x = month;
            day_x = dayOfMonth;
            txtBirthday.getEditText().setText(monthStr[month_x] + " " + day_x + ", " + year_x);
        }
    };

    public void genderClick(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rbtnMale:
                if (checked)
                    selectedGen="Male";
                break;
            case R.id.rbtnFemale:
                if (checked)
                    selectedGen="Female";
                break;
        }
    }

    private void loadUserInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        System.out.println(user);
        Glide.with(this)
                .load(user.getPhotoUrl().toString())
                .into(imgProfilePic);
        String name = user.getDisplayName();


        firebaseAuth = FirebaseAuth.getInstance();
        id = firebaseAuth.getCurrentUser().getUid();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference refProf = database.getReference(id).child("profielImgUrl");

        refProf.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                String updateUser = dataSnapshot.getValue(String.class);
                profileImgUrl = updateUser;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference refFname = database.getReference(id).child("fname");

        refFname.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                String updateUser = dataSnapshot.getValue(String.class);
                txtFname.getEditText().setText(updateUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference refLname = database.getReference(id).child("lname");

        refLname.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                String updateUser = dataSnapshot.getValue(String.class);
                txtLname.getEditText().setText(updateUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference refMname = database.getReference(id).child("mname");

        refMname.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                String updateUser = dataSnapshot.getValue(String.class);
                txtMI.getEditText().setText(updateUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference refBday = database.getReference(id).child("birthday");

        refBday.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                String updateUser = dataSnapshot.getValue(String.class);
                txtBirthday.getEditText().setText(updateUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference refGender = database.getReference(id).child("gender");

        refGender.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                String updateUser = dataSnapshot.getValue(String.class);
                if(updateUser.equals("Male")){
                    selectedGen = "Male";
                    rbtnMale.setChecked(true);
                }else{
                    rbtnFemale.setChecked(true);
                    selectedGen = "Female";
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference refStreet = database.getReference(id).child("street");

        refStreet.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                String updateUser = dataSnapshot.getValue(String.class);
                txtStreet.getEditText().setText(updateUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference refCity = database.getReference(id).child("city");

        refCity.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                String updateUser = dataSnapshot.getValue(String.class);
                txtCity.getEditText().setText(updateUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference refProv = database.getReference(id).child("province");

        refProv.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                String updateUser = dataSnapshot.getValue(String.class);
                txtProv.getEditText().setText(updateUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private void updateUser(String lname, String fname, String mname, String bday, String gender, String street, String city, String prov){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(id);
        User user = new User(lname, fname, mname, bday, gender, street, city, prov);
        databaseReference.setValue(user);
        Toast.makeText(this,"User update successfull",Toast.LENGTH_SHORT).show();
        //startActivity(new Intent(this, HomeActivity.class));
    }


    private void showFileChooser(){
        Intent i = new Intent();
        //you can only select image
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select an Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data != null && data.getData() != null){
            filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                imgProfilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, ""+ e, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
