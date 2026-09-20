package com.example.myapplication.data.auth

import com.example.myapplication.BuildConfig

/**
 * Cognito configuration read from BuildConfig fields.
 *
 * Values are supplied via `local.properties` at build time:
 *   COGNITO_CLIENT_ID=your_client_id
 *   COGNITO_USER_POOL_ID=your_pool_id
 *   COGNITO_REGION=ap-south-1
 */
object CognitoConfig {

    val clientId: String = BuildConfig.COGNITO_CLIENT_ID

    val userPoolId: String = BuildConfig.COGNITO_USER_POOL_ID

    val region: String = BuildConfig.COGNITO_REGION

    /** Cognito User Pools HTTP endpoint. */
    val cognitoEndpoint: String
        get() = "https://cognito-idp.$region.amazonaws.com/"

    /** True when both Client ID and User Pool ID are non-blank. */
    val isConfigured: Boolean
        get() = clientId.isNotBlank() && userPoolId.isNotBlank()
}
