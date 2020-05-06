package com.AbdAllahAbdElFattah13.linkedinsdk.domain.usecases

import android.net.Uri
import com.AbdAllahAbdElFattah13.linkedinsdk.domain.abstracts.UseCase
import com.AbdAllahAbdElFattah13.linkedinsdk.domain.utils.LinkedInConst

class RetrieveAuthorizationTokenFromUriUseCase : UseCase<Uri, String?>() {
    override fun run(input: Uri?): String? {
        return input?.getQueryParameter(LinkedInConst.RESPONSE_TYPE_VALUE)
    }
}