package com.AbdAllahAbdElFattah13.linkedinsdk.linkedin_builder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public final class LinkedInFromActivityBuilder extends LinkedInBuilder {

  private Activity activity;

  private LinkedInFromActivityBuilder(Activity activity) {
    super(activity);
    this.activity = activity;
  }

  public static LinkedInFromActivityBuilder getInstance(Activity activity) {
    return new LinkedInFromActivityBuilder(activity);
  }

  @Override
  LinkedInBuilder self() {
    return this;
  }

  @Override
  void startActivityForResult(Intent intent, int requestCode) {
    activity.startActivityForResult(intent, requestCode);
  }
}
