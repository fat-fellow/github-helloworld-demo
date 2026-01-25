package mayudin.feature.info.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
sealed interface UiState {
    @Immutable
    data object Loading : UiState

    @Immutable
    data class Success(
        val owner: String,
        val repo: String,
        val infos: List<String>
    ) : UiState

    @Immutable
    data class Error(
        val message: String
    ) : UiState
}
