package mayudin.common.utils.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

interface ResultUseCase<in P, R> {
    fun asFlow(params: P): Flow<R>
    fun stream(params: P): Flow<Resultat<R>>
    suspend fun run(params: P): R
    suspend fun execute(params: P): Resultat<R>
    suspend fun async(params: P): Resultat<R>
}

abstract class ResultUseCaseImpl<in P, R>(
    private val context: CoroutineContext
) : ResultUseCase<P, R> {

    @Throws(Exception::class)
    override fun asFlow(params: P): Flow<R> = flow { emit(doWork(params)) }.flowOn(context)

    override fun stream(params: P): Flow<Resultat<R>> {
        return flow {
            emit(Resultat.Loading())
            val r = doWork(params)
            emit(Resultat.Success(r))
        }.catch { t ->
            emit(Resultat.Failure(t))
        }.flowOn(context)
    }

    override suspend fun run(params: P) = doWork(params)

    override suspend fun execute(params: P): Resultat<R> = runCatchingL { doWork(params) }

    override suspend fun async(params: P): Resultat<R> = withContext(context) {
        runCatchingL { doWork(params) }
    }

    protected abstract suspend fun doWork(params: P): R
}