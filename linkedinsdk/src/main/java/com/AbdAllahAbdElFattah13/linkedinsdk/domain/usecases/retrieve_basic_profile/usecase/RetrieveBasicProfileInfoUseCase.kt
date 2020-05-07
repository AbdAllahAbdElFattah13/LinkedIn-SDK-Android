package com.AbdAllahAbdElFattah13.linkedinsdk.domain.usecases.retrieve_basic_profile.usecase

import android.util.Log
import com.AbdAllahAbdElFattah13.linkedinsdk.domain.abstracts.UseCase
import com.AbdAllahAbdElFattah13.linkedinsdk.domain.usecases.retrieve_access_token.models.LinkedInAccessTokenInfo
import com.AbdAllahAbdElFattah13.linkedinsdk.domain.usecases.retrieve_basic_profile.models.LinkedInBasicProfileInfo
import com.AbdAllahAbdElFattah13.linkedinsdk.domain.usecases.retrieve_basic_profile.models.error.RetrieveBasicProfileInfoError
import com.AbdAllahAbdElFattah13.linkedinsdk.domain.utils.RequestHandler
import com.AbdAllahAbdElFattah13.linkedinsdk.domain.utils.RequestHandler.sendGet
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class RetrieveBasicProfileInfoUseCase(private val requestHandler: RequestHandler) : UseCase<LinkedInAccessTokenInfo, LinkedInBasicProfileInfo>() {

    private val TAG = "RetrieveBasicProfileInf"

    override fun run(input: LinkedInAccessTokenInfo?): LinkedInBasicProfileInfo {
        if (input == null) throw RetrieveBasicProfileInfoError.NullInput("Input can't be null for RetrieveBasicProfileInfo")

        try {
            val email = retrieveEmailFromAPI(input)
            val profileInfo = retrieveBasicProfile(input, email)

            return profileInfo
        } catch (e: Exception) {
            throw RetrieveBasicProfileInfoError.FailedToRetrieveBasicProfileInfo(e.localizedMessage)
        }
    }

    /**
     * Method that retrieves user email from LinkedIn emailAddress API
     */
    @Throws(IOException::class, JSONException::class)
    private fun retrieveEmailFromAPI(accessTokenInfo: LinkedInAccessTokenInfo): String {
        val emailUrl = "https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))"
        val result = sendGet(emailUrl, accessTokenInfo.accessToken)
        if (result != null) {
            val jsonObject = JSONObject(result)
            val email = jsonObject.getJSONArray("elements").getJSONObject(0).getJSONObject("handle~").getString("emailAddress")
            return email
        } else {
            Log.e(TAG, "Failed To Retrieve Email from LinkedIn API")
            throw RetrieveBasicProfileInfoError.FailedToRetrieveBasicProfileInfo("Failed To Retrieve Basic Profile")
        }
    }

    /**
     * Method that retrieves user basic info from LinkedIn API
     */
    @Throws(IOException::class, JSONException::class)
    private fun retrieveBasicProfile(accessTokenInfo: LinkedInAccessTokenInfo, email: String): LinkedInBasicProfileInfo {
        val profileEndpoint = "https://api.linkedin.com/v2/me?projection=(id,firstName,lastName,profilePicture(displayImage~:playableStreams))"
        val result = requestHandler.sendGet(profileEndpoint, accessTokenInfo.accessToken)
        if (result != null) {
            val jsonObject = JSONObject(result)
            val linkedInUserId = jsonObject.getString("id")

            val country = jsonObject
                    .getJSONObject("firstName")
                    .getJSONObject("preferredLocale")
                    .getString("country")
            val language = jsonObject
                    .getJSONObject("firstName")
                    .getJSONObject("preferredLocale")
                    .getString("language")
            val firstNameKey = language + "_" + country
            val linkedInUserFirstName = jsonObject.getJSONObject("firstName")
                    .getJSONObject("localized")
                    .getString(firstNameKey)

            val linkedInUserLastName = jsonObject.getJSONObject("lastName")
                    .getJSONObject("localized")
                    .getString(firstNameKey)

            //to take care of users without pp
            val linkedInUserProfile = try {
                jsonObject
                        .getJSONObject("profilePicture")
                        .getJSONObject("displayImage~")
                        .getJSONArray("elements")
                        .getJSONObject(0)
                        .getJSONArray("identifiers")
                        .getJSONObject(0)
                        .getString("identifier")
            } catch (e: Exception) {
                null
            }

            return LinkedInBasicProfileInfo(linkedInUserId, email, linkedInUserFirstName, linkedInUserLastName, linkedInUserProfile, accessTokenInfo.accessToken, accessTokenInfo.accessTokenExpiry)

        } else {
            Log.e(TAG, "Failed To Retrieve Basic Profile")
            throw RetrieveBasicProfileInfoError.FailedToRetrieveBasicProfileInfo("Failed To Retrieve Basic Profile")
        }
    }

}