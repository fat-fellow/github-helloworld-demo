package mayudin.feature.info.impl.domain.usecase

import com.squareup.anvil.annotations.ContributesBinding
import mayudin.common.di.SingleIn
import mayudin.common.di.dispatchers.CoroutinesDispatchers
import mayudin.common.di.dispatchers.Dispatcher
import mayudin.common.utils.domain.ResultUseCaseImpl
import mayudin.feature.info.api.di.InfoScope
import mayudin.feature.info.api.domain.model.GitHubRepo
import mayudin.feature.info.api.domain.usecase.InfoUseCase
import mayudin.feature.info.impl.domain.repository.InfoRepository
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@SingleIn(InfoScope::class)
@ContributesBinding(
    scope = InfoScope::class,
    boundType = InfoUseCase::class
)
class InfoUseCaseImpl @Inject constructor(
    @Dispatcher(CoroutinesDispatchers.IO) context: CoroutineContext,
    private val reposRepository: InfoRepository
) : ResultUseCaseImpl<GitHubRepo, List<String>>(context), InfoUseCase {

    override suspend fun doWork(params: GitHubRepo): List<String> {
        return reposRepository.getReposActivity(params)
    }
}
