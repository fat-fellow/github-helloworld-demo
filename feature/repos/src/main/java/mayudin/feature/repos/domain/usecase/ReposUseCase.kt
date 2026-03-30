package mayudin.feature.repos.domain.usecase

import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import mayudin.feature.repos.domain.repository.ReposRepository

class ReposUseCase @Inject constructor(private val reposRepository: ReposRepository) {
    operator fun invoke(user: String): Flow<ImmutableList<String>> = reposRepository.fetchRepos(user)
        .flowOn(Dispatchers.IO)
        .map { it.toImmutableList() }
        .flowOn(Dispatchers.Default)
}
