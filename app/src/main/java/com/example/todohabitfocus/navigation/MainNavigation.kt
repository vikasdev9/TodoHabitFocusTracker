package com.example.todohabitfocus.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.todohabitfocus.feature.analytics.presentation.AnalyticsScreen
import com.example.todohabitfocus.feature.focus.presentation.FocusScreen
import com.example.todohabitfocus.feature.habit.presentation.dashboard.HabitDashboardScreen
import com.example.todohabitfocus.feature.home.presentation.HomeRoute
import com.example.todohabitfocus.feature.task.presentation.TaskListRoute

@Composable
fun MainNavigation() {
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

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = spring(stiffness = 500f)
                ) + fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = spring(stiffness = 500f)
                ) + fadeOut()
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = spring(stiffness = 500f)
                ) + fadeIn()
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = spring(stiffness = 500f)
                ) + fadeOut()
            }
        ) {
            composable(BottomNavItem.Home.route) {
                HomeRoute(
                    onNavigateToTasks = { navController.navigate(BottomNavItem.Tasks.route) },
                    onNavigateToHabits = { navController.navigate(BottomNavItem.Habits.route) },
                    onNavigateToFocus = { navController.navigate(BottomNavItem.Focus.route) }
                )
            }
            composable(BottomNavItem.Tasks.route) { TaskListRoute() }
            composable(BottomNavItem.Habits.route) {
                HabitDashboardScreen(
                    onAddHabitClick = { /* Navigate to Add Habit */ },
                    onHabitClick = { /* Navigate to Habit Details */ }
                )
            }
            composable(BottomNavItem.Focus.route) { FocusScreen() }
            composable(BottomNavItem.Analytics.route) {
                AnalyticsScreen(onBackClick = { navController.popBackStack() })
            }
        }
    }
}

