package com.AbdAllahAbdElFattah13.domain.usecases.retrieve_access_token.models.errors

sealed class RetrieveAccessTokenError(msg: String) : RuntimeException("RetrieveAccessTokenError: $msg") {

    class NullInput(msg: String) : RetrieveAccessTokenError(msg)
    class AccessTokenRequestFailed(msg: String) : RetrieveAccessTokenError(msg)

}