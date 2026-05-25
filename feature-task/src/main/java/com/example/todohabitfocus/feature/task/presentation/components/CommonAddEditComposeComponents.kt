package com.example.todohabitfocus.feature.task.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todohabitfocus.feature.task.data.TaskCategory

@Composable
fun SectionTitle(title: String) {

    Text(
        text = title, color = Color(0xFF5B5BD6), fontWeight = FontWeight.SemiBold, fontSize = 15.sp
    )

    Spacer(modifier = Modifier.height(10.dp))

}

@Composable
fun PriorityButton(
    text: String, selected: Boolean, onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (selected) Color(0xFFFFE7B8)
                else Color.White
            )
            .clickable {
                onClick()
            }
            .padding(horizontal = 28.dp, vertical = 14.dp), contentAlignment = Alignment.Center) {

        Text(
            text = text, color = if (selected) Color(0xFFFF9800)
            else Color.Gray, fontWeight = FontWeight.Bold
        )

    }

}

@Composable
fun CategoryChip(
    category: TaskCategory, selected: Boolean, onClick: () -> Unit
) {

    Surface(
        shape = RoundedCornerShape(18.dp), color = if (selected) category.color.copy(alpha = 0.2f)
        else Color.White, border = BorderStroke(
            1.dp, if (selected) category.color
            else Color.LightGray
        ), modifier = Modifier
            .padding(end = 10.dp)
            .clickable {
                onClick()
            }) {

        Row(
            modifier = Modifier.padding(
                horizontal = 18.dp, vertical = 12.dp
            ), verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                category.icon, contentDescription = null, tint = category.color
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(category.name)

        }

    }

}

@Composable
fun AddCategoryButton(
    onClick: () -> Unit
) {

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFFF0F2FF),
        modifier = Modifier.clickable {
            onClick()
        }) {

        Row(
            modifier = Modifier.padding(
                horizontal = 18.dp, vertical = 12.dp
            ), verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                Icons.Default.Add, contentDescription = null, tint = Color(0xFF4B5EFF)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text("Custom")

        }

    }

}

@Composable
fun PeriodChip(text: String) {

    FilterChip(selected = false, onClick = {}, label = {
        Text(text)
    })

}