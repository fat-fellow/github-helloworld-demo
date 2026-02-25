package mayudin.feature.repos.data.repository

import app.cash.turbine.test
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import mayudin.common.domain.DomainError
import mayudin.feature.repos.data.api.ReposApi
import mayudin.feature.repos.data.local.RepoEntity
import mayudin.feature.repos.data.local.ReposDao
import mayudin.feature.repos.data.model.Repo
import mayudin.feature.repos.domain.repository.ReposRepository
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ReposRepositoryImplTest {

    private lateinit var api: ReposApi
    private lateinit var reposDao: ReposDao
    private lateinit var repository: ReposRepository

    @Before
    fun setUp() {
        api = mockk()
        reposDao = mockk()
        repository = ReposRepositoryImpl(api, reposDao)
    }

    @Test
    fun `fetchRepos - emits cached repos immediately from DB`() = runTest {
        // Arrange
        val user = "testUser"
        val cachedEntities = listOf(
            RepoEntity(ownerId = user, name = "Repo1"),
            RepoEntity(ownerId = user, name = "Repo2"),
        )
        every { reposDao.observeRepos(user) } returns flowOf(cachedEntities)
        coEvery { api.getUserRepos(user) } returns listOf(Repo(1, "Repo1"), Repo(2, "Repo2"))
        coEvery { reposDao.replaceRepos(user, any()) } just Runs

        // Act & Assert
        repository.fetchRepos(user).test {
            assertEquals(listOf("Repo1", "Repo2"), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchRepos - stores fresh repos in DB on network success`() = runTest {
        // Arrange
        val user = "testUser"
        val freshRepos = listOf(Repo(id = 1L, name = "FreshRepo"))
        every { reposDao.observeRepos(user) } returns flowOf(emptyList())
        coEvery { api.getUserRepos(user) } returns freshRepos
        coEvery { reposDao.replaceRepos(user, any()) } just Runs

        // Act
        repository.fetchRepos(user).test {
            awaitItem() // empty list from cache
            cancelAndIgnoreRemainingEvents()
        }

        // Assert — cache was updated with the network result
        coVerify { reposDao.replaceRepos(user, listOf(RepoEntity(ownerId = user, name = "FreshRepo"))) }
    }

    @Test
    fun `fetchRepos - throws DomainError - when API call fails`() = runTest {
        // Arrange
        val user = "testUser"
        every { reposDao.observeRepos(user) } returns flowOf(emptyList())
        coEvery { api.getUserRepos(user) } throws RuntimeException("API error")

        // Act & Assert
        repository.fetchRepos(user).test {
            assertIs<DomainError>(awaitError())
        }
    }

    @Test
    fun `fetchRepos - emits empty list - when DB is empty and network returns nothing`() = runTest {
        // Arrange
        val user = "testUser"
        every { reposDao.observeRepos(user) } returns flowOf(emptyList())
        coEvery { api.getUserRepos(user) } returns emptyList()
        coEvery { reposDao.replaceRepos(user, any()) } just Runs

        // Act & Assert
        repository.fetchRepos(user).test {
            val item = awaitItem()
            assertTrue(item.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
