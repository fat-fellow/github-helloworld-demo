package mayudin.feature.repos.domain.usecase

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mayudin.feature.repos.domain.repository.ReposRepository
import javax.inject.Inject

class ReposUseCaseImpl @Inject constructor(
    private val reposRepository: ReposRepository,
) : ReposUseCase {

    override suspend fun invoke(params: String): List<String> {
        return withContext(Dispatchers.IO) {
            reposRepository.fetchRepos(params)
        }
    }
}

@Module
@InstallIn(ViewModelComponent::class)
interface ReposUseCaseModule {

    @Binds
    @ViewModelScoped
    fun bindReposUseCase(impl: ReposUseCaseImpl): ReposUseCase
}

