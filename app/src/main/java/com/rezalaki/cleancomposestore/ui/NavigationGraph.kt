package com.rezalaki.cleancomposestore.ui

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.savedstate.SavedState
import com.rezalaki.cleancomposestore.ui.newScreens.NewHomeScreen
import com.rezalaki.cleancomposestore.ui.screen.detailscreen.DetailScreen
import kotlinx.serialization.Serializable


private object Route {
    @Serializable
    object Home

    @Serializable
    data class Detail(val productId: Int)
}


@Composable
fun NavigationGraph() {
    val navController = rememberNavController()
    navController.addOnDestinationChangedListener { controller, destination: NavDestination, arguments: SavedState? ->
        Log.d("ROUTING", "route: ${destination.route} | args: ${arguments.toString()}");
    }
    NavHost(
        navController,
        startDestination = Route.Home
    ) {
        composable<Route.Home> {
            NewHomeScreen(
                goToDetail = { productId: Int ->
                    navController.navigate(
                        Route.Detail(productId)
                    )
                }
            )
        }

        composable<Route.Detail>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Companion.Down,
                    animationSpec = tween(500)
                )
            }
        ) {
            val args = it.toRoute<Route.Detail>()
            DetailScreen(
                productId = args.productId,
                onBackPressed = navController::popBackStack
            )
        }

    }
}
