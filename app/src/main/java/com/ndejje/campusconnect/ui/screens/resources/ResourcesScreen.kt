package com.ndejje.campusconnect.ui.screens.resources

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ndejje.campusconnect.R
import com.ndejje.campusconnect.data.model.ResourceType
import com.ndejje.campusconnect.data.model.UniversityResource

// ── Static resources data ─────────────────────────────────────────────────────
val universityResources = listOf(
    UniversityResource(
        id = 1,
        title = "University Health Centre",
        subtitle = "Free medical consultation – Main Campus",
        contactPhone = "+256 772 000 001",
        contactEmail = "health@ndejjeuniversity.ac.ug",
        resourceType = ResourceType.CONTACT
    ),
    UniversityResource(
        id = 2,
        title = "Academic Registrar",
        subtitle = "Transcripts, results, admissions",
        contactPhone = "+256 772 000 002",
        contactEmail = "registrar@ndejjeuniversity.ac.ug",
        resourceType = ResourceType.CONTACT
    ),
    UniversityResource(
        id = 3,
        title = "Campus Security",
        subtitle = "24/7 Security – Emergency line",
        contactPhone = "+256 772 000 003",
        resourceType = ResourceType.CONTACT
    ),
    UniversityResource(
        id = 4,
        title = "Bursar's Office",
        subtitle = "Fees payment, financial queries",
        contactPhone = "+256 772 000 004",
        contactEmail = "bursar@ndejjeuniversity.ac.ug",
        resourceType = ResourceType.CONTACT
    ),
    UniversityResource(
        id = 5,
        title = "University Library",
        subtitle = "Chief Librarian – research & borrowing",
        contactPhone = "+256 772 000 005",
        contactEmail = "library@ndejjeuniversity.ac.ug",
        resourceType = ResourceType.CONTACT
    ),
    UniversityResource(
        id = 6,
        title = "Student Registration Guide",
        subtitle = "Step-by-step guide for new and returning students",
        pdfUrl = "https://ndejjeuniversity.ac.ug/guides/registration.pdf",
        resourceType = ResourceType.GUIDE
    ),
    UniversityResource(
        id = 7,
        title = "Examination Rules & Regulations",
        subtitle = "Official exam conduct policy – 2025/2026",
        pdfUrl = "https://ndejjeuniversity.ac.ug/guides/exam_rules.pdf",
        resourceType = ResourceType.GUIDE
    ),
    UniversityResource(
        id = 8,
        title = "Fee Structure 2025/2026",
        subtitle = "Tuition and functional fees by programme",
        pdfUrl = "https://ndejjeuniversity.ac.ug/guides/fees.pdf",
        resourceType = ResourceType.GUIDE
    ),
    UniversityResource(
        id = 9,
        title = "Student Handbook",
        subtitle = "Rights, responsibilities, and campus conduct",
        pdfUrl = "https://ndejjeuniversity.ac.ug/guides/handbook.pdf",
        resourceType = ResourceType.GUIDE
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourcesScreen() {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }

    val filtered = remember(searchQuery) {
        universityResources.filter { resource ->
            searchQuery.isBlank() ||
                    resource.title.contains(searchQuery, ignoreCase = true) ||
                    resource.subtitle.contains(searchQuery, ignoreCase = true)
        }
    }

    val contacts = filtered.filter { it.resourceType == ResourceType.CONTACT }
    val guides = filtered.filter { it.resourceType == ResourceType.GUIDE }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.screen_title_resources),
                        style = MaterialTheme.typography.titleLarge
                    )
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
                .padding(horizontal = dimensionResource(R.dimen.spacing_md))
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_sm)))

            // ── Search bar ─────────────────────────────────────────────
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(stringResource(R.string.label_search_resources)) },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius))
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_sm)))

            if (filtered.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.label_no_resources),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = dimensionResource(R.dimen.spacing_sm)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_sm))
                ) {
                    // ── Contacts section ───────────────────────────────
                    if (contacts.isNotEmpty()) {
                        item {
                            SectionHeader(
                                title = stringResource(R.string.label_contacts),
                                icon = { Icon(Icons.Filled.Person, contentDescription = null) }
                            )
                        }
                        items(items = contacts, key = { it.id }) { resource ->
                            ContactCard(
                                resource = resource,
                                onCallClick = { phone ->
                                    val intent = Intent(Intent.ACTION_DIAL).apply {
                                        data = Uri.parse("tel:$phone")
                                    }
                                    context.startActivity(intent)
                                },
                                onEmailClick = { email ->
                                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                                        data = Uri.parse("mailto:$email")
                                        putExtra(Intent.EXTRA_SUBJECT, "Inquiry from Campus Connect App")
                                    }
                                    context.startActivity(intent)
                                }
                            )
                        }
                    }

                    // ── Guides section ─────────────────────────────────
                    if (guides.isNotEmpty()) {
                        item {
                            SectionHeader(
                                title = stringResource(R.string.label_guides),
                                icon = { Icon(Icons.Filled.Description, contentDescription = null) }
                            )
                        }
                        items(items = guides, key = { it.id }) { resource ->
                            GuideCard(
                                resource = resource,
                                onDownloadClick = { url ->
                                    val intent = Intent(Intent.ACTION_VIEW).apply {
                                        data = Uri.parse(url)
                                    }
                                    context.startActivity(intent)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    icon: @Composable () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.spacing_xs))
    ) {
        icon()
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_sm)))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ContactCard(
    resource: UniversityResource,
    onCallClick: (String) -> Unit,
    onEmailClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.card_elevation))
    ) {
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.spacing_md))) {
            Text(
                text = resource.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = resource.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_sm)))
            Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_sm))) {
                resource.contactPhone?.let { phone ->
                    AssistChip(
                        onClick = { onCallClick(phone) },
                        label = { Text(stringResource(R.string.label_call)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Call,
                                contentDescription = stringResource(R.string.cd_contact_icon),
                                modifier = Modifier.size(dimensionResource(R.dimen.icon_size_sm))
                            )
                        }
                    )
                }
                resource.contactEmail?.let { email ->
                    AssistChip(
                        onClick = { onEmailClick(email) },
                        label = { Text(stringResource(R.string.label_email_action)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Email,
                                contentDescription = null,
                                modifier = Modifier.size(dimensionResource(R.dimen.icon_size_sm))
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun GuideCard(
    resource: UniversityResource,
    onDownloadClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.card_elevation))
    ) {
        Row(
            modifier = Modifier.padding(dimensionResource(R.dimen.spacing_md)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.PictureAsPdf,
                contentDescription = stringResource(R.string.cd_guide_icon),
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(dimensionResource(R.dimen.icon_size_lg))
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_md)))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = resource.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = resource.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            resource.pdfUrl?.let { url ->
                AssistChip(
                    onClick = { onDownloadClick(url) },
                    label = { Text(stringResource(R.string.label_download)) }
                )
            }
        }
    }
}
