package mayudin.feature.info.domain.usecase

import mayudin.feature.info.domain.model.GitHubRepo

interface InfoUseCase {
    suspend operator fun invoke(params: GitHubRepo): List<String>
}
