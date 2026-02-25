package mayudin.feature.repos.domain.repository

import kotlinx.coroutines.flow.Flow

interface ReposRepository {
    fun fetchRepos(user: String): Flow<List<String>>
}
