package mayudin.feature.info.api.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class UiState(
    val owner: String,
    val repo: String,
    val infos: List<String>
)