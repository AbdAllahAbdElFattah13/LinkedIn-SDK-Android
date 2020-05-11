package com.AbdAllahAbdElFattah13.linkedinsdk.ui

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LinkedInUser(val id: String?, val email: String?,
                        val firstName: String?, val lastName: String?, val profilePictureUrl: String?,
                        val accessToken: String, val accessTokenExpiry: Long) : Parcelable