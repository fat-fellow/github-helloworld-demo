package mayudin.feature.repos.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import mayudin.common.domain.utils.tryCatching
import mayudin.feature.repos.data.api.ReposApi
import mayudin.feature.repos.data.local.RepoEntity
import mayudin.feature.repos.data.local.RepoSyncMetadata
import mayudin.feature.repos.data.local.ReposDao
import mayudin.feature.repos.domain.repository.ReposRepository

@Singleton
class ReposRepositoryImpl @Inject constructor(private val api: ReposApi, private val reposDao: ReposDao) :
    ReposRepository {

    override fun fetchRepos(user: String): Flow<List<String>> = reposDao.observeRepos(user)
        .map { list -> list.map { it.name } }
        .onStart {
                /* For the sake of simplicity I use a simple time-based cache invalidation strategy here.
                   In a real app, you might want to consider more sophisticated approaches of updating
                   and merging data */
            val lastSync = reposDao.getSyncMetadata(user)?.lastSyncedAt
            val ttlExpired = lastSync == null ||
                System.currentTimeMillis() - lastSync > TTL_MS

            if (ttlExpired) {
                refresh(user)
            }
        }

    private suspend fun refresh(user: String) {
        suspend {
            val fresh = api.getUserRepos(user)
            reposDao.replaceRepos(user, fresh.map { RepoEntity(ownerId = user, name = it.name) })
            reposDao.upsertSyncMetadata(RepoSyncMetadata(ownerId = user, lastSyncedAt = System.currentTimeMillis()))
        }.tryCatching {
            /* In real life you would want to make UI aware that the date is stale and refresh failed,
               instead of just silently ignoring the error */
        }
    }

    private companion object {
        const val TTL_MS = 60_000L
    }
}
