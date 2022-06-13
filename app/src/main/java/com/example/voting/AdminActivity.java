package com.example.voting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.voting.Authentication.AuthenticateActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Objects;

public class AdminActivity extends AppCompatActivity implements onClick {
Button bt_add;
EditText et_addMember;
    FirebaseUser user;

    private DatabaseReference mDatabase;
    private String memberId;
    Members members;
    boolean isAdmin = false;
    RecyclerView rv_members;
    MemberAdapter memberAdapter;
    ArrayList<Members> membersArrayList = new ArrayList<>();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
      toolbar.setTitle("Admin");
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        bt_add = findViewById(R.id.bt_add);
        et_addMember = findViewById(R.id.et_addMember);
        rv_members = findViewById(R.id.rv_members);

        mDatabase = FirebaseDatabase.getInstance().getReference("Member");
        user = FirebaseAuth.getInstance().getCurrentUser();

        getData();
            isAdmin = true;

        memberAdapter = new MemberAdapter(this, membersArrayList,isAdmin,this);
        setAdapter();
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddMember();
            }
        });
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
                startActivity(new Intent(AdminActivity.this, AuthenticateActivity.class));
                //addfav (heart icon) was clicked, Insert your after click code here.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rv_members.setLayoutManager(layoutManager);
        rv_members.setAdapter(memberAdapter);
    }

    private void AddMember() {
        if(et_addMember.getText().toString().isEmpty()){
            Toast.makeText(this, "Member name can't be empty", Toast.LENGTH_SHORT).show();
       return;
        }
        memberId = mDatabase.push().getKey();
        members = new Members(memberId,et_addMember.getText().toString(),0);
        mDatabase.child(memberId).setValue(members);
    }

    //Method created
    public void getData() {
        progressDialog.show();
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

    @Override
    public void onClick(Members member) {

    }
}