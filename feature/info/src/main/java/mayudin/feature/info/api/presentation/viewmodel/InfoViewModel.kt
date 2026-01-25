package mayudin.feature.info.api.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _uiState = MutableStateFlow<Resultat<UiState>>(Resultat.loading())
    val uiState: StateFlow<Resultat<UiState>> = _uiState

    init {
        loadInfo()
    }

    private fun loadInfo() {
        val repository = GitHubRepo(owner = owner, repo = repo)

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
