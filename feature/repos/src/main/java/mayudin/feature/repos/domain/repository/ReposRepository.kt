package mayudin.feature.repos.domain.repository

interface ReposRepository {
    suspend fun fetchRepos(user: String): List<String>
}
