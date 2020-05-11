package com.AbdAllahAbdElFattah13.linkedinsdk.ui.linkedin_builder

import android.content.Context
import android.content.Intent
import android.util.Log
import com.AbdAllahAbdElFattah13.linkedinsdk.ui.LinkedInAuthenticationActivity
import java.util.*

/**
 * Created by AbdAllah AbdElfattah on 11/04/2020,
 * Cairo, Egypt.
 */
abstract class LinkedInBuilder internal constructor(context: Context?) {

    private val intent: Intent = Intent(context, LinkedInAuthenticationActivity::class.java)
    private var state: String? = null
    private var accessTokenRetrievalOnlyRequest = false

    abstract fun self(): LinkedInBuilder
    abstract fun startActivityForResult(intent: Intent, requestCode: Int)

    fun setClientID(clientID: String): LinkedInBuilder {
        updateIntent(CLIENT_ID, clientID)
        return self()
    }

    fun setClientSecret(clientSecret: String): LinkedInBuilder {
        updateIntent(CLIENT_SECRET_KEY, clientSecret)
        return self()
    }

    fun setRedirectURI(redirectURI: String): LinkedInBuilder {
        updateIntent(REDIRECT_URI, redirectURI)
        return self()
    }

    fun setState(state: String): LinkedInBuilder {
        this.state = state
        updateIntent(STATE, state)
        return self()
    }

    fun setAccessTokenRetrievalOnlyRequest(accessTokenRetrievalOnlyRequest: Boolean): LinkedInBuilder {
        this.accessTokenRetrievalOnlyRequest = accessTokenRetrievalOnlyRequest
        intent.putExtra(ACCESS_TOKEN_ONLY, accessTokenRetrievalOnlyRequest)
        return self()
    }

    fun authenticate(requestCode: Int) {
        if (validateAuthenticationParams()) {
            if (state == null) {
                generateState()
            }
            startActivityForResult(intent, requestCode)
        }
    }

    private fun updateIntent(key: String, value: String) {
        intent.putExtra(key, value)
    }

    private fun validateAuthenticationParams(): Boolean {
        if (intent.getStringExtra(CLIENT_ID) == null) {
            Log.e(TAG, "Client ID is required", IllegalArgumentException())
            return false
        }
        if (intent.getStringExtra(CLIENT_SECRET_KEY) == null) {
            Log.e(TAG, "Client Secret is required", IllegalArgumentException())
            return false
        }
        if (intent.getStringExtra(REDIRECT_URI) == null) {
            Log.e(TAG, "Redirect URI is required", IllegalArgumentException())
            return false
        }
        return true
    }

    private fun generateState() {
        val allowedCharSet = "0123456789qwertyuiopasdfghjklzxcvbnmMNBVCXZLKJHGFDSAQWERTYUIOP"
        val random = Random()
        val sb = StringBuilder(16)
        for (i in 0..15) sb.append(allowedCharSet[random.nextInt(allowedCharSet.length)])
        state = sb.toString()
        intent.putExtra(STATE, state)
    }

    companion object {
        const val CLIENT_ID = "client_id"
        const val CLIENT_SECRET_KEY = "client_secret"
        const val REDIRECT_URI = "redirect_uri"
        const val STATE = "state"
        const val ACCESS_TOKEN_ONLY = "access_token_only"
        const val TAG = "LinkedInAuth"
        const val ERROR_USER_DENIED = 11
        const val ERROR_FAILED = 12
    }
}