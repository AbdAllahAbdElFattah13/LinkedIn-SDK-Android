package com.AbdAllahAbdElFattah13.linkedinsdk.linkedin_builder;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

public final class LinkedInFromFragmentBuilder extends LinkedInBuilder {

    private Fragment fragment;

    private LinkedInFromFragmentBuilder(Fragment fragment) {
        this.fragment = fragment;
    }

    public static LinkedInFromFragmentBuilder getInstance(Fragment fragment) {
        return new LinkedInFromFragmentBuilder(fragment);
    }

    public LinkedInFromFragmentBuilder setClientID(String clientID) {
        updateIntent(CLIENT_ID, clientID);
        return this;
    }

    public LinkedInFromFragmentBuilder setClientSecret(String clientSecret) {
        updateIntent(CLIENT_SECRET_KEY, clientSecret);
        return this;
    }

    public LinkedInFromFragmentBuilder setRedirectURI(String redirectURI) {
        updateIntent(REDIRECT_URI, redirectURI);
        return this;
    }

    public LinkedInFromFragmentBuilder setState(String state) {
        this.state = state;
        updateIntent(STATE, state);
        return this;
    }

    @Override
    Context getContext() {
        return this.fragment.getContext();
    }

    @Override
    void startActivityForResult(Intent intent, int requestCode) {
        fragment.startActivityForResult(intent, requestCode);
    }
}
