package mayudin.repos.api.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.squareup.anvil.annotations.ContributesBinding
import mayudin.repos.api.di.ReposScope
import mayudin.repos.api.domain.usecase.ReposUseCase
import javax.inject.Inject

@ContributesBinding(ReposScope::class)
class ReposViewModelFactory @Inject constructor(
    private val reposUseCase: ReposUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReposViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReposViewModel(reposUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}