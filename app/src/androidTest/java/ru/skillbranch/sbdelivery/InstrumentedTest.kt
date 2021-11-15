package ru.skillbranch.sbdelivery

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.skillbranch.sbdelivery.domain.CategoryItem
import ru.skillbranch.sbdelivery.domain.DishItem
import ru.skillbranch.sbdelivery.domain.User
import ru.skillbranch.sbdelivery.screens.components.NavigationDrawer
import ru.skillbranch.sbdelivery.screens.components.items.ProductItem
import ru.skillbranch.sbdelivery.screens.components.menuItems
import ru.skillbranch.sbdelivery.screens.dishes.data.DishesUiState
import ru.skillbranch.sbdelivery.screens.home.logic.HomeFeature
import ru.skillbranch.sbdelivery.screens.home.ui.HomeScreen
import ru.skillbranch.sbdelivery.screens.home.ui.SectionList
import ru.skillbranch.sbdelivery.screens.menu.logic.MenuFeature
import ru.skillbranch.sbdelivery.screens.menu.ui.MenuScreen
import ru.skillbranch.sbdelivery.screens.root.ui.AppTheme

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun module1() {

        val actionsRecord = mutableListOf<String>()
        val expectedRecords = mutableListOf(
            "onLogout",
            "onSelect home",
            "onSelect menu",
            "onSelect favorites",
            "onSelect cart",
            "onSelect profile",
            "onSelect order",
            "onSelect notifications",
            "onSelect about"
        )
        val expectedUser = User("Иван Иванов", "ivan.ivanov@gmail.com")
        val user: MutableState<User?> = mutableStateOf(expectedUser)
        val cart = mutableStateOf(10)
        val notification = mutableStateOf(7)

        composeTestRule.setContent {

            AppTheme {
                NavigationDrawer(
                    currentRoute = "home",
                    user = user.value,
                    cartCount = cart.value,
                    notificationCount = notification.value,
                    onLogout = { actionsRecord.add("onLogout") },
                    onSelect = { actionsRecord.add("onSelect $it") },
                )
            }
        }

        composeTestRule.onNodeWithText(expectedUser.name)
            .assertWithMessage("Узел с текстом \"${expectedUser.name}\" должен отображаться") { assertIsDisplayed() }
        composeTestRule.onNodeWithText(expectedUser.email)
            .assertWithMessage("Узел с текстом \"${expectedUser.email}\" должен отображаться") { assertIsDisplayed() }

        composeTestRule.onNodeWithContentDescription("Logout")
            .assertWithMessage("Узел с ContentDescription \"Logout\" должен отображаться") { assertIsDisplayed() }
            .assertWithMessage("Узел с ContentDescription \"Logout\" должен быть кликабельным") { assertHasClickAction() }
            .performClick()

        menuItems.forEach { menu ->
            composeTestRule.onNodeWithText(menu.title)
                .assertWithMessage("Узел с текстом \"${menu.title}\" должен отображаться") { assertIsDisplayed() }
                .assertWithMessage("Узел с текстом \"${menu.title}\" должен быть кликабельным") { assertHasClickAction() }
                .performClick()
        }

        composeTestRule.onNodeWithText("О приложении")
            .assertWithMessage("Узел с текстом \"О приложении\" должен отображаться") { assertIsDisplayed() }
            .assertWithMessage("Узел с текстом \"О приложении\" должен быть кликабельным") { assertHasClickAction() }
            .performClick()

        composeTestRule.onNode(hasText("Корзина") and hasText("+10"))
            .assertWithMessage("Узел с текстом \"Корзина +10\" должен отображаться") { assertIsDisplayed() }

        composeTestRule.onNode(hasText("Уведомления") and hasText("+7"))
            .assertWithMessage("Узел с текстом \"Уведомления +7\" должен отображаться") { assertIsDisplayed() }

        composeTestRule.runOnIdle {
            user.value = null
            cart.value = 0
            notification.value = 0
        }


        composeTestRule.onNode(hasText("Корзина") and hasText("+10"))
            .assertDoesNotExist()

        composeTestRule.onNode(hasText("Уведомления") and hasText("+7"))
            .assertDoesNotExist()

        composeTestRule.onNodeWithText(expectedUser.name)
            .assertDoesNotExist()
        composeTestRule.onNodeWithText(expectedUser.email)
            .assertDoesNotExist()

        composeTestRule.onNodeWithContentDescription("Logout")
            .assertDoesNotExist()

        assertEquals(expectedRecords, actionsRecord)
    }


    @Test
    fun module2() {
        val expectedDish = DishItem(
            id = "0",
            image = "",
            price = "100",
            title = "test title",
            isSale = true,
            isFavorite = false
        )
        val actionsRecord = mutableListOf<String>()
        val expectedRecords = mutableListOf(
            "onClick ${expectedDish.id}",
            "onAddToCart ${expectedDish.id}",
            "onToggleLike ${expectedDish.id}",
        )

        val dish = mutableStateOf(expectedDish)

        composeTestRule.setContent {

            AppTheme {
                ProductItem(
                    dish = dish.value,
                    modifier = Modifier.width(160.dp),
                    onToggleLike = { actionsRecord.add("onToggleLike ${it.id}") },
                    onAddToCart = { actionsRecord.add("onAddToCart ${it.id}") },
                    onClick = { actionsRecord.add("onClick ${it.id}") })
            }
        }
        composeTestRule.onNode(hasParent(isRoot()))
            .assertWithMessage("Карточка товара должна быть кликабельна") { assertHasClickAction() }
            .performClick()
        composeTestRule.onNodeWithText("test title")
            .assertWithMessage("Узел с текстом \"Тест секции\" должен отображаться") { assertIsDisplayed() }

        composeTestRule.onNodeWithText("АКЦИЯ")
            .assertWithMessage("Узел с текстом \"АКЦИЯ\" должен отображаться") { assertIsDisplayed() }
        composeTestRule.onNodeWithText("${expectedDish.price} руб")
            .assertWithMessage("Узел с текстом \"${expectedDish.price} руб\" должен отображаться") { assertIsDisplayed() }
        composeTestRule.onNode(hasContentDescription("Add to cart"))
            .assertWithMessage("Узел с ContentDescription \"Add to cart\" должен быть кликабельным") {  assertHasClickAction() }
            .performClick()
        composeTestRule.onNode(hasContentDescription("Toggle favorite"))
            .assertWithMessage("Узел с ContentDescription \"Toggle favorite\" должен быть кликабельным") {  assertHasClickAction() }
            .performClick()

        composeTestRule.runOnIdle {
            dish.value = dish.value.copy(isSale = false, title = "test title 2", price = "200")
        }

        composeTestRule.onNodeWithText("АКЦИЯ").assertDoesNotExist()
        composeTestRule.onNodeWithText("test title").assertDoesNotExist()
        composeTestRule.onNodeWithText("test title 2")
            .assertWithMessage("Узел с текстом \"test title 2\" должен отображаться") { assertIsDisplayed() }
        composeTestRule.onNodeWithText("${expectedDish.price} руб").assertDoesNotExist()
        composeTestRule.onNodeWithText("200 руб")
            .assertWithMessage("Узел с текстом \"200 руб\" должен отображаться") { assertIsDisplayed() }
        assertEquals(expectedRecords, actionsRecord)
    }

    @Test
    fun module3() {

        val expectedDish = DishItem(
            id = "0",
            image = "",
            price = "100",
            title = "dish 0",
            isSale = true,
            isFavorite = false
        )

        val actionsRecord = mutableListOf<String>()
        val expectedRecords = mutableListOf(
            "onClick ${expectedDish.id}",
            "onToggleLike ${expectedDish.id}",
            "onAddToCart ${expectedDish.id}",
        )

        val expectedDishes = Array(10) {
            expectedDish.copy(id = "$it", title = "dish $it", price = "${it * 100}")
        }.toList()

        composeTestRule.setContent {

            AppTheme {
                SectionList(
                    dishes = expectedDishes,
                    title = "Тест секции",
                    onToggleLike = { actionsRecord.add("onToggleLike ${it.id}") },
                    onAddToCart = { actionsRecord.add("onAddToCart ${it.id}") },
                    onClick = { actionsRecord.add("onClick ${it.id}") },
                )
            }
        }

        composeTestRule.onNodeWithText("Тест секции")
            .assertWithMessage("Узел с текстом Тест секции должен отображаться") { assertIsDisplayed() }


        composeTestRule.onNodeWithText("См. все")
            .assertWithMessage("Узел с текстом См. все должен быть кликабельным") { assertHasClickAction() }
            .assertWithMessage("Узел с текстом См. все должен отображаться") { assertIsDisplayed() }

        composeTestRule.onNodeWithContentDescription("dish 0")
            .performClick()

        composeTestRule.onNode(
            hasContentDescription("Toggle favorite") and hasParent(
                hasContentDescription("dish 0")
            )
        )
            .performClick()

        composeTestRule.onNode(
            hasContentDescription("Add to cart") and hasParent(
                hasContentDescription("dish 0")
            )
        )
            .performClick()


        composeTestRule.onAllNodes(hasParent(hasScrollAction()))
            .assertCountEquals(3)

        composeTestRule.onNodeWithText("См. все")
            .performClick()

        composeTestRule.onNodeWithText("См. все")
            .assertDoesNotExist()

        composeTestRule.onNodeWithText("Свернуть")
            .assertWithMessage("Узел с текстом Свернуть должен отображаться") { assertIsDisplayed() }

        composeTestRule.onAllNodes(hasContentDescription("dish", true))
            .assertCountEquals(expectedDishes.size)

        assertEquals(expectedRecords, actionsRecord)
    }

    @ExperimentalComposeUiApi
    @ExperimentalFoundationApi
    @Test
    fun module4() {

        val expectedDish = DishItem(
            id = "0",
            image = "",
            price = "100",
            title = "dish 0",
            isSale = true,
            isFavorite = false
        )

        val expectedDishes = Array(10) {
            expectedDish.copy(id = "$it", title = "dish $it", price = "${it * 100}")
        }.toList()

        val recommended = expectedDishes.map { it.copy(title = "recommended dish ${it.id}") }
        val best = expectedDishes.mapIndexed { index, dish ->
            dish.copy(
                id = "${index + 11}",
                title = "best dish ${index + 11}"
            )
        }
        val popular = expectedDishes.mapIndexed { index, dish ->
            dish.copy(
                id = "${index + 21}",
                title = "popular dish ${index + 21}"
            )
        }

        val expectedState = HomeFeature.State(
            recommended = DishesUiState.Value(recommended),
            best = DishesUiState.Value(best),
            popular = DishesUiState.Value(popular)
        )
        val state = mutableStateOf(expectedState)
        composeTestRule.setContent {

            AppTheme {
                HomeScreen(
                    state = state.value,
                    accept = { }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Wallpaper")
            .assertWithMessage("Узел с ContentDescription \"Wallpaper\" должен отображаться") { assertIsDisplayed() }

        composeTestRule.onNodeWithText("Рекомендуем")
            .assertWithMessage("Узел с текстом \"Рекомендуем\" должен отображаться") { assertIsDisplayed() }

        composeTestRule.onNodeWithText("Лучшее")
            .assertWithMessage("Узел с текстом \"Лучшее\" должен существовать") { assertExists() }

        composeTestRule.onNodeWithText("Популярное")
            .assertWithMessage("Узел с текстом \"Популярное\" должен существовать") { assertExists() }

        composeTestRule.onRoot()
            .performGesture { swipeUp() }
            .performGesture { swipeUp() }

        composeTestRule.onNodeWithText("Лучшее")
            .assertWithMessage("Узел с текстом \"Лучшее\" после вертикального свайпа должен отображаться") { assertIsDisplayed() }

        composeTestRule.onNodeWithText("Популярное")
            .assertWithMessage("Узел с текстом \"Популярное\" после вертикального свайпа должен отображаться") { assertIsDisplayed() }


        composeTestRule.onAllNodes(hasText("См. все"))
            .onLast()
            .performClick()

        composeTestRule.onRoot().performGesture { swipeDown() }

        composeTestRule.onAllNodes(hasText("См. все"))
            .get(1)
            .performClick()

        composeTestRule.onAllNodes(hasText("См. все"))
            .onFirst()
            .performClick()

        composeTestRule.onAllNodes(hasContentDescription("dish", true))
            .assertAll(hasClickAction())
            .assertCountEquals(30)

        composeTestRule.runOnIdle {
            state.value =
                state.value.copy(best = DishesUiState.Loading, popular = DishesUiState.Empty)
        }


        composeTestRule.onNodeWithText("Лучшее")
            .assertWithMessage("Узел с текстом \"Лучшее\" во время загрузки должен существовать") { assertExists() }


        composeTestRule.onAllNodes(hasText("См. все"))
            .onFirst()
            .assertWithMessage("Узел с текстом \"См. все\" во время загрузки не кликабельный") { assertHasNoClickAction() }


        composeTestRule.onNodeWithText("Популярное")
            .assertDoesNotExist()


    }

    @ExperimentalComposeUiApi
    @ExperimentalFoundationApi
    @Test
    fun module5() {
        val actionsRecord = mutableListOf<String>()
        val expectedRecords = mutableListOf(
            "Menu(msg=ClickCategory(id=8, title=category 8))"
        )

        val expectedCategory =
            CategoryItem("0", "category 0", order = 0, parentId = null, icon = "")
        val expectedCategories = Array(9) {
            if (it < 6) expectedCategory.copy(id = "$it", title = "category $it", order = it)
            else expectedCategory.copy(
                id = "$it",
                title = "category $it",
                order = it,
                parentId = "0"
            )
        }.toList()

        val expectedState = MenuFeature.State(
            categories = expectedCategories
        )
        val state = mutableStateOf(expectedState)
        composeTestRule.setContent {

            AppTheme {
                MenuScreen(
                    state = state.value,
                    accept = { actionsRecord.add(it.toString()) }
                )
            }
        }

        composeTestRule.onNode(hasScrollAction())

        composeTestRule.onAllNodes(hasText("category", true))
            .assertAll(hasClickAction())
            .assertCountEquals(6)

        composeTestRule.runOnIdle { state.value = state.value.copy(parentId = "0") }

        composeTestRule.onAllNodes(hasText("category", true))
            .assertAll(hasClickAction())
            .assertCountEquals(3)
            .onLast()
            .performClick()

        assertEquals(expectedRecords, actionsRecord)
    }

}

fun SemanticsNodeInteraction.assertWithMessage(
    message: String,
    block: SemanticsNodeInteraction.() -> SemanticsNodeInteraction
): SemanticsNodeInteraction {
    try {
        return block(this)
    } catch (e: AssertionError) {
        throw AssertionError("$message ${e.message}")
    }

}