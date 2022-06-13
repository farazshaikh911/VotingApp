package com.example.voting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.voting.Authentication.AuthenticateActivity;
import com.example.voting.Authentication.SignUpActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements onClick {
    private DatabaseReference mDatabase;
    FirebaseUser fuser;
    boolean isVoted = false,isAdmin = false;
    Button btVote;
    RecyclerView rv_members;
    MemberAdapter memberAdapter;
    ArrayList<Members> membersArrayList = new ArrayList<>();
    ArrayList<User> users = new ArrayList<>();

    ProgressDialog progressDialog;
    String selectedId;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        toolbar.setTitle("Voting poll");
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        mDatabase = FirebaseDatabase.getInstance().getReference("Member");

        rv_members = findViewById(R.id.rv_member);


        btVote = findViewById(R.id.bt_Login);
        getUserDetail();

        btVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isVoted){
                    Toast.makeText(MainActivity.this, "Already Voted", Toast.LENGTH_SHORT).show();
                    return;
                }
                isVoted = true;
                setVote();
                writeNewUser(fuser.getUid(),fuser.getEmail(),isVoted);

            }
        });

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        assert fuser != null;
        if(Objects.equals(fuser.getEmail(), "admin@gmail.com")){
             isAdmin = true;

             startActivity(new Intent(MainActivity.this,AdminActivity.class));
         }
        memberAdapter = new MemberAdapter(this, membersArrayList,isAdmin,this);
        setAdapter();
        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, AuthenticateActivity.class));
                //addfav (heart icon) was clicked, Insert your after click code here.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setVote() {
   count += 1;
        mDatabase.child(selectedId).child("count").setValue(count);
    }

    public void getData() {
        progressDialog.show();
        mDatabase = FirebaseDatabase.getInstance().getReference("Member");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                membersArrayList.clear();
                if (dataSnapshot.getValue() == null) {
                    progressDialog.dismiss();
                }
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Members members = dataSnapshot1.getValue(Members.class);
                    membersArrayList.add(members);
                    progressDialog.dismiss();
                }
                memberAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    private void setAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rv_members.setLayoutManager(layoutManager);
        rv_members.setAdapter(memberAdapter);
    }


    public void writeNewUser(String userId,String email,boolean isVoted) {
        User user = new User(email,isVoted);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("users").child(userId).setValue(user);
        mDatabase.child("users").child(userId).child("isVoted").setValue(isVoted);
    }

    public void getUserDetail(){
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    progressDialog.dismiss();
                }
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    User user = dataSnapshot1.getValue(User.class);
                    users.add(user);
                    checkIfVoted();

                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    public void checkIfVoted(){
        Log.e("WELCOME","VALUE"+users.get(0).getEmail());

        for(User user: users){
            Log.e("WELCOME","VALUE"+user.getEmail());

            if(user.getEmail().equals(fuser.getEmail())){
                Log.e("WELCOME","VALUE"+user.getEmail());
                Log.e("WELCOME","VALUE"+fuser.getEmail());
                Log.e("WELCOME","VALUE"+user.isVoted());

                if(user.isVoted()){
                   isVoted = true;
               }
               else{
                   isVoted = false;
               }
            }
        }
    }

    @Override
    public void onClick(Members member) {

        selectedId = member.getId();
        count = member.getCount();

    }
}