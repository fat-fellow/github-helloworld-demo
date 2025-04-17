package mayudin.repos.api.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import mayudin.common.utils.domain.Resultat
import mayudin.repos.api.presentation.model.UiState
import mayudin.repos.api.presentation.viewmodel.ReposViewModel
import mayudin.repos.api.presentation.viewmodel.ReposViewModelFactory

@Composable
fun ReposScreen(
    factory: ReposViewModelFactory,
    viewModel: ReposViewModel = viewModel(factory = factory),
    onNavigation: () -> Unit = {},
    onClose: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchBar(
            searchText = searchText,
            onSearchTextChanged = { text: TextFieldValue ->
                searchText = text
                viewModel.onSearchTextChanged(text.text)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ReposContent(uiState = uiState)
    }
}

@Composable
fun SearchBar(
    searchText: TextFieldValue,
    onSearchTextChanged: (TextFieldValue) -> Unit
) {
    BasicTextField(
        value = searchText,
        onValueChange = onSearchTextChanged,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@Composable
fun ReposContent(uiState: Resultat<UiState>) {
    when (uiState) {
        is Resultat.Loading -> CircularProgressIndicator()
        is Resultat.Success -> {
            val repos = uiState.value.repos
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(repos) { repo ->
                    Text(text = repo, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        is Resultat.Failure -> {
            Text(text = "Error: ${uiState.exception.message}")
        }
    }
}

