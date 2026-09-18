package com.example.myapplication.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleUtils {
    fun getLocalizedContext(context: Context, languageCode: String): Context {
        val locale = when (languageCode.lowercase()) {
            "as" -> Locale.forLanguageTag("as-IN")
            "bn" -> Locale.forLanguageTag("bn-IN")
            "hi" -> Locale.forLanguageTag("hi-IN")
            else -> Locale.ENGLISH
        }
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }
}
