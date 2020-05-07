package com.AbdAllahAbdElFattah13.linkedinsdk.ui;

import com.AbdAllahAbdElFattah13.linkedinsdk.ui.LinkedInUser;

public interface OnBasicProfileListener {
    void onDataRetrievalStart();

    void onDataSuccess(LinkedInUser linkedInUser);

    void onDataFailed(int errCode, String errMessage);

}
