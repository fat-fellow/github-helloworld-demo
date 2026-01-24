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
import mayudin.feature.info.api.domain.model.GitHubRepo
import mayudin.feature.info.api.presentation.navigation.infoRoute
import mayudin.feature.info.api.presentation.navigation.openInfo
import mayudin.feature.repos.api.presentation.navigation.REPOS_FLOW
import mayudin.feature.repos.api.presentation.navigation.openRepos
import mayudin.feature.repos.api.presentation.viewmodel.ReposViewModelFactory
import mayudin.feature.info.api.presentation.viewmodel.InfoViewModelFactory
import mayudin.helloworld.ui.theme.GithubHelloWorldDemoTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var reposViewModelFactory: ReposViewModelFactory

    @Inject
    lateinit var infoViewModelFactoryProvider: InfoViewModelFactory.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubHelloWorldDemoTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(
                        reposViewModelFactory = reposViewModelFactory,
                        infoViewModelFactoryProvider = infoViewModelFactoryProvider,
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
    reposViewModelFactory: ReposViewModelFactory,
    infoViewModelFactoryProvider: InfoViewModelFactory.Factory,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = REPOS_FLOW,
        modifier = modifier,
    ) {
        openRepos(
            reposViewModelFactory,
        ) { owner, repository ->
            navController.navigate(infoRoute(owner, repository))
        }
        openInfo(init = { owner, repo ->
            infoViewModelFactoryProvider.create(GitHubRepo(owner, repo))
        })
    }
}

