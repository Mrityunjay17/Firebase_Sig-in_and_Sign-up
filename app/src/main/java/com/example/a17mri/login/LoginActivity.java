package com.example.a17mri.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by 17mri on 10-01-2018.
 */

public class LoginActivity extends AppCompatActivity {
    private final String TAG="Firebase";
    private FirebaseAuth mAuth;
    EditText email,password;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.userEmail);
        password=findViewById(R.id.userPassword);
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null){
            Log.v(TAG, "current user is null");
        }
        else {
           onSuccess();
        }
    }


    private  void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if(task.isSuccessful()){
                  onSuccess();
              }
              else {
                  Toast.makeText(getApplicationContext(),"SignIn error",Toast.LENGTH_LONG).show();
                  Log.v(TAG,"signInWithEmail:failure", task.getException());
              }
            }
        });
    }

    public void createAccount(View v) {
        Intent create_Account=new Intent(getApplicationContext(),SignupActivity.class);
        startActivity(create_Account);
    }

    public void login(View v) {
        signIn(email.getText().toString(),password.getText().toString());
    }

    public void forgetPassword(View v) {
       Intent intent=new Intent(getApplicationContext(),ForgetPasswordActivity.class);
       startActivity(intent);
    }


    public void onSuccess(){
        Intent create_Account=new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(create_Account);
        finish();
    }
}
