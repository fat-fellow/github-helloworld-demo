package mayudin.feature.repos.api.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import mayudin.feature.repos.api.presentation.screen.ReposScreen

fun NavGraphBuilder.openRepos(
    onNavigateToInfo: (owner: String, repo: String) -> Unit
) {
    composable<ReposRoute> {
        ReposScreen(onNavigation = onNavigateToInfo)
    }
}

