package com.AbdAllahAbdElFattah13.domain.usecases

import android.net.Uri
import com.AbdAllahAbdElFattah13.domain.abstracts.UseCase
import com.AbdAllahAbdElFattah13.domain.utils.LinkedInConst

class RetrieveAuthorizationTokenFromUriUseCase : UseCase<Uri, String?>() {
    override fun run(input: Uri?): String? {
        return input?.getQueryParameter(LinkedInConst.RESPONSE_TYPE_VALUE)
    }
}