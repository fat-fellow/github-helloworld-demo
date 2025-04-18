package mayudin.feature.info.impl.domain.repository

import mayudin.feature.info.api.domain.model.GitHubRepo

interface InfoRepository {
    suspend fun getReposActivity(user: GitHubRepo): List<String>
}