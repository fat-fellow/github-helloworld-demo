package mayudin.feature.repos.api.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import mayudin.feature.repos.api.presentation.screen.ReposScreen

const val REPOS_FLOW = "repos-flow"
const val REPOS_ROUTE = "repos/list"

fun NavGraphBuilder.openRepos(
    onNavigation: (String, String) -> Unit
) {
    navigation(
        startDestination = REPOS_ROUTE,
        route = REPOS_FLOW
    ) {
        composable(route = REPOS_ROUTE) {
            ReposScreen(onNavigation = onNavigation)
        }
    }
}

