package com.example.user.getthroughapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserList extends AppCompatActivity {
    private RecyclerView userList;
    private DatabaseReference userDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        //declare the firebase database and get values on user
        userDatabase = FirebaseDatabase.getInstance().getReference().child("user");
        userDatabase.keepSynced(true);

        userList=(RecyclerView)findViewById(R.id.userRecyclerView);
        userList.setHasFixedSize(true);
        userList.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();
        //userViewHolder class used for initializing class from textview
        FirebaseRecyclerAdapter<User,UserViewHolder>firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<User, UserViewHolder>
                (User.class,R.layout.user_layout,UserViewHolder.class,userDatabase) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, User model, int position) {
                viewHolder.setName(model.getFname(),model.getLname(),model.getMname());

            }
        };
        userList.setAdapter(firebaseRecyclerAdapter);


    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public UserViewHolder(View userView){
            super(userView);
            mView=itemView;

        }
        public void setName(String fname, String lname, String mname){
            //TextView name = (TextView)mView.findViewById(R.id.txtName);
            //name.setText(fname+" "+mname+". "+lname);

        }
    }
}
