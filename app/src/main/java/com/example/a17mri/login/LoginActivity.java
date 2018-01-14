package com.example.a17mri.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Created by 17mri on 10-01-2018.
 */

public class LoginActivity extends AppCompatActivity {
    private final String TAG="Login";
    private FirebaseAuth mAuth;
    private EditText email,password;
    private static final int RC_SIGN_IN = 1996;
    private GoogleSignInClient googleSignInClient;
    private ProgressDialog progressDialog=null;
    private SignInButton googleSignInButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.userEmail);
        password=findViewById(R.id.userPassword);

        googleSignInButton=findViewById(R.id.googleSignIn);

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });
        //Google sin in configure
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient=GoogleSignIn.getClient(this,gso);

    }




    //on sing in button click function call
    public void login(View v) {
        signIn(email.getText().toString(),password.getText().toString());
    }

    //on create Account button click function call
    public void createAccount(View v) {
        Intent create_Account=new Intent(getApplicationContext(),SignupActivity.class);
        startActivity(create_Account);
    }

    //on create forgetpassword button click function call
    public void forgetPassword(View v) {
        Intent intent=new Intent(getApplicationContext(),ForgetPasswordActivity.class);
        startActivity(intent);
    }

    //on Google sin in button click function call
    private void googleSignIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    //On sing in click method
    private  void signIn(String email, String password){
        progressDialog=progressDialogShow(googleSignInButton.getRootView());
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    onSuccess();
                }
                else {
                    Toast.makeText(getApplicationContext(),"SignIn error",Toast.LENGTH_LONG).show();
                    Log.v(TAG,"signInWithEmail:failure", task.getException());
                    progressDialogDismiss(progressDialog);
                }
            }
        });
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



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    //firebase google sign in method pass
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        progressDialog=progressDialogShow(googleSignInButton.getRootView());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            onSuccess();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            progressDialogDismiss(progressDialog);
                            Snackbar.make(findViewById(R.id.main), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    // on Successful sing in change activity
    public void onSuccess(){
       progressDialogDismiss(progressDialog);
        Intent create_Account=new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(create_Account);
        finish();
    }

    private ProgressDialog progressDialogShow(View view){
        ProgressDialog dialog=new ProgressDialog(view.getContext(),R.style.Theme_MyDialog);
        dialog.setMessage("Sign in.......");
        dialog.show();
        return dialog;
    }

    private void progressDialogDismiss(ProgressDialog progressDialog){
        try {
            progressDialog.dismiss();
        }
        catch (NullPointerException e){

        }
    }

}
