package com.AbdAllahAbdElFattah13.linkedin.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.AbdAllahAbdElFattah13.linkedinsdk.ui.LinkedInUser;
import com.AbdAllahAbdElFattah13.linkedinsdk.ui.linkedin_builder.LinkedInFromActivityBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public static final int LINKEDIN_REQUEST = 99;
    public static String clientID;
    public static String clientSecret;
    public static String redirectUrl;

    private ImageView ivUserPic;
    private  Button btnLogin;
    private TextView tvFName, tvLName, tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getCredentials();

        ivUserPic = findViewById(R.id.iv_user_pic);
        btnLogin = findViewById(R.id.btn_login);
        tvFName = findViewById(R.id.tv_first_name);
        tvLName = findViewById(R.id.tv_last_name);
        tvEmail = findViewById(R.id.tv_email);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinkedInFromActivityBuilder.getInstance(MainActivity.this)
                        .setClientID(clientID)
                        .setClientSecret(clientSecret)
                        .setRedirectURI(redirectUrl)
                        .authenticate(LINKEDIN_REQUEST);
            }
        });
    }

    /**
     * Sets data to UI
     */
    private void setUserData(LinkedInUser user) {

        Log.wtf("LINKEDIN ID", user.getId());

        tvFName.setText(user.getFirstName());
        tvLName.setText(user.getLastName());
        tvEmail.setText(user.getEmail());

        if (user.getProfilePictureUrl() != null && !user.getProfilePictureUrl().isEmpty()) {
            new ImageLoadTask(user.getProfilePictureUrl(), ivUserPic).execute();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LINKEDIN_REQUEST && data != null) {
            if (resultCode == RESULT_OK) {

                //Successfully signed in and retrieved data
                LinkedInUser user = data.getParcelableExtra("social_login");
                setUserData(user);

            } else {
                //print the error
                Log.wtf("LINKEDIN ERR", data.getStringExtra("err_message"));

                if (data.getIntExtra("err_code", 0) == LinkedInFromActivityBuilder.ERROR_USER_DENIED) {
                    //user denied access to account
                    Toast.makeText(this, "User Denied Access", Toast.LENGTH_SHORT).show();
                } else if (data.getIntExtra("err_code", 0) == LinkedInFromActivityBuilder.ERROR_USER_DENIED) {
                    //some error occured
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    /**
     * Loads Image from url in image view, you might want to use a library like picasso or glide
     */
    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }
    }


    /**
     *
     * This Method just for demonstration purpose only, you are free to use any technique to keep your credentials secure
     * If you want to use this method, just rename the linkedin-credentials-example.json file to linkedin-credentials.json
     * Make sure to update your linkedin credentials in the said file
     */
    private void getCredentials() {
        try {

            InputStream is = getAssets().open("linkedin-credentials.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            JSONObject linkedinCred = new JSONObject(json);
            clientID = linkedinCred.getString("client_id");
            clientSecret = linkedinCred.getString("client_secret");
            redirectUrl = linkedinCred.getString("redirect_url");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
