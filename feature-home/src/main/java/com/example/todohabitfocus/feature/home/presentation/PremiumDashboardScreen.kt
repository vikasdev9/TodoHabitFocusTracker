package com.example.todohabitfocus.feature.home.presentation

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

// Premium Palette
private val BgColor = Color(0xFFF6F7FB)
private val PrimaryBlue = Color(0xFF3554D1)
private val DarkBlue = Color(0xFF0B132B)
private val GrayText = Color(0xFF7B8190)
private val CardPurple = Color(0xFFE9DDFB)
private val CardBlue = Color(0xFFDDF3FF)
private val CardPeach = Color(0xFFF8DDCF)
private val CardGreen = Color(0xFFD9F5E5)

@Composable
fun PremiumDashboardRoute(
    onNavigateToTasks: () -> Unit = {},
    onNavigateToHabits: () -> Unit = {},
    onNavigateToFocus: () -> Unit = {}
) {
    PremiumDashboardScreen(
        onNavigateToTasks = onNavigateToTasks,
        onNavigateToHabits = onNavigateToHabits,
        onNavigateToFocus = onNavigateToFocus
    )
}

@Composable
fun PremiumDashboardScreen(
    onNavigateToTasks: () -> Unit,
    onNavigateToHabits: () -> Unit,
    onNavigateToFocus: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    Scaffold(
        containerColor = BgColor,
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 24.dp,
                top = 0.dp,
                bottom = padding.calculateBottomPadding() + 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Precise Top Spacing
            item {
                Spacer(modifier = Modifier.statusBarsPadding())
            }

            // 1. Header
            item {
                DashboardHeader(
                    userName = "Alex Morgan",
                    onSettingsClick = {},
                    onNotificationsClick = {},
                    onAddClick = {}
                )
            }

            // 2. Search Bar
            item {
                SearchBar(onSearch = {})
            }

            // 3. Today's Overview
            item {
                TodayOverviewSection()
            }

            // 4. Focus Session Card
            item {
                FocusSessionCard(
                    timeRemaining = "15:24",
                    sessionName = "Design system",
                    onResumeClick = onNavigateToFocus
                )
            }

            // 5. Recent Tasks
            item {
                RecentTasksSection(onSeeAllClick = onNavigateToTasks)
            }

            // 6. Upcoming
            item {
                UpcomingSection()
            }
        }
    }
}

@Composable
fun DashboardHeader(
    userName: String,
    onSettingsClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(Color(0xFFFDCBBD)) // Soft peach for avatar bg
        ) {
            // Placeholder for avatar image
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Greeting
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Good morning",
                style = MaterialTheme.typography.bodyMedium,
                color = GrayText
            )
            Text(
                text = "$userName 👋",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = DarkBlue
            )
        }

        // Action Buttons
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            HeaderIconButton(icon = Icons.Outlined.Settings, onClick = onSettingsClick)
            HeaderIconButton(icon = Icons.Outlined.Notifications, onClick = onNotificationsClick, showDot = true)
            
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = PrimaryBlue,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(48.dp),
                elevation = FloatingActionButtonDefaults.elevation(0.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    }
}

@Composable
fun HeaderIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    showDot: Boolean = false
) {
    Box {
        Surface(
            onClick = onClick,
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = Color.White,
            border = BorderStroke(1.dp, Color(0xFFE5E7EB))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = DarkBlue,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        if (showDot) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Color.Red)
                    .align(Alignment.TopEnd)
                    .offset(x = (-4).dp, y = 4.dp)
                    .border(2.dp, BgColor, CircleShape)
            )
        }
    }
}

@Composable
fun SearchBar(onSearch: (String) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFF3F4F6))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = GrayText,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Search tasks, habits...",
                style = MaterialTheme.typography.bodyLarge,
                color = GrayText
            )
        }
    }
}

@Composable
fun TodayOverviewSection() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "Today's overview",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = DarkBlue
            )
            Text(
                text = "Wed, 12 Jun",
                style = MaterialTheme.typography.bodyMedium,
                color = GrayText
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OverviewCard(
                count = "12",
                label = "Upcoming",
                color = CardBlue,
                textColor = Color(0xFF1E40AF),
                modifier = Modifier.weight(1f)
            )
            OverviewCard(
                count = "6",
                label = "Today",
                color = CardPeach,
                textColor = Color(0xFF92400E),
                modifier = Modifier.weight(1f)
            )
            OverviewCard(
                count = "38",
                label = "Done",
                color = CardGreen,
                textColor = Color(0xFF065F46),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun OverviewCard(
    count: String,
    label: String,
    color: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(32.dp),
        color = color
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = count,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = textColor
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = textColor.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun FocusSessionCard(
    timeRemaining: String,
    sessionName: String,
    onResumeClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = PrimaryBlue
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "FOCUS SESSION",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.7f),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Deep Work · 25 min",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Resume your \"$sessionName\" session",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                // Timer Progress
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { 0.6f },
                        modifier = Modifier.size(64.dp),
                        color = Color.White,
                        strokeWidth = 6.dp,
                        trackColor = Color.White.copy(alpha = 0.2f)
                    )
                    Text(
                        text = timeRemaining,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = onResumeClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(
                        text = "Resume",
                        color = PrimaryBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                HeaderIconButton(icon = Icons.Outlined.Timer, onClick = {}, showDot = false)
            }
        }
    }
}

@Composable
fun RecentTasksSection(onSeeAllClick: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SectionHeader(title = "Recent tasks", actionLabel = "See all", onActionClick = onSeeAllClick)
        
        RecentTaskCard(
            category = "DESIGN",
            title = "Mobile app redesign",
            progress = 0.72f,
            backgroundColor = CardPurple,
            accentColor = Color(0xFF6D28D9),
            pillLabel = "Today"
        )
        RecentTaskCard(
            category = "RESEARCH",
            title = "User interviews — Q3",
            progress = 0.45f,
            backgroundColor = CardBlue,
            accentColor = Color(0xFF2563EB),
            pillLabel = "Thu"
        )
        RecentTaskCard(
            category = "MARKETING",
            title = "Launch campaign assets",
            progress = 0.20f,
            backgroundColor = CardPeach,
            accentColor = Color(0xFFC2410C),
            pillLabel = "Tomorrow",
            tag = "High"
        )
    }
}

@Composable
fun SectionHeader(title: String, actionLabel: String, onActionClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )
        TextButton(onClick = onActionClick) {
            Text(
                text = actionLabel,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
        }
    }
}

@Composable
fun RecentTaskCard(
    category: String,
    title: String,
    progress: Float,
    backgroundColor: Color,
    accentColor: Color,
    pillLabel: String,
    tag: String? = null
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category,
                    style = MaterialTheme.typography.labelSmall,
                    color = accentColor.copy(alpha = 0.7f),
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Avatars
                    OverlappingAvatars()
                    
                    if (tag != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = Color.White.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = tag,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = accentColor
                            )
                        }
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.size(48.dp),
                        color = accentColor,
                        strokeWidth = 5.dp,
                        trackColor = accentColor.copy(alpha = 0.15f)
                    )
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = Color.White.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = pillLabel,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                }
            }
        }
    }
}

@Composable
fun OverlappingAvatars() {
    val colors = listOf(Color(0xFFFFB2B2), Color(0xFFB2FFB2), Color(0xFFB2B2FF))
    Row {
        colors.forEachIndexed { index, color ->
            Box(
                modifier = Modifier
                    .offset(x = (index * (-8)).dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(color)
                    .border(2.dp, Color.White, CircleShape)
            )
        }
        Box(
            modifier = Modifier
                .offset(x = (colors.size * (-8)).dp)
                .size(24.dp)
                .clip(CircleShape)
                .background(Color(0xFFF3F4F6))
                .border(2.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("+1", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GrayText)
        }
    }
}

@Composable
fun UpcomingSection() {

    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {

        // HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Upcoming",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = Color(0xFF081225)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    modifier = Modifier.size(13.dp),
                    tint = Color(0xFF9AA3B2)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = "This week",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF9AA3B2)
                )
            }
        }

        // ITEMS
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            UpcomingItem(
                date = "12",
                month = "JUN",
                title = "Team standup",
                time = "Today · 09:30",
                badgeColor = Color(0xFFE4F4FF),
                textColor = Color(0xFF2196F3)
            )

            UpcomingItem(
                date = "12",
                month = "JUN",
                title = "Design review",
                time = "Today · 11:00",
                badgeColor = Color(0xFFFFF4CC),
                textColor = Color(0xFFB58A00)
            )

            UpcomingItem(
                date = "12",
                month = "JUN",
                title = "1:1 with Sarah",
                time = "Today · 15:00",
                badgeColor = Color(0xFFFFE5EA),
                textColor = Color(0xFFD64D6F)
            )
        }
    }
}

@Composable
fun UpcomingItem(
    date: String,
    month: String,
    title: String,
    time: String,
    badgeColor: Color,
    textColor: Color
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        border = BorderStroke(
            1.dp,
            Color(0xFFF1F3F7)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // DATE BADGE
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(badgeColor),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = month,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(1.dp))

                    Text(
                        text = date,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = textColor
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // TEXT
            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF081225)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = time,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF8A94A6)
                )
            }

            // ADD BUTTON
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF5F7FB)),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color(0xFF6B7280),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
