package mayudin.helloworld

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mayudin.common.di.ComponentDependenciesProvider
import mayudin.common.di.HasComponentDependencies
import mayudin.helloworld.di.AppComponent
import mayudin.helloworld.di.DaggerAppComponent
import mayudin.helloworld.di.common.ComponentManager
import mayudin.helloworld.ui.theme.GithubHelloWorldDemoTheme
import mayudin.info.api.domain.model.GitHubRepository
import mayudin.info.api.presentation.viewmodel.InfoViewModelFactory
import mayudin.info.api.ui.navigation.INFO_FLOW
import mayudin.info.api.ui.navigation.INFO_ROUTE
import mayudin.info.api.ui.navigation.infoRoute
import mayudin.info.api.ui.navigation.openInfo
import mayudin.repos.api.ui.navigation.REPOS_FLOW
import mayudin.repos.api.ui.navigation.openRepos
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
                .repo(GitHubRepository(owner, repo)).build().viewModelFactory
        })
    }
}


@Composable
fun GreetingScreen(
    name: String,
    onNavigateToSecondScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Hello $name!",
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = onNavigateToSecondScreen, modifier = Modifier.padding(16.dp)) {
            Text("Go to Second Screen")
        }
    }
}

@Composable
fun SecondScreen(onNavigateBack: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "This is the Second Screen",
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = onNavigateBack, modifier = Modifier.padding(16.dp)) {
            Text("Go Back")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GithubHelloWorldDemoTheme {
        GreetingScreen(name = "Android", onNavigateToSecondScreen = {})
    }
}

@Preview(showBackground = true)
@Composable
fun SecondScreenPreview() {
    GithubHelloWorldDemoTheme {
        SecondScreen(onNavigateBack = {})
    }
}
