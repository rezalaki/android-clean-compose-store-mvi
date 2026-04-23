package com.rezalaki.cleancomposestore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rezalaki.cleancomposestore.ui.NavigationGraph
import com.rezalaki.cleancomposestore.ui.theme.CleanComposeStoreTheme
import com.rezalaki.cleancomposestore.utils.setAppLocaleEn
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAppLocaleEn()
        enableEdgeToEdge()
        setContent {
            CleanComposeStoreTheme {
                NavigationGraph()
            }
        }
    }

}
