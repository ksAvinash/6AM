package com.sixamdev.jci.a6am.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by avinashk on 05/11/17.
 */


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.d("FIREBASE", "onTokenRefresh: "+refreshedToken);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }


    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.



        try{
            URL url = new URL("https://zra7dwvb6b.execute-api.ap-south-1.amazonaws.com/prod/update-fb-instance-id");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);


            JSONObject jsonParam = new JSONObject();
            jsonParam.put("email", "hello@123.com");
            jsonParam.put("fb_instance_id",token);


            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonParam.toString());
            os.flush();
            BufferedReader serverAnswer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            Log.d("FIREBASE : ", "updateToken : "+serverAnswer.readLine()); //response from server


            os.close();
            conn.disconnect();

        }catch (Exception e){

        }



    }
}
