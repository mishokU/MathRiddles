package com.example.mathriddles;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    @NotNull
    @BindView(R.id.email_field) public EditText mEmail;
    @BindView(R.id.user_field) public EditText mUser;
    @BindView(R.id.full_score) public TextView mFullScore;
    @BindView(R.id.toolbar) public Toolbar toolbar;

    //FireBase Utils
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser mFUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");

        getInformationAboutUser();
    }

    private void getInformationAboutUser() {
        mAuth = FirebaseAuth.getInstance();

        mFUser = FirebaseAuth.getInstance().getCurrentUser();
        String user_id = mFUser.getUid();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String email = dataSnapshot.child("email").getValue().toString();
                String user = dataSnapshot.child("user").getValue().toString();
                String score = dataSnapshot.child("score").getValue().toString();

                mEmail.setText(email);
                mUser.setText(user);
                mFullScore.setText("Full score is " + score);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate((R.menu.profile_menu),menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                openSettings();
                return true;
            case R.id.log_out:
                exitAccount();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openSettings() {

    }

    private void exitAccount() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this,SignInActivity.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0,0);
    }
}
