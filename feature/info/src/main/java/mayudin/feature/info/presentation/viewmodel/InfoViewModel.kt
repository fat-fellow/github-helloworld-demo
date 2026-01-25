package mayudin.feature.info.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mayudin.feature.info.domain.model.GitHubRepo
import mayudin.feature.info.domain.usecase.InfoUseCase
import mayudin.feature.info.presentation.model.UiState

@HiltViewModel(assistedFactory = InfoViewModel.Factory::class)
class InfoViewModel @AssistedInject constructor(
    @Assisted("owner") private val owner: String,
    @Assisted("repo") private val repo: String,
    private val infoUseCase: InfoUseCase
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("owner") owner: String,
            @Assisted("repo") repo: String
        ): InfoViewModel
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.value = UiState.Error(
            message = throwable.message ?: "Unknown error occurred"
        )
    }

    init {
        loadInfo()
    }

    private fun loadInfo() {
        viewModelScope.launchSafely {
            _uiState.value = UiState.Loading

            val repository = GitHubRepo(owner = owner, repo = repo)
            val result = infoUseCase(repository)

            _uiState.value = UiState.Success(
                owner = owner,
                repo = repo,
                infos = result
            )
        }
    }

    private fun CoroutineScope.launchSafely(block: suspend () -> Unit): Job =
        this.launch(errorHandler) {
            block()
        }
}
