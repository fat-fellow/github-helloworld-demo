package mayudin.common.network.util

import mayudin.common.domain.utils.safeRun
import mayudin.common.network.Mappers

suspend fun <T> safeRequestRun(block: suspend () -> T): T {
    return safeRun(block, { throw Mappers.mapToDomain(it) })
}