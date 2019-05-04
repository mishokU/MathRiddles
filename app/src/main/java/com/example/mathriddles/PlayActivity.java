package com.example.mathriddles;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

class PlayActivity extends AppCompatActivity implements View.OnClickListener {

    //Firebase storage
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private int countOfRiddles = 0;

    private View riddle_place;
    private View keyBoard;
    private TextView riddle_text;
    private Toolbar toolbar;
    private List<Button> keybuttos = new ArrayList<>();
    private EditText input;
    private ImageButton enter;
    private ImageButton erase_from_input;
    private ImageButton helpHint;
    private String riddle_answer;
    private Random random;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_activity);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("riddles");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                countOfRiddles = (int)dataSnapshot.getChildrenCount();
                addRiddleToScreen(dataSnapshot.child(String.valueOf(countOfRiddles-1)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        findAllViews();
        setOnActions();
    }

    private void addRiddleToScreen(DataSnapshot child) {
        Iterator i = child.getChildren().iterator();
        if(i.hasNext()) {
            String riddle = ((DataSnapshot)i.next()).getKey();
            System.out.println("riddle: " + riddle);
            riddle_text.setText(riddle);
            riddle_text.setVisibility(View.VISIBLE);
            riddle_answer = child.child(riddle).getValue().toString();
            System.out.println("riddle_answer: " + riddle_answer);
        }
    }

    private void setOnActions() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        for(int i = 0; i <= 9; i++){
            String buttonID = "button_" + i;
            int resId = getResources().getIdentifier(buttonID,"id",getPackageName());
            Button button = findViewById(resId);
            button.setOnClickListener(this);
            keybuttos.add(button);
        }

        enter.setOnClickListener(this);
        erase_from_input.setOnClickListener(this);
    }

    private void findAllViews() {

        toolbar = findViewById(R.id.toolbar);
        keyBoard = findViewById(R.id.keyboard);
        riddle_place = findViewById(R.id.riddle_place);
        riddle_text = findViewById(R.id.riddle_text);
        enter = findViewById(R.id.enter);
        helpHint = findViewById(R.id.helpHint);
        erase_from_input = findViewById(R.id.erase_from_input);

        input = findViewById(R.id.input);
        input.setFocusable(false);
        input.setClickable(false);
    }

    @Override
    public void onClick(View v) {
        for(Button button : keybuttos){
            if(button == v) {
                CharSequence charSequence = input.getText() + ((Button)v).getText().toString();
                input.setText(charSequence);
            }
        }
        if(v == erase_from_input) {
            input.setText("");
        }

        if(v == enter) {
            if(input.getText().toString().equals(riddle_answer)){
                Toast.makeText(getApplicationContext(), "win!", Toast.LENGTH_LONG).show();

                //Score + 1
                //Refresh riddle

                int current_riddle = random.nextInt(countOfRiddles);
                //addRiddleToScreen(.child(String.valueOf(current_riddle)));
            }
        }
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
    }

}
