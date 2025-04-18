package mayudin.repos.api.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import mayudin.common.utils.domain.Resultat
import mayudin.common.utils.domain.map
import mayudin.repos.api.domain.usecase.ReposUseCase
import mayudin.repos.api.presentation.model.UiState

class ReposViewModel(
    private val reposUseCase: ReposUseCase,
) : ViewModel() {

    private val queryFlow: MutableStateFlow<String> = MutableStateFlow("")

    private val _uiState = MutableStateFlow<Resultat<UiState>>(Resultat.success(UiState(emptyList())))
    val uiState: StateFlow<Resultat<UiState>> = _uiState

    init {
        queryFlow
            .debounce(300L)
            .distinctUntilChanged()
            .flatMapLatest { query -> processQuery(query) }
            .onEach { _uiState.value = it }
            .launchIn(viewModelScope)
    }

    private fun processQuery(query: String): Flow<Resultat<UiState>> {
        return if (query.isBlank()) {
            flowOf(Resultat.success(UiState(emptyList())))
        } else {
            reposUseCase.stream(query)
                .map { it.map { UiState(it) } }
        }
    }

    fun onSearchTextChanged(search: String) {
        queryFlow.value = search
    }
}
