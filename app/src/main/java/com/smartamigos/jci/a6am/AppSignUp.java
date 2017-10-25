package com.smartamigos.jci.a6am;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class AppSignUp extends AppCompatActivity {
    private static GoogleSignInResult googleSignInResult;
    private EditText userNameInput;
    private EditText userAgeInput;
    private EditText userLocationInput;
    private EditText userPhoneInput;
    private EditText userGenderInput;
    private ImageView googleProfileImage;
    private Button signUpButton;
    private boolean isFilled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle gSignInData = getIntent().getExtras();
        String googleName = gSignInData.getString("userName");
        final String googleEmail = gSignInData.getString("userEmail");

        setContentView(R.layout.activity_app_sign_up);

        userNameInput = (EditText) findViewById(R.id.userNameInput);
        userAgeInput = (EditText) findViewById(R.id.userAgeInput);
        userLocationInput = (EditText) findViewById(R.id.userLocationInput);
        userPhoneInput = (EditText) findViewById(R.id.userPhoneInput);
        userGenderInput = (EditText) findViewById(R.id.userGenderInput);
        googleProfileImage = (ImageView) findViewById(R.id.googleProfileImage);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        Log.i("urlReceived : ", gSignInData.getString("userProfileImage"));

        userNameInput.setText(googleName);
        if (gSignInData.getString("") != null) {
            Uri googlePhoto = Uri.parse(gSignInData.getString("userProfileImage"));
            Glide.with(this).load(googlePhoto).into(googleProfileImage);
        }

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(googleEmail)) {
                    if (TextUtils.isEmpty(userNameInput.getText())) {
                        userNameInput.setError("UserName required!");
                        isFilled = false;
                    }
                    if (TextUtils.isEmpty(userAgeInput.getText())) {
                        userAgeInput.setError("Age required!");
                        isFilled = false;
                    }
                    if (TextUtils.isEmpty(userLocationInput.getText())) {
                        userLocationInput.setError("Location required!");
                        isFilled = false;
                    }
                    if (TextUtils.isEmpty(userPhoneInput.getText())) {
                        userPhoneInput.setError("Phone no required!");
                        isFilled = false;
                    }
                    if (TextUtils.isEmpty(userGenderInput.getText())) {
                        userGenderInput.setError("Gender required!");
                        isFilled = false;
                    }
                    if (isFilled) {
                        Log.i("","Inside calling post");
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
                                    jsonParam.put("name",userNameInput.getText());
                                    jsonParam.put("gender",userGenderInput.getText());
                                    jsonParam.put("age",userAgeInput.getText());
                                    jsonParam.put("phoneno",userPhoneInput.getText());
                                    jsonParam.put("place",userLocationInput.getText());
                                    jsonParam.put("avatarid",gSignInData.getString("userProfileImage"));

                                    Log.i("JSON", jsonParam.toString());
                                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                                    os.writeBytes(jsonParam.toString());

                                    os.flush();

                                    BufferedReader serverAnswer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                    String line;
                                    while ((line = serverAnswer.readLine()) != null) {

                                        Log.i("LINE: ", line); //<--If any response from server
                                        //use it as you need, if server send something back you will get it here.
                                    }

                                    os.close();

                                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                                    Log.i("MSG", conn.getResponseMessage());

                                    conn.disconnect();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        thread.start();
                    }
                }
            }
        });
    }
}
