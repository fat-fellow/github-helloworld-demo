package mayudin.feature.info.api.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import mayudin.common.utils.domain.Resultat
import mayudin.feature.info.api.presentation.model.UiState
import mayudin.feature.info.api.presentation.viewmodel.InfoViewModel
import mayudin.feature.info.api.presentation.viewmodel.InfoViewModelFactory
import mayudin.feature.info.api.R

@Composable
fun InfoScreen(
    factory: InfoViewModelFactory,
    viewModel: InfoViewModel = viewModel(factory = factory),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Layout(uiState)
}

@Composable
private fun Layout(uiState: Resultat<UiState>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (uiState) {
            is Resultat.Loading -> LoadingLayout()
            is Resultat.Success -> SuccessLayout(uiState.value)
            is Resultat.Failure -> ErrorLayout(Modifier.align(Alignment.Center))
        }
    }
}

@Composable
private fun LoadingLayout() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Color.Blue)
    }
}

@Composable
private fun ErrorLayout(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.error_message),
        color = Color.Red,
        modifier = modifier
    )
}

@Composable
private fun SuccessLayout(uiState: UiState) {
    Column(modifier = Modifier.fillMaxSize()) {
        InfoHeader(repo = uiState.repo, owner = uiState.owner)

        if (uiState.infos.isEmpty()) {
            EmptyMessage(Modifier.align(Alignment.CenterHorizontally))
        } else {
            InfoList(items = uiState.infos)
        }
    }
}

@Composable
private fun InfoHeader(repo: String, owner: String) {
    Column(Modifier.fillMaxWidth()) {
        Text(
            text = "Owner: $owner",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = "Repo: $repo",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = stringResource(R.string.activities),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Magenta,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
private fun InfoList(items: List<String>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            Text(
                text = item,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}

@Composable
private fun EmptyMessage(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.no_activity_message),
        style = MaterialTheme.typography.bodyMedium,
        color = Color.Gray,
        modifier = modifier
    )
}
