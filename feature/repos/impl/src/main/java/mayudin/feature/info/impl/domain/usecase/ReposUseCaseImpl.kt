package mayudin.feature.info.impl.domain.usecase

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.withContext
import mayudin.common.di.dispatchers.CoroutinesDispatchers
import mayudin.common.di.dispatchers.Dispatcher
import mayudin.feature.info.impl.domain.repository.ReposRepository
import mayudin.feature.repos.api.domain.usecase.ReposUseCase
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class ReposUseCaseImpl @Inject constructor(
    @Dispatcher(CoroutinesDispatchers.IO) private val context: CoroutineContext,
    private val reposRepository: ReposRepository
) : ReposUseCase {

    override suspend fun invoke(params: String): List<String> {
        return withContext(context) {
            reposRepository.fetchRepos(params)
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface ReposUseCaseModule {
    @Binds
    @Singleton
    fun bindReposUseCase(impl: ReposUseCaseImpl): ReposUseCase
}

