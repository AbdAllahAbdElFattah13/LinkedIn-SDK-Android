package com.AbdAllahAbdElFattah13.linkedinsdk.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.AbdAllahAbdElFattah13.linkedinsdk.R
import com.AbdAllahAbdElFattah13.linkedinsdk.di.DependencyGraph
import com.AbdAllahAbdElFattah13.linkedinsdk.domain.usecases.retrieve_access_token.models.LinkedInAccessTokenInfo
import com.AbdAllahAbdElFattah13.linkedinsdk.domain.usecases.retrieve_basic_profile.models.LinkedInBasicProfileInfo
import com.AbdAllahAbdElFattah13.linkedinsdk.domain.utils.Executors
import com.AbdAllahAbdElFattah13.linkedinsdk.domain.utils.LinkedInConst.RESPONSE_TYPE_VALUE
import com.AbdAllahAbdElFattah13.linkedinsdk.domain.utils.LinkedInConst.getAuthorizationUrl
import com.AbdAllahAbdElFattah13.linkedinsdk.domain.utils.RequestHandler
import com.AbdAllahAbdElFattah13.linkedinsdk.presentation.linkedin_authentication.models.LinkedInAuthenticationState
import com.AbdAllahAbdElFattah13.linkedinsdk.presentation.linkedin_authentication.models.LinkedInInitializationInfo
import com.AbdAllahAbdElFattah13.linkedinsdk.presentation.linkedin_authentication.viewmodel.LinkedInAuthenticationViewModel
import com.AbdAllahAbdElFattah13.linkedinsdk.ui.linkedin_builder.LinkedInFromActivityBuilder
import kotlinx.android.synthetic.main.linkedin_activity_linkedin_authentication.*

class LinkedInAuthenticationActivity : AppCompatActivity() {

    private val clientId: String by lazy { intent.getStringExtra(LinkedInFromActivityBuilder.CLIENT_ID) }
    private val clientSecretKey by lazy { intent.getStringExtra(LinkedInFromActivityBuilder.CLIENT_SECRET_KEY) }
    private val redirectUri: String by lazy { intent.getStringExtra(LinkedInFromActivityBuilder.REDIRECT_URI) }
    private val state: String by lazy { intent.getStringExtra(LinkedInFromActivityBuilder.STATE) }
    private val accessTokenOnlyRequest: Boolean by lazy { intent.getBooleanExtra(LinkedInFromActivityBuilder.ACCESS_TOKEN_ONLY, false) }

    private val progressDialog: AlertDialog by lazy {
        AlertDialog.Builder(this@LinkedInAuthenticationActivity)
                .setCancelable(false)
                .setView(R.layout.linkedin_layout_progress_dialog)
                .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.linkedin_activity_linkedin_authentication)

        //enable fullscreen mode
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()

        val dependencyGraph = createDependencyGraph(clientId, clientSecretKey, redirectUri, accessTokenOnlyRequest)
        val vm = dependencyGraph
                .providesLinkedInAuthenticationViewModelFactory().create(LinkedInAuthenticationViewModel::class.java)

        web_view_linkedin_login.requestFocus(View.FOCUS_DOWN)
        web_view_linkedin_login.clearHistory()
        web_view_linkedin_login.clearCache(true)

        setProgressDialogVisibility(true)

        vm.state.observe(this, Observer { state ->
            when (state) {
                LinkedInAuthenticationState.Loading -> {
                    setProgressDialogVisibility(true)
                }
                is LinkedInAuthenticationState.AccessTokenRetrievedSuccessfully -> {
                    setProgressDialogVisibility(false)
                    val linkedinUser: LinkedInUser = createLinkedInUser(state.accessTokenInfo)
                    returnResultBack(linkedinUser)
                }
                is LinkedInAuthenticationState.FailedToRetrieveAccessToken -> {
                    setProgressDialogVisibility(false)
                }
                is LinkedInAuthenticationState.BasicProfileRetrievedSuccessfully -> {
                    setProgressDialogVisibility(false)

                    val linkedinUser: LinkedInUser = createLinkedInUser(state.profileInfo)
                    returnResultBack(linkedinUser)
                }
                is LinkedInAuthenticationState.FailedToRetrieveProfile -> {
                    setProgressDialogVisibility(false)

                    val err = state.accessTokenRetrievalError
                    val outputIntent = Intent().apply {
                        putExtra("error_code", 1019)
                        putExtra("error_msg", err.localizedMessage)
                    }

                    setResult(Activity.RESULT_CANCELED, outputIntent)
                    finish()
                }
            }
        })

        web_view_linkedin_login.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                setProgressDialogVisibility(false)
            }

            //to support below Android N we need to use the deprecated method only
            override fun shouldOverrideUrlLoading(view: WebView, authorizationUrl: String): Boolean {
                setProgressDialogVisibility(true)
                if (authorizationUrl.startsWith(redirectUri)) {
                    val uri = Uri.parse(authorizationUrl)
                    //vm.onRedirect(uri);
                    val stateToken = uri.getQueryParameter(STATE_PARAM)
                    if (stateToken == null || stateToken != state) {
                        Log.e(LinkedInFromActivityBuilder.TAG, "State token doesn't match")
                        return true
                    }

                    //If the user doesn't allow authorization to our application, the authorizationToken Will be null.
                    val authorizationToken = uri.getQueryParameter(RESPONSE_TYPE_VALUE)
                    if (authorizationToken == null) {
                        val intent = Intent()
                        intent.putExtra("err_code", LinkedInFromActivityBuilder.ERROR_USER_DENIED)
                        intent.putExtra("err_message", "Authorization not received. User didn't allow access to account.")
                        setResult(Activity.RESULT_CANCELED, intent)
                        finish()
                    } else {
                        vm.onAuthorizationTokenGranted(authorizationToken)
                    }
                } else {
                    //Default behaviour
                    web_view_linkedin_login.loadUrl(authorizationUrl)
                }
                return true
            }
        }
        val authUrl = getAuthorizationUrl(clientId, redirectUri, state)
        web_view_linkedin_login.loadUrl(authUrl)
    }

    private fun createDependencyGraph(clientId: String, clientSecret: String, redirectUri: String, accessTokenOnlyRequest: Boolean): DependencyGraph {
        val linkedInInitializationInfo = LinkedInInitializationInfo(clientId, clientSecret, redirectUri, accessTokenOnlyRequest)
        return DependencyGraph(linkedInInitializationInfo, RequestHandler, Executors())
    }

    private fun setProgressDialogVisibility(show: Boolean) {
        if (!this@LinkedInAuthenticationActivity.isFinishing) {
            if (show) {
                progressDialog.show()
            } else {
                progressDialog.dismiss()
            }
        }
    }

    private fun createLinkedInUser(accessTokenInfo: LinkedInAccessTokenInfo): LinkedInUser {
        return LinkedInUser().apply {
            accessToken = accessTokenInfo.accessToken
            accessTokenExpiry = accessTokenInfo.accessTokenExpiry
        }
    }

    private fun createLinkedInUser(basicProfileInfo: LinkedInBasicProfileInfo): LinkedInUser {
        return LinkedInUser().apply {
            id = basicProfileInfo.id
            email = basicProfileInfo.email
            firstName = basicProfileInfo.firstName
            lastName = basicProfileInfo.lastName
            profilePictureUrl = basicProfileInfo.profilePictureUrl
            accessToken = basicProfileInfo.accessToken
            accessTokenExpiry = basicProfileInfo.accessTokenExpiry
        }
    }

    private fun returnResultBack(linkedinUser: LinkedInUser) {
        val outputIntent = Intent().apply {
            putExtra("social_login", linkedinUser)
        }
        setResult(Activity.RESULT_OK, outputIntent)
        finish()
    }

    companion object {
        private const val STATE_PARAM = "state"
    }
}