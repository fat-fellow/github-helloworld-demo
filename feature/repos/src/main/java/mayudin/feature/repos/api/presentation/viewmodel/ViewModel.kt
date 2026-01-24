package mayudin.feature.repos.api.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mayudin.common.utils.domain.Resultat
import mayudin.feature.repos.api.domain.usecase.ReposUseCase
import mayudin.feature.repos.api.presentation.model.UiState
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class ReposViewModel @Inject constructor(
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
            flow {
                emit(Resultat.loading())
                val result = reposUseCase(query)
                emit(Resultat.success(UiState(result)))
            }.catch { throwable ->
                emit(Resultat.failure(throwable))
            }
        }
    }

    fun onSearchTextChanged(search: String) {
        queryFlow.value = search
    }
}
