package com.example.mathriddles;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    @NotNull
    @BindView(R.id.email_field) public EditText mEmail;
    @BindView(R.id.password_field) public EditText mPassword;
    @BindView(R.id.sign_in_button) public Button mSignInButton;
    @BindView(R.id.to_register) public Button mToRegister;
    //Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        mSignInButton.setOnClickListener(this);
        mToRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == mSignInButton){

            String email = mEmail.getText().toString();
            String password = mPassword.getText().toString();

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                Toast.makeText(this, "All fields reqired!", Toast.LENGTH_SHORT).show();
            } else {
                signIn(email,password);
            }
        }

        if(v == mToRegister){
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
            overridePendingTransition(0,0);
        }
    }

    private void signIn(final String email,final String password) {
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                    finish();
                    overridePendingTransition(0, 0);
                } else {
                    Toast.makeText(SignInActivity.this,"Wrong data",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
