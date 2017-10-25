package com.smartamigos.jci.a6am;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by charank on 28-09-2017.
 */

public class GoogleSignInActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private SignInButton SignIn;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    private TextView EmailTextField, NameTextField;
    private ImageView UserImageField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_signin_layout);
        SignIn = (SignInButton) findViewById(R.id.googleSignInButton);
        SignIn.setOnClickListener(this);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions).build();
//        EmailTextField = (TextView) findViewById(R.id.EmailTextField);
//        NameTextField = (TextView) findViewById(R.id.NameTextField);
//        UserImageField = (ImageView) findViewById(R.id.UserImageField);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.googleSignInButton :
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,REQ_CODE);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void handleResult(GoogleSignInResult signInResult){
        if(signInResult.isSuccess()){
            GoogleSignInAccount googleSignInAccount = signInResult.getSignInAccount();
            Uri userPhoto = googleSignInAccount.getPhotoUrl();
            String name = googleSignInAccount.getDisplayName();
            String email = googleSignInAccount.getEmail();
            Log.i("USername :: ",name);
            Log.i("EmailID :: ",email);
            Log.i("ImageUri ::",userPhoto.toString());

            Intent postGSignInIntent = new Intent(this,AppSignUp.class);
            postGSignInIntent.putExtra("userName",name);
            postGSignInIntent.putExtra("userEmail",email);
            postGSignInIntent.putExtra("userProfileImage",userPhoto.toString());
            startActivity(postGSignInIntent);
//            NameTextField.setText(name);
//            EmailTextField.setText(email);

            try {
//                Glide.with(this).load(userPhoto).into(UserImageField);   //setting image using Glide
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(),"UserNAme :: "+name+"\nEmailID :: "+email,Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQ_CODE){
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(googleSignInResult);
        }
    }
}
