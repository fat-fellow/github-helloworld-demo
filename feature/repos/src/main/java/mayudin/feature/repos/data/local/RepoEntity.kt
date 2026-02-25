package mayudin.feature.repos.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "repos", primaryKeys = ["owner_id", "name"])
data class RepoEntity(@ColumnInfo(name = "owner_id") val ownerId: String, @ColumnInfo(name = "name") val name: String)
