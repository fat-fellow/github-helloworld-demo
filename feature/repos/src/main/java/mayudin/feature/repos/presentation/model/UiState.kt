package mayudin.feature.repos.presentation.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
sealed interface UiState {
    @Immutable
    data object Idle : UiState

    @Immutable
    data object Loading : UiState

    @Immutable
    data class Success(val owner: String, val repos: ImmutableList<String>) : UiState

    @Immutable
    data class Error(val message: String) : UiState
}
