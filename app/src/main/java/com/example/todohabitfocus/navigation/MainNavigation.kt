package com.example.todohabitfocus.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
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
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                fadeIn(animationSpec = tween(400)) + slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(200))
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

@Composable
fun AppBottomBar(
    items: List<BottomNavItem>,
    currentDestination: NavDestination?,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp,
        modifier = Modifier.height(84.dp)
    ) {
        items.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true
            
            val iconScale by animateFloatAsState(
                targetValue = if (isSelected) 1.2f else 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "iconScale"
            )

            val itemColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primary 
                              else MaterialTheme.colorScheme.onSurfaceVariant,
                animationSpec = tween(durationMillis = 300),
                label = "itemColor"
            )

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.scale(iconScale),
                        tint = itemColor
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                        color = itemColor
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}


