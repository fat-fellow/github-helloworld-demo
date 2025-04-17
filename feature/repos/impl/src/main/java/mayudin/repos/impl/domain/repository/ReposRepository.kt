package mayudin.repos.impl.domain.repository

interface ReposRepository {
    suspend fun fetchRepos(user: String): List<String>
}