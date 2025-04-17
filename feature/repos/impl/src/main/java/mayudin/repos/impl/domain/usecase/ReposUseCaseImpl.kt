package mayudin.repos.impl.domain.usecase

import com.squareup.anvil.annotations.ContributesBinding
import kotlinx.coroutines.Dispatchers.IO
import mayudin.common.di.SingleIn
import mayudin.common.di.dispatchers.CoroutinesDispatchers
import mayudin.common.di.dispatchers.Dispatcher
import mayudin.common.utils.domain.ResultUseCaseImpl
import mayudin.repos.api.domain.usecase.ReposUseCase
import mayudin.repos.api.di.ReposScope
import mayudin.repos.impl.domain.repository.ReposRepository
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@SingleIn(ReposScope::class)
@ContributesBinding(
    scope = ReposScope::class,
    boundType = ReposUseCase::class
)
class ReposUseCaseImpl @Inject constructor(
    @Dispatcher(CoroutinesDispatchers.IO) context: CoroutineContext,
    private val reposRepository: ReposRepository
) : ResultUseCaseImpl<String, List<String>>(context), ReposUseCase {

    override suspend fun doWork(params: String): List<String> {
        return reposRepository.fetchRepos(params)
    }
}
