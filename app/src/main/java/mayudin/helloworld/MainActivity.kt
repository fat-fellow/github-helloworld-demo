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
import mayudin.common.di.ComponentDependenciesProvider
import mayudin.common.di.HasComponentDependencies
import mayudin.feature.info.api.domain.model.GitHubRepo
import mayudin.feature.info.api.presentation.navigation.infoRoute
import mayudin.feature.info.api.presentation.navigation.openInfo
import mayudin.feature.repos.api.presentation.navigation.REPOS_FLOW
import mayudin.feature.repos.api.presentation.navigation.openRepos
import mayudin.helloworld.di.AppComponent
import mayudin.helloworld.di.DaggerAppComponent
import mayudin.helloworld.di.common.ComponentManager
import mayudin.helloworld.ui.theme.GithubHelloWorldDemoTheme
import javax.inject.Inject

class MainActivity : ComponentActivity(), HasComponentDependencies {

    @Inject
    override lateinit var dependencies: ComponentDependenciesProvider

    val appComponent: AppComponent by lazy {
        DaggerAppComponent
            .factory()
            .create(this)
    }

    val componentManager by lazy {
        ComponentManager(appComponent, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        enableEdgeToEdge()
        setContent {
            GithubHelloWorldDemoTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(
                        componentManager = componentManager,
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavHost(
    componentManager: ComponentManager,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = REPOS_FLOW,
        modifier = modifier
    ) {
        openRepos(
            componentManager.repoComponent.get().viewModelFactory,
        ) { owner, repository ->
            navController.navigate(infoRoute(owner, repository))
        }
        openInfo(init = { owner, repo ->
            componentManager.main.infoComponentBuilder()
                .repo(GitHubRepo(owner, repo)).build().viewModelFactory
        })
    }
}