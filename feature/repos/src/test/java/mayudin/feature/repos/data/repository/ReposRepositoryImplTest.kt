package mayudin.feature.repos.data.repository

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import mayudin.common.domain.DomainError
import mayudin.feature.repos.data.api.ReposApi
import mayudin.feature.repos.data.model.Repo
import mayudin.feature.repos.domain.repository.ReposRepository
import org.junit.Before
import org.junit.Test

class ReposRepositoryImplTest {

    private lateinit var api: ReposApi
    private lateinit var repository: ReposRepository

    @Before
    fun setUp() {
        api = mockk()
        repository = ReposRepositoryImpl(api)
    }

    @Test
    fun `fetchRepos - returns list of repo names - when API call is successful`() = runTest {
        // Arrange
        val user = "testUser"
        val mockResponse = listOf(
            Repo(id = 1, name = "Repo1"),
            Repo(id = 2, name = "Repo2"),
        )
        coEvery { api.getUserRepos(user) } returns mockResponse

        // Act
        val result = repository.fetchRepos(user)

        // Assert
        assertEquals(listOf("Repo1", "Repo2"), result)
        coVerify { api.getUserRepos(user) }
    }

    @Test
    fun `fetchRepos - throws mapped exception - when API call fails`() = runTest {
        // Arrange
        val user = "testUser"
        val exception = RuntimeException("API error")
        coEvery { api.getUserRepos(user) } throws exception

        // Act
        assertFailsWith<DomainError> {
            runBlocking { repository.fetchRepos(user) }
        }

        // Assert
        coVerify { api.getUserRepos(user) }
    }
}
