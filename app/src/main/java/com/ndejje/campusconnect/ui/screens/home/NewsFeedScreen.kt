package com.ndejje.campusconnect.ui.screens.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ndejje.campusconnect.R
import com.ndejje.campusconnect.data.model.Announcement
import com.ndejje.campusconnect.data.model.AnnouncementCategory
import com.ndejje.campusconnect.ui.theme.AcademicBlue
import com.ndejje.campusconnect.ui.theme.EmergencyRed
import com.ndejje.campusconnect.ui.theme.EventPurple
import com.ndejje.campusconnect.ui.theme.GeneralGrey
import com.ndejje.campusconnect.viewmodel.UserRole
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsFeedScreen(
    announcements: List<Announcement>,
    selectedCategory: AnnouncementCategory?,
    unreadCount: Int,
    userRole: UserRole,
    onCategorySelected: (AnnouncementCategory?) -> Unit,
    onAnnouncementClicked: (Announcement) -> Unit,
    onPostAnnouncement: (String, String, AnnouncementCategory) -> Unit,
    onLogout: () -> Unit,
    isFilteringUnread: Boolean = false,
    onToggleUnreadFilter: () -> Unit = {},
    onDeleteAnnouncement: (Int) -> Unit = {}
) {
    var selectedAnnouncementForDetail by remember { mutableStateOf<Announcement?>(null) }
    var showPostDialog by remember { mutableStateOf(false) }
    var showLogoutConfirmation by remember { mutableStateOf(false) }
    var announcementToDelete by remember { mutableStateOf<Announcement?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.screen_title_home),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = onToggleUnreadFilter) {
                        BadgedBox(
                            badge = { 
                                if (unreadCount > 0) {
                                    Badge { Text(text = unreadCount.toString()) } 
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isFilteringUnread) Icons.Filled.NotificationsActive else Icons.Filled.Notifications,
                                contentDescription = "Toggle Unread Notifications",
                                tint = if (isFilteringUnread) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }

                    if (userRole == UserRole.STAFF || userRole == UserRole.ADMIN) {
                        IconButton(onClick = { showPostDialog = true }) {
                            Icon(
                                imageVector = Icons.Filled.AddComment,
                                contentDescription = "Post Announcement",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                    
                    IconButton(onClick = { showLogoutConfirmation = true }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.spacing_md)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_sm)),
                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.spacing_sm))
            ) {
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { onCategorySelected(null) },
                        label = { Text(stringResource(R.string.label_filter_all)) }
                    )
                }
                items(AnnouncementCategory.values()) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { onCategorySelected(category) },
                        label = { Text(categoryLabel(category)) }
                    )
                }
            }

            if (announcements.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (isFilteringUnread) "No unread announcements" else stringResource(R.string.label_no_announcements),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(
                        horizontal = dimensionResource(R.dimen.spacing_md),
                        vertical = dimensionResource(R.dimen.spacing_sm)
                    ),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_sm))
                ) {
                    items(announcements) { announcement ->
                        AnnouncementCard(
                            announcement = announcement,
                            onClick = {
                                onAnnouncementClicked(announcement)
                                selectedAnnouncementForDetail = announcement
                            },
                            isAdmin = userRole == UserRole.ADMIN,
                            onDeleteClick = { announcementToDelete = announcement }
                        )
                    }
                }
            }
        }

        // --- Dialogs ---
        selectedAnnouncementForDetail?.let { announcement ->
            AnnouncementDetailDialog(
                announcement = announcement,
                onDismiss = { selectedAnnouncementForDetail = null }
            )
        }

        if (showPostDialog) {
            PostAnnouncementDialog(
                onDismiss = { showPostDialog = false },
                onPost = { title, body, category ->
                    onPostAnnouncement(title, body, category)
                    showPostDialog = false
                }
            )
        }

        if (showLogoutConfirmation) {
            AlertDialog(
                onDismissRequest = { showLogoutConfirmation = false },
                title = { Text("Logout") },
                text = { Text("Are you sure you want to log out?") },
                confirmButton = {
                    TextButton(onClick = onLogout) { Text("Logout") }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutConfirmation = false }) { Text("Cancel") }
                }
            )
        }

        announcementToDelete?.let { announcement ->
            AlertDialog(
                onDismissRequest = { announcementToDelete = null },
                title = { Text("Delete Announcement") },
                text = { Text("This will permanently remove the announcement. Proceed?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onDeleteAnnouncement(announcement.id)
                            announcementToDelete = null
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { announcementToDelete = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun AnnouncementDetailDialog(
    announcement: Announcement,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = categoryIcon(announcement.category),
                        contentDescription = null,
                        tint = categoryColor(announcement.category),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = categoryLabel(announcement.category),
                        style = MaterialTheme.typography.labelLarge,
                        color = categoryColor(announcement.category)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = announcement.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "By: ${announcement.postedBy}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = formatTimestamp(announcement.timestamp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = announcement.body,
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.2
                )

                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Close")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnnouncementCard(
    announcement: Announcement,
    onClick: () -> Unit,
    isAdmin: Boolean = false,
    onDeleteClick: () -> Unit = {}
) {
    val categoryColor = categoryColor(announcement.category)
    val categoryIcon = categoryIcon(announcement.category)

    val containerColor by animateColorAsState(
        targetValue = if (!announcement.isRead) 
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        else 
            MaterialTheme.colorScheme.surface,
        label = "color"
    )

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (!announcement.isRead) 4.dp else 1.dp
        ),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.spacing_md)),
            verticalAlignment = Alignment.Top
        ) {
            Box {
                Icon(
                    imageVector = categoryIcon,
                    contentDescription = null,
                    tint = if (!announcement.isRead) categoryColor else categoryColor.copy(alpha = 0.5f),
                    modifier = Modifier.size(dimensionResource(R.dimen.icon_size_lg))
                )
                if (!announcement.isRead) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .align(Alignment.TopEnd)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.error)
                    )
                }
            }

            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_sm)))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = announcement.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (!announcement.isRead) FontWeight.ExtraBold else FontWeight.Medium,
                        color = if (!announcement.isRead) 
                            MaterialTheme.colorScheme.onSurface 
                        else 
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    
                    if (isAdmin) {
                        IconButton(
                            onClick = onDeleteClick,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xs)))

                Text(
                    text = announcement.body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (!announcement.isRead)
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xs)))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = announcement.postedBy,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (!announcement.isRead) 0.6f else 0.4f)
                    )
                    Text(
                        text = formatTimestamp(announcement.timestamp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (!announcement.isRead) 0.6f else 0.4f)
                    )
                }
            }
        }
    }
}

private fun categoryLabel(category: AnnouncementCategory) = when (category) {
    AnnouncementCategory.ACADEMIC -> "Academic"
    AnnouncementCategory.EVENTS -> "Events"
    AnnouncementCategory.EMERGENCY -> "Emergency"
    AnnouncementCategory.GENERAL -> "General"
}

private fun categoryColor(category: AnnouncementCategory): Color = when (category) {
    AnnouncementCategory.ACADEMIC -> AcademicBlue
    AnnouncementCategory.EVENTS -> EventPurple
    AnnouncementCategory.EMERGENCY -> EmergencyRed
    AnnouncementCategory.GENERAL -> GeneralGrey
}

private fun categoryIcon(category: AnnouncementCategory): ImageVector = when (category) {
    AnnouncementCategory.ACADEMIC -> Icons.Filled.CalendarMonth
    AnnouncementCategory.EVENTS -> Icons.Filled.Campaign
    AnnouncementCategory.EMERGENCY -> Icons.Filled.Warning
    AnnouncementCategory.GENERAL -> Icons.Filled.Info
}

private fun formatTimestamp(timestamp: Long): String {
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return formatter.format(Date(timestamp))
}

@Composable
fun PostAnnouncementDialog(
    onDismiss: () -> Unit,
    onPost: (String, String, AnnouncementCategory) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(AnnouncementCategory.GENERAL) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "New Announcement",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = body,
                    onValueChange = { body = it },
                    label = { Text("Message") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Category", style = MaterialTheme.typography.labelLarge)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AnnouncementCategory.values().forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(categoryLabel(cat)) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onPost(title, body, category) },
                        enabled = title.isNotBlank() && body.isNotBlank()
                    ) {
                        Text("Post")
                    }
                }
            }
        }
    }
}
