package com.AbdAllahAbdElFattah13.domain.usecases.retrieve_access_token.usecases

import com.AbdAllahAbdElFattah13.domain.abstracts.UseCase
import com.AbdAllahAbdElFattah13.domain.usecases.retrieve_access_token.models.LinkedInAccessTokenInfo
import com.AbdAllahAbdElFattah13.domain.usecases.retrieve_access_token.models.errors.RetrieveAccessTokenError
import com.AbdAllahAbdElFattah13.domain.utils.LinkedInConst
import com.AbdAllahAbdElFattah13.domain.utils.RequestHandler
import org.json.JSONObject
import java.util.*

class RetrieveAccessTokenUseCase(private val requestHandler: RequestHandler) : UseCase<RetrieveAccessTokenUseCase.RetrieveAccessTokenInputs, LinkedInAccessTokenInfo>() {

    private fun getAccessTokenEndpoint(inputs: RetrieveAccessTokenInputs): String {
        return (LinkedInConst.ACCESS_TOKEN_URL
                + LinkedInConst.QUESTION_MARK
                + LinkedInConst.GRANT_TYPE_PARAM + LinkedInConst.EQUALS + LinkedInConst.GRANT_TYPE
                + LinkedInConst.AMPERSAND
                + LinkedInConst.RESPONSE_TYPE_VALUE + LinkedInConst.EQUALS + inputs.authorizationToken
                + LinkedInConst.AMPERSAND
                + LinkedInConst.CLIENT_ID_PARAM + LinkedInConst.EQUALS + inputs.clientId
                + LinkedInConst.AMPERSAND
                + LinkedInConst.REDIRECT_URI_PARAM + LinkedInConst.EQUALS + inputs.redirectUri
                + LinkedInConst.AMPERSAND
                + LinkedInConst.SECRET_KEY_PARAM + LinkedInConst.EQUALS + inputs.clientSecretKey)
    }

    override fun run(input: RetrieveAccessTokenInputs?): LinkedInAccessTokenInfo {
        if (input == null) throw RetrieveAccessTokenError.NullInput("RetrieveAccessTokenUseCase inputs can't be null!")

        val accessTokenEndpoint = getAccessTokenEndpoint(input)
        val resultString = requestHandler.sendPost(accessTokenEndpoint, JSONObject())
        if (resultString != null) {
            val resultJson = JSONObject(resultString)
            val expiresIn = if (resultJson.has("expires_in")) resultJson.getInt("expires_in") else 0
            val accessToken = if (resultJson.has("access_token")) resultJson.getString("access_token") else null

            if (expiresIn > 0 && accessToken != null) {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.SECOND, expiresIn)
                val expireDate = calendar.timeInMillis

                val linkedInAccessTokenInfo = LinkedInAccessTokenInfo(accessToken, expireDate)
                return linkedInAccessTokenInfo
            } else {
                throw RetrieveAccessTokenError.AccessTokenRequestFailed("API call returned null access token!")
            }
        } else {
            throw RetrieveAccessTokenError.AccessTokenRequestFailed("Access token API failed!")
        }
    }

    data class RetrieveAccessTokenInputs(val authorizationToken: String, val clientId: String, val redirectUri: String, val clientSecretKey: String)
}