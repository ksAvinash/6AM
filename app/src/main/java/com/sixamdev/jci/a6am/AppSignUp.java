package com.sixamdev.jci.a6am;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class AppSignUp extends AppCompatActivity implements View.OnClickListener {
    private static GoogleSignInResult googleSignInResult;
    private final String signInPreferences = "6am_users";
    public SharedPreferences sharedPreferences;
    private EditText userNameInput;
    private EditText userAgeInput;
    private EditText userLocationInput;
    private EditText userPhoneInput;
    private ImageView avatarImageView;
    private Button signUpButton;
    private boolean isFilled = true;
    private CircleImageView genderMale, genderFemale;
    private String googleEmail, googleName, avatarID, userAge, userLocation, userPhoneNumber, genderInput = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle gSignInData = getIntent().getExtras();
        googleName = gSignInData.getString("userName");
        googleEmail = gSignInData.getString("userEmail");
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_app_sign_up);

        userNameInput = (EditText) findViewById(R.id.userNameInput);
        userAgeInput = (EditText) findViewById(R.id.userAgeInput);
        userLocationInput = (EditText) findViewById(R.id.userLocationInput);
        userPhoneInput = (EditText) findViewById(R.id.userPhoneInput);
        avatarImageView = (ImageView) findViewById(R.id.avatarImageView);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        genderMale = (CircleImageView) findViewById(R.id.genderMale);
        genderFemale = (CircleImageView) findViewById(R.id.genderFemale);


        avatarID = gSignInData.getString("userProfileImage");

        userNameInput.setText(googleName);
        if (gSignInData.getString("") != null) {
            Uri googlePhoto = Uri.parse(gSignInData.getString("userProfileImage"));
            Glide.with(this).load(googlePhoto).into(avatarImageView);
        }

        genderMale.setOnClickListener(this);
        genderFemale.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.genderMale:
                genderMale.setBorderColor(Color.GREEN);
                genderFemale.setBorderColor(Color.TRANSPARENT);
                genderFemale.setAlpha(0);
                genderInput = "male";
                break;
            case R.id.genderFemale:
                genderFemale.setBorderColor(Color.GREEN);
                genderMale.setBorderColor(Color.TRANSPARENT);
                genderMale.setAlpha(0);
                genderInput = "female";
                break;
            case R.id.signUpButton:

                if (!TextUtils.isEmpty(googleEmail)) {
//                    if (TextUtils.isEmpty(userNameInput.getText())) {
//                        userNameInput.setError("UserName required!");
//                        isFilled = false;
//                    }
//                    if (TextUtils.isEmpty(userAgeInput.getText())) {
//                        userAgeInput.setError("Age required!");
//                        isFilled = false;
//                    }
//                    if (TextUtils.isEmpty(userLocationInput.getText())) {
//                        userLocationInput.setError("Location required!");
//                        isFilled = false;
//                    }
//                    if (TextUtils.isEmpty(userPhoneInput.getText())) {
//                        userPhoneInput.setError("Phone no required!");
//                        isFilled = false;
//                    }
//                    if (TextUtils.isEmpty(userGenderInput.getText())) {
//                        userGenderInput.setError("Gender required!");
//                        isFilled = false;
//                    }
                    if (isFilled) {
                        Log.i("", "Inside calling post");
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    URL url = new URL("https://zra7dwvb6b.execute-api.ap-south-1.amazonaws.com/prod/signup");
                                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                    conn.setRequestMethod("POST");
                                    conn.setRequestProperty("Content-Type", "application/json");
                                    conn.setRequestProperty("Accept", "application/json");
                                    conn.setDoOutput(true);
                                    conn.setDoInput(true);

                                    JSONObject jsonParam = new JSONObject();
                                    jsonParam.put("email", googleEmail);
                                    jsonParam.put("name", userNameInput.getText());
                                    jsonParam.put("age", userAgeInput.getText());
                                    jsonParam.put("gender", genderInput);
                                    jsonParam.put("phoneno", userPhoneInput.getText());
                                    jsonParam.put("place", userLocationInput.getText());
                                    jsonParam.put("avatarid", avatarID);

                                    userAge = userAgeInput.getText().toString().trim();
                                    userLocation = userLocationInput.getText().toString().trim();
                                    userPhoneNumber = userPhoneInput.getText().toString().trim();

                                    Log.i("JSON", jsonParam.toString());
                                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                                    os.writeBytes(jsonParam.toString());

                                    os.flush();

                                    BufferedReader serverAnswer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                    String result;

                                    result = serverAnswer.readLine();

                                    os.close();

                                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                                    Log.i("MSG", conn.getResponseMessage());


                                    conn.disconnect();

                                    JSONObject responseJSON = new JSONObject(result);
                                    String responseStatus = responseJSON.getString("signup");

                                    Log.i("responseStatus : ", responseStatus);

                                    if (responseStatus.equals("PASS")) {
                                        //set the signedin shared preference to true
                                        sharedPreferences = getSharedPreferences(signInPreferences, Context.MODE_PRIVATE);

                                        SharedPreferences.Editor spEditor = sharedPreferences.edit();

                                        spEditor.putBoolean("signedup", true);
                                        spEditor.putString("email", googleEmail);
                                        spEditor.putString("name", googleName);
                                        spEditor.putString("age", userAge);
                                        spEditor.putString("gender", genderInput);
                                        spEditor.putString("phoneno", userPhoneNumber);
                                        spEditor.putString("place", userLocation);
                                        spEditor.putString("avatarid", avatarID);
                                        spEditor.commit();

                                        Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(mainActivityIntent);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        thread.start();
                    }
                }
                break;
        }
    }
}
