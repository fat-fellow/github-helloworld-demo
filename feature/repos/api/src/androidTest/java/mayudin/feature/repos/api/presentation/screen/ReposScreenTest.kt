package mayudin.feature.repos.api.presentation.screen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.input.TextFieldValue
import mayudin.common.utils.domain.Resultat
import mayudin.feature.repos.api.presentation.model.UiState
import org.junit.Rule
import org.junit.Test

class ReposScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testSearchBarDisplaysHint() {
        composeTestRule.setContent {
            SearchBar(
                searchText = remember { mutableStateOf(TextFieldValue("")) }.value,
                onSearchTextChanged = {},
                hint = "Search Repos"
            )
        }

        composeTestRule.onNodeWithText("Search Repos").assertIsDisplayed()
    }

    @Test
    fun testSearchBarUpdatesText() {
        val searchText = mutableStateOf(TextFieldValue(""))

        composeTestRule.setContent {
            SearchBar(
                searchText = searchText.value,
                onSearchTextChanged = { searchText.value = it },
                hint = "Search Repos"
            )
        }

        composeTestRule.onNodeWithText("").performTextInput("New Repo")
        composeTestRule.onNodeWithText("New Repo").assertIsDisplayed()
    }

    @Test
    fun testLoadingLayoutDisplaysProgressIndicator() {
        composeTestRule.setContent {
            LoadingLayout()
        }

        composeTestRule.onNodeWithContentDescription("LoadingIndicator").assertIsDisplayed()
    }

    @Test
    fun testSuccessLayoutDisplaysRepos() {
        val repos = listOf("Repo1", "Repo2", "Repo3")

        composeTestRule.setContent {
            SuccessLayout(
                repos = repos,
                owner = "Owner",
                onNavigation = { _, _ -> }
            )
        }

        repos.forEach { repo ->
            composeTestRule.onNodeWithText(repo).assertIsDisplayed()
        }
    }

    @Test
    fun testErrorLayoutDisplaysErrorMessage() {
        composeTestRule.setContent {
            ErrorLayout()
        }

        composeTestRule.onNodeWithText("Something went wrong").assertIsDisplayed()
    }

    @Test
    fun testReposScreenDisplaysCorrectState() {
        val uiState = mutableStateOf<Resultat<UiState>>(Resultat.Loading())

        composeTestRule.setContent {
            ScreenLayout(
                uiState = uiState.value,
                searchText = TextFieldValue(""),
                onSearchTextChanged = {},
                onNavigation = { _, _ -> }
            )
        }

        // Assert Loading state
        uiState.value = Resultat.Loading()
        composeTestRule.onNodeWithContentDescription("LoadingIndicator").assertIsDisplayed()

        // Assert Success state
        uiState.value = Resultat.Success(UiState(repos = listOf("Repo1", "Repo2")))
        composeTestRule.onNodeWithText("Repo1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Repo2").assertIsDisplayed()

        // Assert Failure state
        uiState.value = Resultat.Failure(Exception("Error"))
        composeTestRule.onNodeWithText("Something went wrong").assertIsDisplayed()
    }
}
