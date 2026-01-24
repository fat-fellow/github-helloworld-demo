package mayudin.feature.info.impl.domain.usecase

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.withContext
import mayudin.common.di.dispatchers.CoroutinesDispatchers
import mayudin.common.di.dispatchers.Dispatcher
import mayudin.feature.info.api.domain.model.GitHubRepo
import mayudin.feature.info.api.domain.usecase.InfoUseCase
import mayudin.feature.info.impl.domain.repository.InfoRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class InfoUseCaseImpl @Inject constructor(
    @Dispatcher(CoroutinesDispatchers.IO) private val context: CoroutineContext,
    private val reposRepository: InfoRepository
) : InfoUseCase {

    override suspend fun invoke(params: GitHubRepo): List<String> {
        return withContext(context) {
            reposRepository.getReposActivity(params)
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface InfoUseCaseModule {
    @Binds
    @Singleton
    fun bindInfoUseCase(impl: InfoUseCaseImpl): InfoUseCase
}

