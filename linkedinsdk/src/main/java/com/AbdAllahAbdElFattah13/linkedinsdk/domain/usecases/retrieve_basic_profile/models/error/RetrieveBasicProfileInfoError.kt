package com.AbdAllahAbdElFattah13.linkedinsdk.domain.usecases.retrieve_basic_profile.models.error

sealed class RetrieveBasicProfileInfoError(msg: String) : RuntimeException(msg) {
    class NullInput(msg: String) : RetrieveBasicProfileInfoError(msg)
    class FailedToRetrieveBasicProfileInfo(msg: String) : RetrieveBasicProfileInfoError(msg)
}