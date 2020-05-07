package com.AbdAllahAbdElFattah13.linkedinsdk.di

import com.AbdAllahAbdElFattah13.linkedinsdk.domain.usecases.retrieve_access_token.usecases.RetrieveAccessTokenUseCase
import com.AbdAllahAbdElFattah13.linkedinsdk.domain.usecases.retrieve_basic_profile.usecase.RetrieveBasicProfileInfoUseCase
import com.AbdAllahAbdElFattah13.linkedinsdk.domain.utils.Executors
import com.AbdAllahAbdElFattah13.linkedinsdk.domain.utils.RequestHandler
import com.AbdAllahAbdElFattah13.linkedinsdk.presentation.linkedin_authentication.models.LinkedInInitializationInfo
import com.AbdAllahAbdElFattah13.linkedinsdk.presentation.linkedin_authentication.viewmodel.LinkedInAuthenticationViewModelFactory

class DI(
        private val initializationInfo: LinkedInInitializationInfo,
        private val requestHandler: RequestHandler,
        private val executors: Executors
) {
    fun providesRetrieveAccessTokenUseCase() = RetrieveAccessTokenUseCase(requestHandler)
    fun providesRetrieveBasicProfileInfoUseCase() = RetrieveBasicProfileInfoUseCase(requestHandler)
    fun providesLinkedInAuthenticationViewModelFactory() = LinkedInAuthenticationViewModelFactory(
            initializationInfo,
            providesRetrieveAccessTokenUseCase(),
            providesRetrieveBasicProfileInfoUseCase(),
            executors
    )
}