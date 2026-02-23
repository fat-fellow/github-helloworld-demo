package mayudin.feature.repos.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import mayudin.common.domain.utils.safeRun
import mayudin.feature.repos.domain.usecase.ReposUseCase
import mayudin.feature.repos.presentation.model.RepoEffect
import mayudin.feature.repos.presentation.model.UiState

@OptIn(FlowPreview::class)
@HiltViewModel
class ReposViewModel @Inject constructor(private val reposUseCase: ReposUseCase) : ViewModel() {

    private val queryFlow = MutableStateFlow("")

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _viewEffects = MutableSharedFlow<RepoEffect>()
    val viewEffects: SharedFlow<RepoEffect> = _viewEffects.asSharedFlow()

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
                .debounce(DEBOUNCE)
                .distinctUntilChanged()
                .collect { query ->
                    loadRepos(query)
                }
        }
    }

    fun onSearchTextChanged(search: String) {
        queryFlow.value = search
    }

    fun onRepoClicked(owner: String, repo: String) {
        viewModelScope.launch {
            _viewEffects.emit(RepoEffect.NavigateToInfo(owner, repo))
        }
    }

    private suspend fun loadRepos(query: String) {
        if (query.isBlank()) {
            _uiState.value = UiState.Idle
            return
        }

        _uiState.value = UiState.Loading

        safeRun(
            {
                val result = reposUseCase(query)

                _uiState.value = UiState.Success(
                    owner = query,
                    repos = result,
                )
            },
            ::onError,
        )
    }

    private fun CoroutineScope.launchSafely(block: suspend () -> Unit): Job = this.launch(errorHandler) {
        block()
    }

    companion object {
        private const val DEBOUNCE = 300L
    }
}
