package mayudin.feature.repos.api.presentation.screen

import androidx.annotation.VisibleForTesting
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import mayudin.common.utils.domain.Resultat
import mayudin.feature.repos.api.R
import mayudin.feature.repos.api.presentation.model.UiState
import mayudin.feature.repos.api.presentation.viewmodel.ReposViewModel
import mayudin.feature.repos.api.presentation.viewmodel.ReposViewModelFactory

@Composable
fun ReposScreen(
    factory: ReposViewModelFactory,
    viewModel: ReposViewModel = viewModel(factory = factory),
    onNavigation: (String, String) -> Unit,
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
        onNavigation = onNavigation
    )
}

@Composable
fun ScreenLayout(
    uiState: Resultat<UiState>,
    searchText: TextFieldValue,
    onSearchTextChanged: (TextFieldValue) -> Unit,
    onNavigation: (String, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchBar(
            searchText = searchText,
            onSearchTextChanged = onSearchTextChanged,
            hint = stringResource(R.string.search_hint)
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is Resultat.Loading -> LoadingLayout()
            is Resultat.Success -> SuccessLayout(
                repos = uiState.value.repos,
                owner = searchText.text,
                onNavigation = onNavigation
            )
            is Resultat.Failure -> ErrorLayout()
        }
    }
}

@Composable
fun LoadingLayout() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color.Blue,
            modifier = Modifier.semantics {
                contentDescription = "LoadingIndicator"
            }
        )
    }
}

@Composable
fun SuccessLayout(
    repos: List<String>,
    owner: String,
    onNavigation: (String, String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .border(
                width = Dp.Hairline,
                color = Color.Gray,
                shape = RoundedCornerShape(8.dp)
            ),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(repos) { repo ->
            RepoItem(repo = repo) {
                onNavigation(owner, repo)
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
            .clickable { onClick() }
    )
}

@Composable
fun ErrorLayout() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = stringResource(R.string.error_message), color = Color.Red)
    }
}

@Composable
fun SearchBar(
    searchText: TextFieldValue,
    onSearchTextChanged: (TextFieldValue) -> Unit,
    hint: String = ""
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .border(width = Dp.Hairline, color = Color.Gray, shape = RoundedCornerShape(8.dp))
    ) {
        if (searchText.text.isEmpty()) {
            Text(
                text = hint,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                modifier = Modifier.padding(8.dp)
            )
        }
        BasicTextField(
            value = searchText,
            onValueChange = onSearchTextChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}