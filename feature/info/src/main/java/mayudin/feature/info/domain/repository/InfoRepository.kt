package mayudin.feature.info.domain.repository

import mayudin.feature.info.domain.model.GitHubRepo

interface InfoRepository {
    suspend fun getReposActivity(repo: GitHubRepo): List<String>
}
