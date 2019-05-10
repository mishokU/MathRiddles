package com.example.mathriddles;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    @NotNull
    @BindView(R.id.register_button) public Button mRegisterButton;
    @BindView(R.id.to_sign_in) public Button mToSignIn;
    @BindView(R.id.email_field) public EditText mEmail;
    @BindView(R.id.password_field) public EditText mPassword;
    @BindView(R.id.user_field) public EditText mUser;

    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        mRegisterButton.setOnClickListener(this);
        mToSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == mRegisterButton){

            String email = mEmail.getText().toString();
            String password = mPassword.getText().toString();
            String user = mUser.getText().toString();

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(user)){
                Toast.makeText(this, "Required all fields!", Toast.LENGTH_SHORT).show();
            } else {
                register(email,password,user);
            }
        }
        if(v == mToSignIn){
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            overridePendingTransition(0,0);
        }
    }

    private void register(final String email, final String password, final String user) {
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String user_id = firebaseUser.getUid();

                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("user",user);
                            hashMap.put("email", email);
                            hashMap.put("score", "0");

                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Wrong data", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
