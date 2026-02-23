package mayudin.feature.info.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import mayudin.feature.info.presentation.ui.InfoScreen

fun NavGraphBuilder.openInfo(onNavigateBack: () -> Unit) {
    composable<InfoRoute> {
        val args = it.toRoute<InfoRoute>()
        InfoScreen(
            owner = args.owner,
            repo = args.repo,
            onNavigateBack = onNavigateBack,
        )
    }
}
