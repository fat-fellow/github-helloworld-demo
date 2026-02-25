package mayudin.feature.repos.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import mayudin.feature.repos.domain.usecase.ReposUseCase
import mayudin.feature.repos.presentation.model.RepoEffect
import mayudin.feature.repos.presentation.model.UiState

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
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
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        _uiState.value = UiState.Idle
                        return@flatMapLatest emptyFlow()
                    }
                    _uiState.value = UiState.Loading
                    reposUseCase(query)
                        .onEach { repos ->
                            _uiState.value = UiState.Success(owner = query, repos = repos)
                        }
                        .catch { throwable -> onError(throwable) }
                }
                .collect()
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

    private fun CoroutineScope.launchSafely(block: suspend () -> Unit): Job = this.launch(errorHandler) {
        block()
    }

    companion object {
        private const val DEBOUNCE = 300L
    }
}
