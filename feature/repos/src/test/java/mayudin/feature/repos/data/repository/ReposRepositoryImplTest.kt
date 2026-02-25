package mayudin.feature.repos.data.repository

import app.cash.turbine.test
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import mayudin.common.domain.DomainError
import mayudin.feature.repos.data.api.ReposApi
import mayudin.feature.repos.data.local.RepoEntity
import mayudin.feature.repos.data.local.RepoSyncMetadata
import mayudin.feature.repos.data.local.ReposDao
import mayudin.feature.repos.data.model.Repo
import org.junit.Before
import org.junit.Test

class ReposRepositoryImplTest {

    private lateinit var api: ReposApi
    private lateinit var reposDao: ReposDao
    private lateinit var repository: ReposRepositoryImpl

    @Before
    fun setUp() {
        api = mockk()
        reposDao = mockk()
        repository = ReposRepositoryImpl(api, reposDao)
    }

    // ── TTL valid — serve cache, skip network ─────────────────────────────

    @Test
    fun `fetchRepos - emits cached repos and skips network when TTL is valid`() = runTest {
        val user = "testUser"
        val recentSync = System.currentTimeMillis() - 10_000L // 10 s ago, within 1 min TTL
        coEvery { reposDao.getSyncMetadata(user) } returns RepoSyncMetadata(user, recentSync)
        coEvery { reposDao.countRepos(user) } returns 2
        every { reposDao.observeRepos(user) } returns flowOf(
            listOf(RepoEntity(user, "Repo1"), RepoEntity(user, "Repo2")),
        )

        repository.fetchRepos(user).test {
            assertEquals(listOf("Repo1", "Repo2"), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 0) { api.getUserRepos(any()) }
    }

    // ── No cache, TTL expired — foreground load ───────────────────────────

    @Test
    fun `fetchRepos - loads from network and emits result when cache is empty`() = runTest {
        val user = "testUser"
        coEvery { reposDao.getSyncMetadata(user) } returns null
        coEvery { reposDao.countRepos(user) } returns 0
        coEvery { api.getUserRepos(user) } returns listOf(Repo(1, "FreshRepo"))
        coEvery { reposDao.replaceRepos(user, any()) } just Runs
        coEvery { reposDao.upsertSyncMetadata(any()) } just Runs
        every { reposDao.observeRepos(user) } returns flowOf(
            listOf(RepoEntity(user, "FreshRepo")),
        )

        repository.fetchRepos(user).test {
            assertEquals(listOf("FreshRepo"), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { reposDao.replaceRepos(user, listOf(RepoEntity(user, "FreshRepo"))) }
        coVerify { reposDao.upsertSyncMetadata(match { it.ownerId == user }) }
    }

    @Test
    fun `fetchRepos - propagates DomainError when cache is empty and network fails`() = runTest {
        val user = "testUser"
        coEvery { reposDao.getSyncMetadata(user) } returns null
        coEvery { reposDao.countRepos(user) } returns 0
        coEvery { api.getUserRepos(user) } throws RuntimeException("network down")
        every { reposDao.observeRepos(user) } returns flowOf(emptyList())

        repository.fetchRepos(user).test {
            assertTrue(awaitError() is DomainError)
        }
    }

    // ── Cache exists, TTL expired — background refresh ────────────────────

    @Test
    fun `fetchRepos - emits stale cache immediately and updates after background refresh`() = runTest {
        val user = "testUser"
        val expiredSync = System.currentTimeMillis() - 120_000L // 2 min ago, TTL expired
        coEvery { reposDao.getSyncMetadata(user) } returns RepoSyncMetadata(user, expiredSync)
        coEvery { reposDao.countRepos(user) } returns 1
        coEvery { api.getUserRepos(user) } returns listOf(Repo(2, "FreshRepo"))
        coEvery { reposDao.replaceRepos(user, any()) } just Runs
        coEvery { reposDao.upsertSyncMetadata(any()) } just Runs
        every { reposDao.observeRepos(user) } returns flowOf(
            listOf(RepoEntity(user, "StaleRepo")),
            listOf(RepoEntity(user, "FreshRepo")),
        )

        repository.fetchRepos(user).test {
            assertEquals(listOf("StaleRepo"), awaitItem())
            assertEquals(listOf("FreshRepo"), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { reposDao.replaceRepos(user, listOf(RepoEntity(user, "FreshRepo"))) }
    }

    @Test
    fun `fetchRepos - keeps serving stale cache when background refresh fails`() = runTest {
        val user = "testUser"
        val expiredSync = System.currentTimeMillis() - 120_000L
        coEvery { reposDao.getSyncMetadata(user) } returns RepoSyncMetadata(user, expiredSync)
        coEvery { reposDao.countRepos(user) } returns 1
        coEvery { api.getUserRepos(user) } throws RuntimeException("network down")
        every { reposDao.observeRepos(user) } returns flowOf(
            listOf(RepoEntity(user, "StaleRepo")),
        )

        // Flow must complete without error — background failure is swallowed
        repository.fetchRepos(user).test {
            assertEquals(listOf("StaleRepo"), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
