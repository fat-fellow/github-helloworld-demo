package mayudin.repos.api.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import mayudin.repos.api.presentation.viewmodel.ReposViewModelFactory
import mayudin.repos.api.ui.view.ReposScreen

const val REPOS_FLOW = "repos-flow"
const val REPOS_ROUTE = "repos/list"

fun NavGraphBuilder.openRepos(
    factory: ReposViewModelFactory,
    onNavigation: (String, String) -> Unit
) {
    navigation(
        startDestination = REPOS_ROUTE,
        route = REPOS_FLOW
    ) {
        composable(route = REPOS_ROUTE) {
            ReposScreen(factory, onNavigation = onNavigation)
        }
    }
}

