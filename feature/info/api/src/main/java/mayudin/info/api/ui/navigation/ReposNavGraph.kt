package mayudin.info.api.ui.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import mayudin.info.api.presentation.viewmodel.InfoViewModelFactory
import mayudin.info.api.ui.view.InfoScreen

const val INFO_FLOW = "info-flow"
const val INFO_ROUTE = "info/list/{owner}/{repo}"

fun NavGraphBuilder.openInfo(
    init: (String, String) -> InfoViewModelFactory
) {
    navigation(
        startDestination = INFO_ROUTE,
        route = INFO_FLOW
    ) {
        composable(
            route = "info/list/{owner}/{repo}"
        ) { backStackEntry ->
            val owner = backStackEntry.arguments?.getString("owner") ?: ""
            val repo = backStackEntry.arguments?.getString("repo") ?: ""
            var factory by remember { mutableStateOf((init(owner, repo))) }

            InfoScreen(
                factory = factory,
            )
        }
    }
}

fun infoRoute(owner: String, repo: String): String {
    return "info/list/$owner/$repo"
}
