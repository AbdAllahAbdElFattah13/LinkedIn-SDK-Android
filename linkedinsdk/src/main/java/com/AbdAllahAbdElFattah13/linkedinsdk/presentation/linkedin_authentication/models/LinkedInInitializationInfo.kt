package com.AbdAllahAbdElFattah13.linkedinsdk.presentation.linkedin_authentication.models

data class LinkedInInitializationInfo(val clientId: String, val clientSecretKey: String, val redirectUri: String, val accessTokenOnlyRequest: Boolean)