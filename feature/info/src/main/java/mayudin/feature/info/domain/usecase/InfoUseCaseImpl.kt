package mayudin.feature.info.domain.usecase

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mayudin.feature.info.domain.model.GitHubRepo
import mayudin.feature.info.domain.usecase.InfoUseCase
import mayudin.feature.info.domain.repository.InfoRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InfoUseCaseImpl @Inject constructor(
    private val reposRepository: InfoRepository,
) : InfoUseCase {

    override suspend fun invoke(params: GitHubRepo): List<String> {
        return withContext(Dispatchers.IO) {
            reposRepository.getReposActivity(params)
        }
    }
}

@Module
@InstallIn(ViewModelComponent::class)
interface InfoUseCaseModule {
    @Binds
    @ViewModelScoped
    fun bindInfoUseCase(impl: InfoUseCaseImpl): InfoUseCase
}

