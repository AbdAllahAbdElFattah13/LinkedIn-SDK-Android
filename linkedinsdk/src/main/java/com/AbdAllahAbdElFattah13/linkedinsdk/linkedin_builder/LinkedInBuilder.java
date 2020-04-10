package com.AbdAllahAbdElFattah13.linkedinsdk.linkedin_builder;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.AbdAllahAbdElFattah13.linkedinsdk.LinkedInAuthenticationActivity;
import com.AbdAllahAbdElFattah13.linkedinsdk.RetrieveBasicProfileAsyncTask;
import com.AbdAllahAbdElFattah13.linkedinsdk.helpers.OnBasicProfileListener;

import java.util.Random;

/**
 * Created by AbdAllah AbdElfattah on 11/04/2020,
 * Cairo, Egypt.
 */
public abstract class LinkedInBuilder {

    private Intent intent;
    private String state;

    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_SECRET_KEY = "client_secret";
    public static final String REDIRECT_URI = "redirect_uri";
    public static final String STATE = "state";

    public static final String TAG = "LinkedInAuth";


    public static final int ERROR_USER_DENIED = 11;
    public static final int ERROR_FAILED = 12;

    abstract LinkedInBuilder self();

    abstract void startActivityForResult(Intent intent, int requestCode);


    LinkedInBuilder(Context context) {
        this.intent = new Intent(context, LinkedInAuthenticationActivity.class);
    }

    public LinkedInBuilder setClientID(String clientID) {
        updateIntent(CLIENT_ID, clientID);
        return self();
    }

    public LinkedInBuilder setClientSecret(String clientSecret) {
        updateIntent(CLIENT_SECRET_KEY, clientSecret);
        return self();
    }

    public LinkedInBuilder setRedirectURI(String redirectURI) {
        updateIntent(REDIRECT_URI, redirectURI);
        return self();
    }

    public LinkedInBuilder setState(String state) {
        this.state = state;
        updateIntent(STATE, state);
        return self();
    }

    public void authenticate(int requestCode) {
        if (validateAuthenticationParams()) {
            if (state == null) {
                generateState();
            }
            this.startActivityForResult(intent, requestCode);
        }
    }

    private void updateIntent(String key, String value) {
        intent.putExtra(key, value);
    }

    private boolean validateAuthenticationParams() {

        if (intent.getStringExtra(CLIENT_ID) == null) {
            Log.e(TAG, "Client ID is required", new IllegalArgumentException());
            return false;
        }

        if (intent.getStringExtra(CLIENT_SECRET_KEY) == null) {
            Log.e(TAG, "Client Secret is required", new IllegalArgumentException());
            return false;
        }

        if (intent.getStringExtra(REDIRECT_URI) == null) {
            Log.e(TAG, "Redirect URI is required", new IllegalArgumentException());
            return false;
        }

        return true;
    }

    private void generateState() {
        String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnmMNBVCXZLKJHGFDSAQWERTYUIOP";
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < 16; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        this.state = sb.toString();
        intent.putExtra(STATE, state);
    }

    public static void retrieveBasicProfile(@NonNull String accessToken, long accessTokenExpiry, @NonNull OnBasicProfileListener onBasicProfileListener) {
        new RetrieveBasicProfileAsyncTask(accessToken, accessTokenExpiry, onBasicProfileListener).execute();
    }

}
