package mayudin.feature.info.presentation.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
sealed interface UiState {
    @Immutable
    data object Loading : UiState

    @Immutable
    data class Success(val owner: String, val repo: String, val infos: ImmutableList<String>) : UiState

    @Immutable
    data class Error(val message: String) : UiState
}
