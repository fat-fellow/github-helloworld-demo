package mayudin.feature.info.api.domain.usecase

import mayudin.feature.info.api.domain.model.GitHubRepo

interface InfoUseCase {
    suspend operator fun invoke(params: GitHubRepo): List<String>
}
