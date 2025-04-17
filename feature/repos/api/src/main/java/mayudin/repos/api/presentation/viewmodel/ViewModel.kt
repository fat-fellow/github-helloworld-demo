package mayudin.repos.api.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import mayudin.common.utils.domain.Resultat
import mayudin.common.utils.domain.map
import mayudin.repos.api.di.ReposScope
import mayudin.repos.api.domain.usecase.ReposUseCase
import mayudin.repos.api.presentation.model.UiState
import javax.inject.Inject

class ReposViewModel(
    private val reposUseCase: ReposUseCase,
) : ViewModel() {

    private val queryFlow: MutableStateFlow<String> = MutableStateFlow("fat-fellow")

    val uiState: StateFlow<Resultat<UiState>> = queryFlow
        .debounce(300L)
        .distinctUntilChanged()
        .flatMapLatest { query -> processQuery(query) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = Resultat.loading()
        )

    private fun processQuery(query: String) = reposUseCase.stream(query)
        .map {
            it.map { UiState(it) }
        }

    fun onSearchTextChanged(search: String) {
        queryFlow.value = search
    }
}
