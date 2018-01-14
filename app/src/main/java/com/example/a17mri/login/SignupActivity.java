package com.example.a17mri.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText fullName,emailId,mobileNo,createPassword,confirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        fullName=findViewById(R.id.fullName);
        emailId=findViewById(R.id.userEmailId);
        mobileNo=findViewById(R.id.mobileNo);
        createPassword=findViewById(R.id.createPassword);
        confirmPassword=findViewById(R.id.confirmPassword);
    }

    public void signup(View v) {
        createAccount(emailId.getText().toString(),createPassword.getText().toString());
    }

    private void createAccount(String email,String password){
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user=mAuth.getCurrentUser();
                            addDetails(user);
                        }
                        else {
                            System.out.println(task.getException());
                        }
                    }
                });
    }

    private void addDetails(FirebaseUser user){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().
                setDisplayName(fullName.getText().toString()).build();
        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


}
