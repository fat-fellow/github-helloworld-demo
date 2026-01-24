package mayudin.feature.info.api.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mayudin.common.utils.domain.Resultat
import mayudin.feature.info.api.domain.model.GitHubRepo
import mayudin.feature.info.api.domain.usecase.InfoUseCase
import mayudin.feature.info.api.presentation.model.UiState

class InfoViewModel(
    repository: GitHubRepo,
    infoUseCase: InfoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<Resultat<UiState>>(Resultat.loading())
    val uiState: StateFlow<Resultat<UiState>> = _uiState

    init {
        flow {
            emit(Resultat.loading())
            val result = infoUseCase(repository)
            emit(Resultat.success(UiState(infos = result, owner = repository.owner, repo = repository.repo)))
        }.catch { throwable ->
            emit(Resultat.failure(throwable))
        }
            .onEach { _uiState.value = it }
            .launchIn(viewModelScope)
    }
}

