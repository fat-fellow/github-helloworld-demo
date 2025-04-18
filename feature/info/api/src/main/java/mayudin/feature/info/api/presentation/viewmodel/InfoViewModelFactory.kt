package mayudin.feature.info.api.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.squareup.anvil.annotations.ContributesBinding
import mayudin.feature.info.api.di.InfoScope
import mayudin.feature.info.api.domain.model.GitHubRepo
import mayudin.feature.info.api.domain.usecase.InfoUseCase
import javax.inject.Inject

@ContributesBinding(InfoScope::class)
class InfoViewModelFactory @Inject constructor(
    private val infoUseCase: InfoUseCase,
    private val repository: GitHubRepo,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InfoViewModel(repository, infoUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}