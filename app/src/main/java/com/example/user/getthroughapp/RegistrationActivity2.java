package com.example.user.getthroughapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity2 extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE_REQUEST = 234;
    private Button btnLogout, btnChoose, btnUpload;
    private TextView useremail, btnSave;
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
    String isUserDetailed;
    private int year_x,month_x,day_x;
    private String[] monthStr = {"January","February","March","April","May","June","July","August","September","October","November","December"};
    private static final int DIALOG_ID = 0;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);





        firebaseAuth = FirebaseAuth.getInstance();

        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        showDialogOnClick();

        progressDialog = new ProgressDialog(this);

        storageReference = FirebaseStorage.getInstance().getReference();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        btnChoose = findViewById(R.id.btnChoose);
        imgView = findViewById(R.id.imgView);


        txtLname = findViewById(R.id.txtLname);
        txtFname = findViewById(R.id.txtFname);
        txtMI = findViewById(R.id.txtMI);
        txtStreet = findViewById(R.id.txtStreet);
        txtCity = findViewById(R.id.txtCity);
        txtProv = findViewById(R.id.txtProv);

        btnSave = findViewById(R.id.btnSave);

        txtBirthday = findViewById(R.id.txtBirthday);

        rbtnMale = findViewById(R.id.rbtnMale);
        rbtnFemale = findViewById(R.id.rbtnFemale);

        rbtnMale.setChecked(true);
        gender="Male";

        btnSave.setOnClickListener(this);
        btnChoose.setOnClickListener(this);



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
//        txtBirthday.getEditText().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new DatePickerDialog(RegistrationActivity2.this, date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//            }
//        });


    }


    private void saveUserInfo(){
        final String fname = txtFname.getEditText().getText().toString().trim();
        final String lname = txtLname.getEditText().getText().toString().trim();
        final String mname = txtMI.getEditText().getText().toString().trim();
        final String street = txtStreet.getEditText().getText().toString().trim();
        final String city = txtCity.getEditText().getText().toString().trim();
        final String province = txtProv.getEditText().getText().toString().trim();
        final String birthday = txtBirthday.getEditText().getText().toString().trim();

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
            txtMI.setError("Please enter middle initial");
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
        if (TextUtils.isEmpty(province)) {
            //Toast.makeText(UpdateActivity.this,"Please enter province",Toast.LENGTH_SHORT).show();
            txtProv.setError("Please enter province address");
            txtProv.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(birthday)) {
            //Toast.makeText(UpdateActivity.this,"Please enter birthdate",Toast.LENGTH_SHORT).show();
            txtBirthday.setError("Please enter birthday");
            txtBirthday.requestFocus();
            return;
        }
        if(filepath==null){
            //imgProfilePic.setImageURI();
            Toast.makeText(RegistrationActivity2.this,"Please enter profile image",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setTitle("Uploading Information...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        StorageReference riversRef = storageReference.child("images/"+System.currentTimeMillis()+".jpg");

        riversRef.putFile(filepath)
        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                profileImgUrl = taskSnapshot.getDownloadUrl().toString();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                        .setDisplayName(lname+", "+fname+" "+mname+".")
                        .setPhotoUri(Uri.parse(profileImgUrl))
                        .build();
                currentUser.updateProfile(profileChangeRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        User user = new User(fname,lname,mname,birthday,gender,street,city,province);
                        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                        databaseReference.child(currentUser.getUid()).setValue(user);
                        Toast.makeText(RegistrationActivity2.this,"Information Saved",Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(RegistrationActivity2.this,QuestionActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity2.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressDialog.dismiss();
                Toast.makeText(RegistrationActivity2.this,exception.getMessage(),Toast.LENGTH_LONG).show();
            }
        })
        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setMessage(((int) progress) + "% Uploaded");
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
                    gender="Male";
                    break;
            case R.id.rbtnFemale:
                if (checked)
                    gender="Female";
                    break;
        }
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

        //declaring database
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //mPreferences = getSharedPreferences("fileUri", Context.MODE_PRIVATE);
        //to store
        mEditor = mPreferences.edit();

        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data != null && data.getData() != null){
            filepath = data.getData();
            //saving to sharedprefs
            mEditor.putString("keyUri", String.valueOf(filepath));
            mEditor.commit();
            //load the keyUri stored data
            //String filepathUri = mPreferences.getString("keyUri", "default");
            Toast.makeText(this, ""+ filepath,Toast.LENGTH_SHORT).show();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                imgView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, ""+ e, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v == btnLogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

        if(v == btnSave){
            saveUserInfo();
        }

        if(v == btnChoose){
            //img chooser
            showFileChooser();
        }
    }


}
