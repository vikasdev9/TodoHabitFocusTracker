package com.example.todohabitfocus.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.todohabitfocus.feature.analytics.presentation.AnalyticsScreen
import com.example.todohabitfocus.feature.focus.presentation.FocusScreen
import com.example.todohabitfocus.feature.habit.presentation.add_edit.AddEditHabitScreen
import com.example.todohabitfocus.feature.habit.presentation.dashboard.HabitDashboardScreen
import com.example.todohabitfocus.feature.home.presentation.PremiumDashboardRoute
import com.example.todohabitfocus.feature.onboarding.presentation.OnboardingScreen
import com.example.todohabitfocus.feature.task.presentation.TaskListRoute
import com.example.todohabitfocus.feature.task.presentation.add_edit.AddEditTaskScreen
import com.example.todohabitfocus.presentation.appstart.AppStartUiState
import com.example.todohabitfocus.presentation.appstart.AppStartViewModel
import com.example.todohabitfocus.presentation.appstart.SplashScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigation(
    viewModel: AppStartViewModel = hiltViewModel()
) {
    val appStartState by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Tasks,
        BottomNavItem.Habits,
        BottomNavItem.Focus,
        BottomNavItem.Analytics
    )

    when (val state = appStartState) {
        AppStartUiState.Loading -> {
            SplashScreen()
        }
        is AppStartUiState.Nav -> {
            val startRoute = if (!state.onboardingCompleted) "onboarding" else BottomNavItem.Home.route

            Scaffold(
                bottomBar = {
                    val showBottomBar = currentDestination?.route != "onboarding"
                    if (showBottomBar) {
                        AppBottomBar(
                            items = items,
                            currentDestination = currentDestination,
                            onNavigate = { route ->
                                navController.navigate(route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = startRoute,
                    modifier = Modifier.padding(innerPadding),
                    enterTransition = {
                        fadeIn(animationSpec = tween(300)) + slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Start,
                            animationSpec = tween(300)
                        )
                    },
                    exitTransition = {
                        fadeOut(animationSpec = tween(300)) + slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Start,
                            animationSpec = tween(300)
                        )
                    },
                    popEnterTransition = {
                        fadeIn(animationSpec = tween(300)) + slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.End,
                            animationSpec = tween(300)
                        )
                    },
                    popExitTransition = {
                        fadeOut(animationSpec = tween(300)) + slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.End,
                            animationSpec = tween(300)
                        )
                    }
                ) {
                    composable("onboarding") {
                        OnboardingScreen(
                            onFinish = {
                                navController.navigate(BottomNavItem.Home.route) {
                                    popUpTo("onboarding") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(BottomNavItem.Home.route) {
                        PremiumDashboardRoute(
                            onNavigateToTasks = { navController.navigate(BottomNavItem.Tasks.route) },
                            onNavigateToHabits = { navController.navigate(BottomNavItem.Habits.route) },
                            onNavigateToFocus = { navController.navigate(BottomNavItem.Focus.route) },
                            onNavigateToAddEditTask = { taskId ->
                                val route = if (taskId != null) "add_edit_task?taskId=$taskId" else "add_edit_task"
                                navController.navigate(route)
                            }
                        )
                    }

                    composable(
                        route = "add_edit_task?taskId={taskId}",
                        arguments = listOf(
                            navArgument("taskId") {
                                type = NavType.StringType
                                nullable = true
                                defaultValue = null
                            }
                        )
                    ) { backStackEntry ->
                        val taskId = backStackEntry.arguments?.getString("taskId")
                        AddEditTaskScreen(
                            taskId = taskId,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    composable(BottomNavItem.Tasks.route) { TaskListRoute() }
                    composable(BottomNavItem.Habits.route) {
                        HabitDashboardScreen(
                            onAddHabitClick = { navController.navigate("add_edit_habit") },
                            onHabitClick = { /* Navigate to Habit Details */ }
                        )
                    }

                    composable("add_edit_habit") {
                        AddEditHabitScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable(BottomNavItem.Focus.route) { FocusScreen() }
                    composable(BottomNavItem.Analytics.route) {
                        AnalyticsScreen(onBackClick = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}

@Composable
fun AppBottomBar(
    items: List<BottomNavItem>,
    currentDestination: NavDestination?,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        items.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true
            
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
