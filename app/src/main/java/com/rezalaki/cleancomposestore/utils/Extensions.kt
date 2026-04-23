package com.rezalaki.cleancomposestore.utils

import android.content.res.Configuration
import android.content.res.Resources
import androidx.activity.ComponentActivity
import java.util.Locale


fun ComponentActivity.setAppLocaleEn(){
    val locale = Locale("en")
    Locale.setDefault(locale)
    val resources: Resources = this.resources
    val config: Configuration = resources.configuration
    config.setLocale(locale)
    resources.updateConfiguration(config, resources.displayMetrics)
}
