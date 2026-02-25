package mayudin.feature.repos.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repo_sync_metadata")
data class RepoSyncMetadata(
    @PrimaryKey @ColumnInfo(name = "owner_id") val ownerId: String,
    @ColumnInfo(name = "last_synced_at") val lastSyncedAt: Long,
)
