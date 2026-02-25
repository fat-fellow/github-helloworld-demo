package mayudin.feature.repos.data.local
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
@Dao
abstract class ReposDao {
    @Query("SELECT * FROM repos WHERE owner_id = :ownerId ORDER BY name ASC")
    abstract fun observeRepos(ownerId: String): Flow<List<RepoEntity>>

    @Query("SELECT COUNT(*) FROM repos WHERE owner_id = :ownerId")
    abstract suspend fun countRepos(ownerId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertRepos(repos: List<RepoEntity>)

    @Query("DELETE FROM repos WHERE owner_id = :ownerId")
    abstract suspend fun deleteReposForOwner(ownerId: String)

    @Transaction
    open suspend fun replaceRepos(ownerId: String, repos: List<RepoEntity>) {
        deleteReposForOwner(ownerId)
        insertRepos(repos)
    }

    @Query("SELECT * FROM repo_sync_metadata WHERE owner_id = :ownerId")
    abstract suspend fun getSyncMetadata(ownerId: String): RepoSyncMetadata?

    @Upsert
    abstract suspend fun upsertSyncMetadata(metadata: RepoSyncMetadata)
}
