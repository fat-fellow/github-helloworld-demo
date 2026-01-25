package mayudin.feature.repos.api.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import mayudin.feature.repos.api.domain.usecase.ReposUseCase
import mayudin.feature.repos.api.presentation.model.UiState
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class ReposViewModel @Inject constructor(
    private val reposUseCase: ReposUseCase,
) : ViewModel() {

    private val queryFlow = MutableStateFlow("")

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        onError(throwable)
    }

    private fun onError(throwable: Throwable) {
        _uiState.value = UiState.Error(
            message = throwable.message ?: "Unknown error occurred",
        )
    }

    init {
        viewModelScope.launchSafely {
            queryFlow
                .debounce(300L)
                .distinctUntilChanged()
                .collect { query ->
                    loadRepos(query)
                }
        }
    }

    fun onSearchTextChanged(search: String) {
        queryFlow.value = search
    }

    private suspend fun loadRepos(query: String) {
        if (query.isBlank()) {
            _uiState.value = UiState.Idle
            return
        }

        _uiState.value = UiState.Loading

        try {
            val result = reposUseCase(query)

            _uiState.value = UiState.Success(
                owner = query,
                repos = result,
            )
        } catch (e: Throwable) {
            onError(e)
        }
    }

    private fun CoroutineScope.launchSafely(block: suspend () -> Unit): Job =
        this.launch(errorHandler) {
            block()
        }
}
