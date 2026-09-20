package com.example.myapplication.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {

    private const val BASE_URL =
        "https://c3ou54e5g0.execute-api.ap-south-1.amazonaws.com/"

    /**
     * Token provider set by [com.example.myapplication.ArogyaApplication].
     * Returns the current access token, or `null` in demo / unauthenticated mode.
     */
    private var tokenProvider: (() -> String?)? = null

    fun setTokenProvider(provider: () -> String?) {
        tokenProvider = provider
    }

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val token = tokenProvider?.invoke()
                if (token != null) {
                    chain.proceed(
                        original.newBuilder()
                            .addHeader("Authorization", "Bearer $token")
                            .build()
                    )
                } else {
                    chain.proceed(original)
                }
            }
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}