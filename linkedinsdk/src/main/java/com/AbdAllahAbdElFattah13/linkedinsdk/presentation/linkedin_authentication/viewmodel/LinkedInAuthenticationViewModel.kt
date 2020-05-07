package com.AbdAllahAbdElFattah13.linkedinsdk.presentation.linkedin_authentication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.AbdAllahAbdElFattah13.linkedinsdk.domain.usecases.retrieve_access_token.models.errors.RetrieveAccessTokenError
import com.AbdAllahAbdElFattah13.linkedinsdk.domain.usecases.retrieve_access_token.usecases.RetrieveAccessTokenUseCase
import com.AbdAllahAbdElFattah13.linkedinsdk.domain.usecases.retrieve_basic_profile.models.error.RetrieveBasicProfileInfoError
import com.AbdAllahAbdElFattah13.linkedinsdk.domain.usecases.retrieve_basic_profile.usecase.RetrieveBasicProfileInfoUseCase
import com.AbdAllahAbdElFattah13.linkedinsdk.domain.utils.Executors
import com.AbdAllahAbdElFattah13.linkedinsdk.presentation.linkedin_authentication.models.LinkedInAuthenticationState
import com.AbdAllahAbdElFattah13.linkedinsdk.presentation.linkedin_authentication.models.LinkedInInitializationInfo

class LinkedInAuthenticationViewModel(
        private val initializationInfo: LinkedInInitializationInfo,
        private val retrieveAccessTokenUseCase: RetrieveAccessTokenUseCase,
        private val retrieveBasicProfileInfoUseCase: RetrieveBasicProfileInfoUseCase,
        private val executors: Executors
) : ViewModel() {

    private val _state = MutableLiveData<LinkedInAuthenticationState>()
    val state: LiveData<LinkedInAuthenticationState> = _state

    fun onAuthorizationTokenGranted(authorizationToken: String) {
        _state.value = LinkedInAuthenticationState.Loading

        executors.networkIO().execute {
            val accessTokenInfo = try {
                retrieveAccessTokenUseCase.run(RetrieveAccessTokenUseCase.RetrieveAccessTokenInputs(
                        authorizationToken = authorizationToken,
                        clientId = initializationInfo.clientId,
                        clientSecretKey = initializationInfo.clientSecretKey,
                        redirectUri = initializationInfo.redirectUri
                ))
            } catch (e: RetrieveAccessTokenError) {
                executors.mainThread().execute {
                    _state.value = LinkedInAuthenticationState.FailedToRetrieveAccessToken(e)
                }
                return@execute
            }
            executors.mainThread().execute {
                _state.value = LinkedInAuthenticationState.AccessTokenRetrievedSuccessfully(accessTokenInfo)
            }

            val profileInfo = try {
                retrieveBasicProfileInfoUseCase.run(accessTokenInfo)
            } catch (e: RetrieveBasicProfileInfoError) {
                executors.mainThread().execute {
                    _state.value = LinkedInAuthenticationState.FailedToRetrieveProfile(e)
                }
                return@execute
            }
            executors.mainThread().execute {
                _state.value = LinkedInAuthenticationState.BasicProfileRetrievedSuccessfully(profileInfo)
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class LinkedInAuthenticationViewModelFactory constructor(
        private val initializationInfo: LinkedInInitializationInfo,
        private val retrieveAccessTokenUseCase: RetrieveAccessTokenUseCase,
        private val retrieveBasicProfileInfoUseCase: RetrieveBasicProfileInfoUseCase,
        private val executors: Executors
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LinkedInAuthenticationViewModel::class.java)) {
            return (LinkedInAuthenticationViewModel(
                    initializationInfo,
                    retrieveAccessTokenUseCase,
                    retrieveBasicProfileInfoUseCase,
                    executors
            )) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class!")
    }
}