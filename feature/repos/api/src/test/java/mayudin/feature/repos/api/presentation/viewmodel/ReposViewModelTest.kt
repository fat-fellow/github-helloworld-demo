package mayudin.feature.repos.api.presentation.viewmodel

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import mayudin.common.utils.domain.Resultat
import mayudin.feature.repos.api.domain.usecase.ReposUseCase
import mayudin.feature.repos.api.presentation.model.UiState
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ReposViewModelTest {

    private lateinit var viewModel: ReposViewModel
    private lateinit var reposUseCase: ReposUseCase
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        reposUseCase = mock()
        viewModel = ReposViewModel(reposUseCase)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `uiState emits empty list - when query is blank`() {
        testScope.runTest {
            viewModel.uiState.test {
                assertEquals(Resultat.success(UiState(emptyList())), awaitItem())
            }
        }
    }

    @Test
    fun `uiState - emits results from use case - when query is not blank`() = testScope.runTest {
        val query = "test"
        val mockResult = Resultat.success(listOf("Repo1", "Repo2"))
        whenever(reposUseCase.stream(query)).thenReturn(flowOf(mockResult))

        viewModel.onSearchTextChanged(query)

        viewModel.uiState.test {
            skipItems(1) // Skip initial empty state
            assertEquals(Resultat.success(UiState(listOf("Repo1", "Repo2"))), awaitItem())
        }
    }

    @Test
    fun `uiState - emits loading state - when query is not blank`() = testScope.runTest {
        val query = "test"
        val result = Resultat.loading<List<String>>()
        val mockResult = flowOf(result)
        whenever(reposUseCase.stream(query)).thenReturn(mockResult)

        viewModel.onSearchTextChanged(query)

        viewModel.uiState.test {
            skipItems(1) // Skip initial empty state
            assertEquals(result, awaitItem())
        }
    }

    @Test
    fun `uiState - emits error state - when use case returns error`() = testScope.runTest {
        val query = "test"
        val mockError = Resultat.failure<List<String>>(Throwable("An error occurred"))
        whenever(reposUseCase.stream(query)).thenReturn(flowOf(mockError))

        viewModel.onSearchTextChanged(query)

        viewModel.uiState.test {
            skipItems(1) // Skip initial empty state
            assertEquals(mockError, awaitItem())
        }
    }
}
