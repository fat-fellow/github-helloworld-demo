package mayudin.feature.repos.domain.usecase

import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mayudin.feature.repos.domain.repository.ReposRepository

class ReposUseCase @Inject constructor(private val reposRepository: ReposRepository) {

    suspend operator fun invoke(params: String): List<String> {
        return withContext(Dispatchers.IO) {
            reposRepository.fetchRepos(params)
        }
    }
}