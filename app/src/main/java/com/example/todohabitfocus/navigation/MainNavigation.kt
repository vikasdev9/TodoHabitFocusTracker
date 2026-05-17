package com.example.todohabitfocus.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.todohabitfocus.feature.analytics.presentation.AnalyticsScreen
import com.example.todohabitfocus.feature.focus.presentation.FocusScreen
import com.example.todohabitfocus.feature.habit.presentation.dashboard.HabitDashboardScreen
import com.example.todohabitfocus.feature.home.presentation.PremiumDashboardRoute
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
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
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
            composable(BottomNavItem.Home.route) {
                PremiumDashboardRoute(
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

@Composable
fun AppBottomBar(
    items: List<BottomNavItem>,
    currentDestination: NavDestination?,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp // Clean white look, no elevation shadow
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
