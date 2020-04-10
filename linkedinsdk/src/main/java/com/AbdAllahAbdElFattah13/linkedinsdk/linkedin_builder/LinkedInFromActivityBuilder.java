package com.AbdAllahAbdElFattah13.linkedinsdk.linkedin_builder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public final class LinkedInFromActivityBuilder extends LinkedInBuilder {

  private Activity activity;

  private LinkedInFromActivityBuilder(Activity activity) {
    this.activity = activity;
  }


  public static LinkedInFromActivityBuilder getInstance(Activity activity) {
    return new LinkedInFromActivityBuilder(activity);
  }

  public LinkedInFromActivityBuilder setClientID(String clientID) {
    updateIntent(CLIENT_ID, clientID);
    return this;
  }

  public LinkedInFromActivityBuilder setClientSecret(String clientSecret) {
    updateIntent(CLIENT_SECRET_KEY, clientSecret);
    return this;
  }

  public LinkedInFromActivityBuilder setRedirectURI(String redirectURI) {
    updateIntent(REDIRECT_URI, redirectURI);
    return this;
  }

  public LinkedInFromActivityBuilder setState(String state) {
    this.state = state;
    updateIntent(STATE, state);
    return this;
  }


  @Override
  Context getContext() {
    return this.activity;
  }

  @Override
  void startActivityForResult(Intent intent, int requestCode) {
    activity.startActivityForResult(intent, requestCode);
  }
}
