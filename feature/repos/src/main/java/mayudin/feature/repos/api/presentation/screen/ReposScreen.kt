package mayudin.feature.repos.api.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mayudin.feature.repos.R
import mayudin.feature.repos.api.presentation.model.UiState
import mayudin.feature.repos.api.presentation.viewmodel.ReposViewModel

@Composable
fun ReposScreen(
    onNavigation: (String, String) -> Unit,
    viewModel: ReposViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var searchText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    ScreenLayout(
        uiState = uiState,
        searchText = searchText,
        onSearchTextChanged = { text ->
            searchText = text
            viewModel.onSearchTextChanged(text.text)
        },
        onNavigation = onNavigation,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenLayout(
    uiState: UiState,
    searchText: TextFieldValue,
    onSearchTextChanged: (TextFieldValue) -> Unit,
    onNavigation: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.github_repositories)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
        ) {
            SearchBar(
                searchText = searchText,
                onSearchTextChanged = onSearchTextChanged,
                hint = stringResource(R.string.search_hint),
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (uiState) {
                is UiState.Idle -> IdleLayout()
                is UiState.Loading -> LoadingLayout()
                is UiState.Success -> SuccessLayout(
                    repos = uiState.repos,
                    owner = uiState.owner,
                    onNavigation = onNavigation,
                )

                is UiState.Error -> ErrorLayout(uiState.message)
            }
        }
    }
}

@Composable
fun LoadingLayout() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = Color.Blue,
        )
    }
}

@Composable
fun IdleLayout() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.search_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
        )
    }
}

@Composable
fun SuccessLayout(
    repos: List<String>,
    owner: String,
    onNavigation: (String, String) -> Unit,
) {
    if (repos.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.no_repos_found),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                .border(
                    width = Dp.Hairline,
                    color = Color.Gray,
                    shape = RoundedCornerShape(8.dp),
                ),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(repos) { repo ->
                RepoItem(repo = repo) {
                    onNavigation(owner, repo)
                }
            }
        }
    }
}

@Composable
fun RepoItem(repo: String, onClick: () -> Unit) {
    Text(
        text = repo,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
    )
}

@Composable
fun ErrorLayout(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, color = Color.Red)
    }
}

@Composable
fun SearchBar(
    searchText: TextFieldValue,
    onSearchTextChanged: (TextFieldValue) -> Unit,
    hint: String = "",
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .border(width = Dp.Hairline, color = Color.Gray, shape = RoundedCornerShape(8.dp)),
    ) {
        if (searchText.text.isEmpty()) {
            Text(
                text = hint,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                modifier = Modifier.padding(8.dp),
            )
        }
        BasicTextField(
            value = searchText,
            onValueChange = onSearchTextChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        )
    }
}