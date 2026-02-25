package mayudin.feature.repos.domain.usecase

import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import mayudin.feature.repos.domain.repository.ReposRepository

class ReposUseCase @Inject constructor(private val reposRepository: ReposRepository) {

    operator fun invoke(params: String): Flow<List<String>> =
        reposRepository.fetchRepos(params).flowOn(Dispatchers.IO)
}
