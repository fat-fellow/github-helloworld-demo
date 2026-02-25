package mayudin.feature.repos.domain.usecase

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import mayudin.feature.repos.domain.repository.ReposRepository

class ReposUseCase @Inject constructor(private val reposRepository: ReposRepository) {
    operator fun invoke(user: String): Flow<List<String>> = reposRepository.fetchRepos(user)
}
