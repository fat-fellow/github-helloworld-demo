package mayudin.feature.info.impl.data.repository

import kotlinx.coroutines.runBlocking
import mayudin.common.domain.DomainError
import mayudin.feature.info.impl.data.model.Repo
import mayudin.feature.info.impl.data.remote.ReposApi
import mayudin.feature.info.impl.domain.repository.ReposRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.mock

class ReposRepositoryImplTest {

    private lateinit var api: ReposApi
    private lateinit var repository: ReposRepository

    @Before
    fun setUp() {
        api = mock()
        repository = ReposRepositoryImpl(api)
    }

    @Test
    fun `fetchRepos - returns list of repo names - when API call is successful`() {
        runBlocking {
            // Arrange
            val user = "testUser"
            val mockResponse = listOf(
                Repo(id = 1, name = "Repo1"),
                Repo(id = 2, name = "Repo2")
            )
            `when`(api.getUserRepos(user)).thenReturn(mockResponse)

            // Act
            val result = repository.fetchRepos(user)

            // Assert
            assertEquals(listOf("Repo1", "Repo2"), result)
            verify(api).getUserRepos(user)
        }
    }

    @Test
    fun `fetchRepos - throws mapped exception - when API call fails`() {
        runBlocking {
            // Arrange
            val user = "testUser"
            val exception = RuntimeException("API error")
            `when`(api.getUserRepos(user)).thenThrow(exception)

            // Act
            val thrown = assertThrows(DomainError::class.java) {
                runBlocking { repository.fetchRepos(user) }
            }

            // Assert
            assertEquals("java.lang.RuntimeException: API error", thrown.message)
            verify(api).getUserRepos(user)
        }
    }
}
