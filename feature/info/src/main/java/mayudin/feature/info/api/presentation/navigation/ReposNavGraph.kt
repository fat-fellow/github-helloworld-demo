package mayudin.feature.info.api.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import mayudin.feature.info.api.presentation.screen.InfoScreen

const val INFO_FLOW = "info-flow"
const val INFO_ROUTE = "info/list/{owner}/{repo}"

fun NavGraphBuilder.openInfo() {
    navigation(
        startDestination = INFO_ROUTE,
        route = INFO_FLOW
    ) {
        composable(
            route = "info/list/{owner}/{repo}"
        ) {
            InfoScreen()
        }
    }
}

fun infoRoute(owner: String, repo: String): String {
    return "info/list/$owner/$repo"
}
