package mayudin.feature.info.impl.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Activity(
    val id: Long,
    @SerialName("activity_type") val activityType: String,
    val actor: Actor
)