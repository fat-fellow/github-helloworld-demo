package mayudin.feature.info.api.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import mayudin.feature.info.api.presentation.screen.InfoScreen

fun NavGraphBuilder.openInfo(
    onNavigateBack: () -> Unit
) {
    composable<InfoRoute> {
        val args = it.toRoute<InfoRoute>()
        InfoScreen(
            owner = args.owner,
            repo = args.repo,
            onNavigateBack = onNavigateBack
        )
    }
}
