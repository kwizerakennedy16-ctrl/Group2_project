package com.ndejje.campusconnect.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.ndejje.campusconnect.R
import com.ndejje.campusconnect.data.model.TimetableEntry
import com.ndejje.campusconnect.data.model.TimetableType
import com.ndejje.campusconnect.ui.theme.AcademicBlue
import com.ndejje.campusconnect.ui.theme.EmergencyRed
import com.ndejje.campusconnect.ui.theme.NdejjeGold80

private val tabTitles = listOf("Lectures", "Exams", "Deadlines")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcademicDashboardScreen(
    teacher: List<TimetableEntry>,
    exams: List<TimetableEntry>,
    deadlines: List<TimetableEntry>,
    onLogout: () -> Unit // Added logout callback
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.screen_title_dashboard),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // ── Tab row ────────────────────────────────────────────────
            PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    )
                }
            }

            // ── Tab content ────────────────────────────────────────────
            val currentList = when (selectedTabIndex) {
                0 -> lectures
                1 -> exams
                2 -> deadlines
                else -> emptyList()
            }
            val emptyMessage = when (selectedTabIndex) {
                0 -> stringResource(R.string.label_no_timetable)
                1 -> stringResource(R.string.label_no_exams)
                else -> stringResource(R.string.label_no_deadlines)
            }

            if (currentList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emptyMessage,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(
                        horizontal = dimensionResource(R.dimen.spacing_md),
                        vertical = dimensionResource(R.dimen.spacing_md)
                    ),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_sm))
                ) {
                    items(
                        items = currentList,
                        key = { entry -> entry.id }
                    ) { entry ->
                        TimetableCard(entry = entry)
                    }
                }
            }
        }
    }
}

@Composable
private fun TimetableCard(entry: TimetableEntry) {
    val accentColor = entryAccentColor(entry.entryType)
    val entryIcon = entryIcon(entry.entryType)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.card_elevation))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.spacing_md)),
            verticalAlignment = Alignment.Top
        ) {
            // Time column
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(width = dimensionResource(R.dimen.spacing_xxl))
            ) {
                Icon(
                    imageVector = entryIcon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(dimensionResource(R.dimen.icon_size_md))
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xs)))
                Text(
                    text = entry.startTime,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
                Text(
                    text = entry.endTime,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.width(width = dimensionResource(R.dimen.spacing_md)))

            // Details column
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.courseName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xs)))
                Text(
                    text = entry.courseCode,
                    style = MaterialTheme.typography.bodyMedium,
                    color = accentColor
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xs)))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Place,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.size(dimensionResource(R.dimen.icon_size_sm))
                    )
                    Spacer(modifier = Modifier.width(width = dimensionResource(R.dimen.spacing_xs)))
                    Text(
                        text = entry.venue,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xs)))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.AccessTime,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.size(dimensionResource(R.dimen.icon_size_sm))
                    )
                    Spacer(modifier = Modifier.width(width = dimensionResource(R.dimen.spacing_xs)))
                    Text(
                        text = entry.dayOfWeek,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xs)))

                Text(
                    text = "${stringResource(R.string.label_lecturer)}: ${entry.lecturer}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}

private fun entryAccentColor(type: TimetableType): Color = when (type) {
    TimetableType.LECTURE -> AcademicBlue
    TimetableType.EXAM -> EmergencyRed
    TimetableType.DEADLINE -> NdejjeGold80
}

private fun entryIcon(type: TimetableType): ImageVector = when (type) {
    TimetableType.LECTURE -> Icons.Filled.Book
    TimetableType.EXAM -> Icons.Filled.Assignment
    TimetableType.DEADLINE -> Icons.Filled.Warning
}
