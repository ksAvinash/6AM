package com.sixamdev.jci.a6am;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

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

    private static final int REQ_CODE = 9001;
    private final String signInPreferences = "sixAmSignIn";
    public SharedPreferences sharedPreferences;
    private SignInButton SignIn;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.google_signin_layout);

        sharedPreferences = this.getSharedPreferences(signInPreferences, MODE_WORLD_READABLE);
        boolean isUserSignedUp = sharedPreferences.getBoolean("signedup", false);

        Log.i("isUserSignedUp : ", String.valueOf(isUserSignedUp));

        if (isUserSignedUp) {
            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            startActivity(mainActivityIntent);
        }

        SignIn = (SignInButton) findViewById(R.id.googleSignInButton);
        SignIn.setOnClickListener(this);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.googleSignInButton:
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, REQ_CODE);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void handleResult(GoogleSignInResult signInResult) {
        if (signInResult.isSuccess()) {
            GoogleSignInAccount googleSignInAccount = signInResult.getSignInAccount();
            Uri userPhoto = googleSignInAccount.getPhotoUrl();
            String name = googleSignInAccount.getDisplayName();
            String email = googleSignInAccount.getEmail();

            Intent postGSignInIntent = new Intent(this, AppSignUp.class);
            postGSignInIntent.putExtra("userName", name);
            postGSignInIntent.putExtra("userEmail", email);
            postGSignInIntent.putExtra("userProfileImage", userPhoto.toString());
            startActivity(postGSignInIntent);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(googleSignInResult);
        }
    }
}
