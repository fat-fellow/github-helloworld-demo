package mayudin.helloworld

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import mayudin.feature.info.api.presentation.navigation.infoRoute
import mayudin.feature.info.api.presentation.navigation.openInfo
import mayudin.feature.repos.api.presentation.navigation.REPOS_FLOW
import mayudin.feature.repos.api.presentation.navigation.openRepos
import mayudin.helloworld.ui.theme.GithubHelloWorldDemoTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubHelloWorldDemoTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = REPOS_FLOW,
        modifier = modifier,
    ) {
        openRepos { owner, repository ->
            navController.navigate(infoRoute(owner, repository))
        }
        openInfo()
    }
}

