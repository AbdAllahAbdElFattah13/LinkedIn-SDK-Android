package com.AbdAllahAbdElFattah13.linkedinsdk.domain.utils

object LinkedInConst {
    const val AUTHORIZATION_URL = "https://www.linkedin.com/uas/oauth2/authorization"
    const val ACCESS_TOKEN_URL = "https://www.linkedin.com/uas/oauth2/accessToken"
    const val SECRET_KEY_PARAM = "client_secret"
    const val RESPONSE_TYPE_PARAM = "response_type"
    const val GRANT_TYPE_PARAM = "grant_type"
    const val GRANT_TYPE = "authorization_code"
    const val RESPONSE_TYPE_VALUE = "code"
    const val CLIENT_ID_PARAM = "client_id"
    const val STATE_PARAM = "state"
    const val REDIRECT_URI_PARAM = "redirect_uri"
    const val QUESTION_MARK = "?"
    const val AMPERSAND = "&"
    const val EQUALS = "="

    fun getAuthorizationUrl(clientId: String, redirectUri: String, state: String): String = (AUTHORIZATION_URL
            + QUESTION_MARK + RESPONSE_TYPE_PARAM + EQUALS + RESPONSE_TYPE_VALUE
            + AMPERSAND + CLIENT_ID_PARAM + EQUALS + clientId
            + AMPERSAND + STATE_PARAM + EQUALS + state
            + AMPERSAND + REDIRECT_URI_PARAM + EQUALS + redirectUri
            + AMPERSAND + "scope=r_liteprofile%20r_emailaddress")
}