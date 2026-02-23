package mayudin.helloworld.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import mayudin.feature.info.presentation.navigation.InfoRoute
import mayudin.feature.info.presentation.navigation.openInfo
import mayudin.feature.repos.presentation.navigation.ReposRoute
import mayudin.feature.repos.presentation.navigation.openRepos
import mayudin.helloworld.ui.theme.GithubHelloWorldDemoTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubHelloWorldDemoTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = ReposRoute,
        modifier = modifier.fillMaxSize(),
    ) {
        openRepos { owner, repo ->
            navController.navigate(InfoRoute(owner, repo))
        }
        openInfo(
            onNavigateBack = { navController.popBackStack() },
        )
    }
}
