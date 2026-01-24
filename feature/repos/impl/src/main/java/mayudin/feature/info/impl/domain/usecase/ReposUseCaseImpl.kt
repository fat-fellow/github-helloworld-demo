package mayudin.feature.info.impl.domain.usecase

import com.squareup.anvil.annotations.ContributesBinding
import kotlinx.coroutines.withContext
import mayudin.common.di.SingleIn
import mayudin.common.di.dispatchers.CoroutinesDispatchers
import mayudin.common.di.dispatchers.Dispatcher
import mayudin.feature.info.impl.domain.repository.ReposRepository
import mayudin.feature.repos.api.di.ReposScope
import mayudin.feature.repos.api.domain.usecase.ReposUseCase
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@SingleIn(ReposScope::class)
@ContributesBinding(
    scope = ReposScope::class,
    boundType = ReposUseCase::class
)
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
