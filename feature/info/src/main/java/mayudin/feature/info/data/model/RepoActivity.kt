package mayudin.feature.info.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepoActivity(val id: Long, @SerialName("activity_type") val activityType: String, val actor: Actor)
