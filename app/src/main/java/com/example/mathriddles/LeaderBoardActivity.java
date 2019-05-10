package com.example.mathriddles;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LeaderBoardActivity extends AppCompatActivity {

    @BindView(R.id.scrollView) public ScrollView scrollView;
    @BindView(R.id.mainTape) public LinearLayout mainTape;
    @BindView(R.id.toolbar) public Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_board_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Leader Board");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getAllScores();
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

    private void getAllScores() {
        //Firebase data
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                collectInformation((Map<String,Object>) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void collectInformation(Map<String, Object> users) {

        //Iterate through each user, ignoring their UID
        for(Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();

            LeaderBoardItem leaderBoardItem = new LeaderBoardItem(this);
            leaderBoardItem.setItemIndex(String.valueOf(mainTape.getChildCount()));
            leaderBoardItem.setUserItemName(singleUser.get("user").toString());
            leaderBoardItem.setItemScore(singleUser.get("score").toString());

            mainTape.addView(leaderBoardItem.getItemView(),0);
        }
    }
}
