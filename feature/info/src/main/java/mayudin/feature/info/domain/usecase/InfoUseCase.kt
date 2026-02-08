package mayudin.feature.info.domain.usecase

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mayudin.feature.info.domain.model.GitHubRepo
import mayudin.feature.info.domain.repository.InfoRepository

@Singleton
class InfoUseCase @Inject constructor(private val reposRepository: InfoRepository) {

    suspend operator fun invoke(params: GitHubRepo): List<String> {
        return withContext(Dispatchers.IO) {
            reposRepository.getReposActivity(params)
        }
    }
}