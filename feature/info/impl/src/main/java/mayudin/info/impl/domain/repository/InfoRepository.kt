package mayudin.info.impl.domain.repository

import mayudin.info.api.domain.model.GitHubRepository

interface InfoRepository {
    suspend fun getReposActivity(user: GitHubRepository): List<String>
}