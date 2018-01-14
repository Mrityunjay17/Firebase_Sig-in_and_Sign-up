package com.example.a17mri.login;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    EditText email;
    final String TAG="forgetPassword";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        firebaseAuth=FirebaseAuth.getInstance();
        email=findViewById(R.id.emailId);

    }

    public void restPassword(final View v){
        firebaseAuth.sendPasswordResetEmail(email.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Snackbar.make(v,"Check Your EmailId For Password",Snackbar.LENGTH_LONG).show();
                        }
                        else {
                            Log.e(TAG,task.getException().toString());
                        }
                    }
                });
    }
}
