package com.AbdAllahAbdElFattah13.linkedinsdk.linkedin_builder;

import android.content.Intent;

import androidx.fragment.app.Fragment;

public final class LinkedInFromFragmentBuilder extends LinkedInBuilder {

    private Fragment fragment;

    private LinkedInFromFragmentBuilder(Fragment fragment) {
        super(fragment.getActivity());
        this.fragment = fragment;
    }

    public static LinkedInFromFragmentBuilder getInstance(Fragment fragment) {
        return new LinkedInFromFragmentBuilder(fragment);
    }

    @Override
    LinkedInBuilder self() {
        return this;
    }

    @Override
    void startActivityForResult(Intent intent, int requestCode) {
        fragment.startActivityForResult(intent, requestCode);
    }
}
